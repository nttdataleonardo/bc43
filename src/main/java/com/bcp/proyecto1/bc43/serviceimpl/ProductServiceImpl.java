package com.bcp.proyecto1.bc43.serviceimpl;

import com.bcp.proyecto1.bc43.exceptions.ConflictException;
import com.bcp.proyecto1.bc43.exceptions.NotFoundException;
import com.bcp.proyecto1.bc43.model.Client;
import com.bcp.proyecto1.bc43.model.ClientType;
import com.bcp.proyecto1.bc43.model.Product;
import com.bcp.proyecto1.bc43.model.ProductType;
import com.bcp.proyecto1.bc43.repository.ProductRepository;
import com.bcp.proyecto1.bc43.service.ProductService;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.concurrent.Flow;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    @Autowired
    private ClientServiceImpl clientService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Override
    public Observable<Product> getAllProducts() {
        return Observable.fromPublisher(productRepository.findAll());
    }
    @Override
    public Maybe<Product> createProduct(Product product) {
        return clientService.getClientById(product.getIdClient())
                .switchIfEmpty(Maybe.error(new NotFoundException("Non existent clientId: " + product.getIdClient())))
                .flatMap(client -> {
                    return validateClientAndProduct(client, product);
                });
    }
    @Override
    public Maybe<Product> validateClientAndProduct(Client client, Product product) {
        ProductType[] typesToPersonal = {ProductType.AHORRO, ProductType.CUENTA_CORRIENTE, ProductType.PLAZO_FIJO};
        ProductType[] typesToBusiness = {ProductType.AHORRO, ProductType.PLAZO_FIJO};
        if (ClientType.PERSONAL.equals(client.getType())) { //CLIENTE PERSONAL
            return existsProductByTypes(product, typesToPersonal,"Un cliente personal solo puede tener un máximo de una cuenta de ahorro, una cuenta corriente o cuentas a plazo fijo");
        } else { // CLIENTE EMPRESARIAL
            return existsProductByTypes(product, typesToBusiness,"Un cliente empresarial no puede tener una cuenta de ahorro o de plazo fijo, pero sí múltiples cuentas corrientes");
        }
    }

    public Maybe<Product> existsProductByTypes(Product product, ProductType[] typesProduct, String errorMessage) {
        Maybe<Product> productsFound = productRepository.existsByClientIdAndTypeContains(product.getIdClient(), typesProduct);
        return  productsFound.flatMap(
                productEntity -> Maybe.error(new ConflictException(errorMessage)),
                (error) -> Maybe.error(error) ,
                () -> Maybe.just(product)
        ).flatMapSingle(single -> saveProduct(single).toSingle());
    }

    @Override
    public Maybe<Product> saveProduct(Product product) {
        return Maybe.fromPublisher(productRepository.save(product));
    }

    @Override
    public Observable<Product> updateProduct(String id, Product updatedProduct) {
        return Observable.create(emitter -> {
            productRepository.findById(id)
                    .subscribe(existingProduct -> {
                        existingProduct.setName(updatedProduct.getName());
                        existingProduct.setType(updatedProduct.getType());
                        productRepository.save(existingProduct)
                                .subscribe(savedProduct -> {
                                    emitter.onNext(savedProduct);
                                    emitter.onComplete();
                                }, error -> emitter.onError(error));
                    }, error -> emitter.onError(error));
        });
    }
    @Override
    public Maybe<Product> getProductById(String id) {
        return Maybe.fromPublisher(productRepository.findById(id));
    }
    @Override
    public Observable deleteProductById(String id) {
        return Observable.fromPublisher(productRepository.deleteById(id));
    }

    @Override
    public Observable<Product> depositProduct(String id, Product updatedProduct) {
        return Observable.create(emitter -> {
            productRepository.findById(id)
                    .subscribe(existingProduct -> {
                        existingProduct.setBalance(existingProduct.getBalance().add(updatedProduct.getBalance()));
                        productRepository.save(existingProduct)
                                .subscribe(savedProduct -> {
                                    emitter.onNext(savedProduct);
                                    emitter.onComplete();
                                }, error -> emitter.onError(error));
                    }, error -> emitter.onError(error));
        });
    }

    @Override
    public Observable<Product> withdrawal(String id, Product updatedProduct) {
        return Observable.create(emitter -> {
            productRepository.findById(id)
                    .subscribe(existingProduct -> {
                        existingProduct.setBalance(existingProduct.getBalance().subtract(updatedProduct.getBalance()));
                        productRepository.save(existingProduct)
                                .subscribe(savedProduct -> {
                                    emitter.onNext(savedProduct);
                                    emitter.onComplete();
                                }, error -> emitter.onError(error));
                    }, error -> emitter.onError(error));
        });
    }

    @Override
    public Maybe<Product> paycredit(String id, Product product) {
        return  Maybe.fromPublisher(productRepository.findById(id))
                .flatMap(
                productEntity -> {
                    BigDecimal montocredito = productEntity.getBalance();
                    product.setBalance(montocredito.add(product.getPaycredit()));
                    return Maybe.just(product);
                },
                (error) -> Maybe.error(error) ,
                () -> Maybe.just(product)
        ).flatMapSingle(single -> saveProduct(single).toSingle());
    }

    @Override
    public Maybe<Product> chargeconsumptions(String id, Product product) {
        ProductType[] typesCredit = {ProductType.TARJETA_CREDITO_PERSONAL, ProductType.TARJETA_CREDITO_EMPRESARIAL};
        Maybe<Product> productsFound = productRepository.existsByIdAndTypeContains(id, typesCredit);
        return  productsFound
                .switchIfEmpty(Maybe.error(new ConflictException("No es una cuenta de crédito")))
                .flatMap(
                        productEntity -> {
                            BigDecimal saldodisponible = productEntity.getBalance();
                            BigDecimal consumo = product.getPaycredit(); //product.getPaycredit() = consumo por el cliente, balance usado para el flujo de dinero que fluctua siendo el reflejo del credito
                            BigDecimal balanceUpdate = saldodisponible.subtract(consumo);

                            compareToAmount(saldodisponible,consumo).subscribe(result->{
                               if(result){
                                   product.setBalance(saldodisponible);
                               }else {
                                   product.setBalance(balanceUpdate);
                               }
                            });

                            return Maybe.just(productEntity);
                        },
                        (error) -> Maybe.error(error) ,
                        () -> Maybe.just(product)
                ).flatMapSingle(single -> saveProduct(single).toSingle());
    }

    public Single<Boolean> compareToAmount(BigDecimal monto1, BigDecimal monto2) {
        return Maybe.just(monto1)
                .flatMap(m1 -> Maybe.just(monto1).map(m2 -> m1.compareTo(monto2) < 0))
                .defaultIfEmpty(false);
    }

    @Override
    public Flowable<Product> availablebalances(String id){
        ProductType[] typesCreditsAndAccountBank = { ProductType.AHORRO, ProductType.CUENTA_CORRIENTE, ProductType.PLAZO_FIJO,
                ProductType.TARJETA_CREDITO_PERSONAL, ProductType.TARJETA_CREDITO_EMPRESARIAL};
        return Flowable.fromPublisher(this.productRepository.existsByClientIdAndTypeContainsAll(id,typesCreditsAndAccountBank));
    }
}
