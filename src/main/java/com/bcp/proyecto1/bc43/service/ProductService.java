package com.bcp.proyecto1.bc43.service;

import com.bcp.proyecto1.bc43.model.Client;
import com.bcp.proyecto1.bc43.model.ClientType;
import com.bcp.proyecto1.bc43.model.Product;
import com.bcp.proyecto1.bc43.model.ProductType;
import com.bcp.proyecto1.bc43.repository.ClientRepository;
import com.bcp.proyecto1.bc43.repository.ProductRepository;
import io.reactivex.rxjava3.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    @Autowired
    private ClientService clientService;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Observable<Product> getAllProducts() {
        return Observable.fromPublisher(productRepository.findAll());
    }

    public Observable<Product> createProduct(Product product) {
        return Observable.create(emitter -> {
            // Obtener el cliente asociado al producto
            Client client = clientService.getClientById(product.getIdClient()).blockingFirst();
            ProductType[] types = {ProductType.AHORRO,ProductType.CUENTA_CORRIENTE,ProductType.PLAZO_FIJO}
            if (client != null && productRepository.countByClientIdAndTypeContains(product.getIdClient(), types) > 0) {
                emitter.onError(new IllegalArgumentException("El cliente ya tiene una cuenta de ahorro, cuenta corriente o cuenta a plazo fijo."));
            } else {
                System.out.println(client);
                productRepository.save(product)
                        .doOnSuccess(savedProduct -> emitter.onComplete())
                        .doOnError(error -> emitter.onError(error))
                        .subscribe();
            }
        });
    }

    public Completable validClientPersonal(Product product) {
        ProductType[] types = {ProductType.AHORRO, ProductType.CUENTA_CORRIENTE, ProductType.PLAZO_FIJO};

        return clientService.getClientById(product.getIdClient())
                .flatMapCompletable(existingClient -> {
                    if (existingClient.getType() != ClientType.PERSONAL) {
                        String errorMessage = "Client is not of type PERSONAL";
                        return Completable.error(new IllegalArgumentException(errorMessage));
                    } else {
                        return productRepository.countByClientIdAndTypeContains(product.getIdClient(), types)
                                .flatMapCompletable(count -> {
                                    if (count > 0) {
                                        String errorMessage = "Exceeded the maximum number of products for the client";
                                        return Completable.error(new IllegalArgumentException(errorMessage));
                                    } else {
                                        System.out.println("guardar");
                                        return Completable.fromPublisher(productRepository.save(product));
                                    }
                                });
                    }
                })
                .onErrorResumeNext(error -> {
                    String errorMessage = "An error occurred while validating the client";
                    return Completable.error(new Exception(errorMessage));
                });
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
