package com.bcp.proyecto1.bc43.service;

import com.bcp.proyecto1.bc43.model.Client;
import com.bcp.proyecto1.bc43.model.ClientType;
import com.bcp.proyecto1.bc43.model.Product;
import com.bcp.proyecto1.bc43.model.ProductType;
import com.bcp.proyecto1.bc43.repository.ProductRepository;
import io.reactivex.rxjava3.core.*;
import org.springframework.beans.factory.annotation.Autowired;
public interface ProductService {
    Observable<Product> getAllProducts();
    Maybe<Product> createProduct(Product product);
    Maybe<Product> validateClientAndProduct(Client client, Product product);
    Maybe<Product> saveProduct(Product product);
    Observable<Product> updateProduct(String id, Product updatedProduct);
    Maybe<Product> getProductById(String id);
    Observable deleteProductById(String id);

    Observable<Product> depositProduct(String id, Product updatedProduct);
    Observable<Product> withdrawal(String id, Product updatedProduct);
    Maybe<Product> paycredit(String id, Product updatedProduct);
    //Maybe<Product> paytotalcredit(String id, Product product);
    Maybe<Product> chargeconsumptions(String id, Product product);

    Flowable<Product> availablebalances(String idClient);
}
