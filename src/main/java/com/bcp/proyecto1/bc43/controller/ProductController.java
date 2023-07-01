package com.bcp.proyecto1.bc43.controller;

import com.bcp.proyecto1.bc43.model.Product;
import com.bcp.proyecto1.bc43.service.ProductService;
import io.reactivex.rxjava3.core.*;
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
    public Observable<Product> getAllUsers() {
        return productService.getAllProducts();
    }

    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Maybe<Product> createProduct(@RequestBody Product Product) {
        return productService.createProduct(Product);
    }

    @PutMapping(value = "/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Observable<Product> updateProduct(@PathVariable String id, @RequestBody Product updatedProduct) {
        return productService.updateProduct(id, updatedProduct);
    }

    @GetMapping(value = "/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Maybe<Product> getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Observable<Void> deleteProductById(@PathVariable String id) {
        return productService.deleteProductById(id);
    }


    @PutMapping(value = "/deposit/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Observable<Product> deposit(@PathVariable String id, @RequestBody Product updatedProduct) {
        return productService.depositProduct(id, updatedProduct);
    }

    @PutMapping(value = "/withdrawal/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Observable<Product> withdrawal(@PathVariable String id, @RequestBody Product updatedProduct) {
        return productService.withdrawal(id, updatedProduct);
    }

    @PutMapping(value = "/paycredit/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Maybe<Product> paycredit(@PathVariable String id, @RequestBody Product updatedProduct) {
        return productService.paycredit(id, updatedProduct);
    }

    /*@PutMapping(value = "/paytotalcredit/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Maybe<Product> paytotalcredit(@PathVariable String id, @RequestBody Product updatedProduct) {
        return productService.paytotalcredit(id, updatedProduct);
    }*/

    @PutMapping(value = "/chargeconsumptions/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Maybe<Product> chargeconsumptions(@PathVariable String id, @RequestBody Product updatedProduct) {
        return productService.chargeconsumptions(id, updatedProduct);
    }

    @GetMapping(value = "/availablebalances/{idClient}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flowable<Product> availablebalances(@PathVariable String idClient) {
        return productService.availablebalances(idClient);
    }

}
