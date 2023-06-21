package com.bcp.proyecto1.bc43.service;

import com.bcp.proyecto1.bc43.model.Client;
import com.bcp.proyecto1.bc43.model.ClientType;
import com.bcp.proyecto1.bc43.model.Product;
import com.bcp.proyecto1.bc43.model.ProductType;
import com.bcp.proyecto1.bc43.repository.ClientRepository;
import com.bcp.proyecto1.bc43.repository.ProductRepository;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
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

    public Flowable<Product> getAllProducts() {
        return Flowable.fromPublisher(productRepository.findAll());
    }

    public Completable createProduct(Product product) {
        return Completable.create(emitter -> {
            productRepository.save(product)
                    .doOnSuccess(savedProduct -> emitter.onComplete())
                    .doOnError(error -> emitter.onError(error))
                    .subscribe();
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

    public Completable updateProduct(String id, Product updatedProduct) {
        return Maybe.fromPublisher(productRepository.findById(id))
                .flatMapCompletable(existingProduct -> {
                    existingProduct.setName(updatedProduct.getName());
                    existingProduct.setType(updatedProduct.getType());
                    return Completable.fromPublisher(productRepository.save(existingProduct));
                });
    }

    public Flowable<Product> getProductById(String id) {
        return Flowable.fromPublisher(productRepository.findById(id));
    }

    public Completable deleteProductById(String id) {
        return Completable.fromPublisher(productRepository.deleteById(id));
    }
}
