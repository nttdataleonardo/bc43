package com.bcp.proyecto1.bc43.serviceimpl;

import com.bcp.proyecto1.bc43.model.Client;
import com.bcp.proyecto1.bc43.model.ClientType;
import com.bcp.proyecto1.bc43.model.Product;
import com.bcp.proyecto1.bc43.model.ProductType;
import com.bcp.proyecto1.bc43.repository.ProductRepository;
import com.bcp.proyecto1.bc43.service.ClientService;
import com.bcp.proyecto1.bc43.service.ProductService;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    @Autowired
    private ClientServiceImpl clientService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Observable<Product> getAllProducts() {
        return Observable.fromPublisher(productRepository.findAll());
    }

    /*public Observable<Product> createProduct(Product product) {
        return Observable.create(emitter -> {
            productRepository.save(product)
                    .doOnSuccess(savedClient -> emitter.onComplete())
                    .doOnError(error -> emitter.onError(error))
                    .subscribe();
        });
    }*/

    public Observable<Product> createProduct(Product product) {
        return clientService.getClientById(product.getIdClient())
                .flatMap(client -> validateClientAndProduct(client, product).toObservable())
                .flatMap(validatedProduct -> saveProduct(validatedProduct));
    }

    public Single<Product> validateClientAndProduct(Client client, Product product) {
        if (client.getType() == ClientType.PERSONAL) {
            ProductType[] types = {ProductType.AHORRO, ProductType.CUENTA_CORRIENTE, ProductType.PLAZO_FIJO};
            return productRepository.countByClientIdAndTypeContains(product.getIdClient(), types)
                    .flatMap(count -> {
                        if (count > 0) {
                            return Single.error(new IllegalArgumentException("El cliente personal ya tiene una cuenta de ahorro, cuenta corriente o cuenta a plazo fijo."));
                        } else {
                            return Single.just(product);
                        }
                    });
        } else {
            return Single.just(product);
        }
    }

    public Observable<Product> saveProduct(Product product) {
        return Observable.fromPublisher(productRepository.save(product));
    }

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

    public Observable<Product> getProductById(String id) {
        return Observable.fromPublisher(productRepository.findById(id));
    }

    public Observable deleteProductById(String id) {
        return Observable.fromPublisher(productRepository.deleteById(id));
    }
}
