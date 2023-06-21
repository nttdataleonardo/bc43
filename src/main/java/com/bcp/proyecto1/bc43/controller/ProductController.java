package com.bcp.proyecto1.bc43.controller;

import com.bcp.proyecto1.bc43.model.Product;
import com.bcp.proyecto1.bc43.service.ProductService;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flowable<Product> getAllUsers() {
        return productService.getAllProducts();
    }

    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Completable createProduct(@RequestBody Product Product) {
        return productService.createProduct(Product);
    }

    @PutMapping(value = "/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Completable updateProduct(@PathVariable String id, @RequestBody Product updatedProduct) {
        return productService.updateProduct(id, updatedProduct);
    }

    @GetMapping(value = "/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flowable<Product> getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Completable deleteProductById(@PathVariable String id) {
        return productService.deleteProductById(id);
    }

    /*@PostMapping(value = "/validClientPersonal", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flowable<Product> validClientPersonalEndpoint(@RequestBody Product product) {
        return Flowable.just(product)
                .observeOn(Schedulers.io())
                .flatMapCompletable(productService::validClientPersonal)
                .andThen(Completable.fromAction(() -> System.out.println("Processing...")))
                .andThen(Flowable.just(product))
                .onErrorResumeNext(error -> {
                    String errorMessage = "An error occurred while validating the client";
                    System.out.println(errorMessage);
                    return Flowable.error(new Exception(errorMessage));
                })
                .doOnError(error -> System.out.println(error.getMessage()))
                .toFlowable();
    }*/
}
