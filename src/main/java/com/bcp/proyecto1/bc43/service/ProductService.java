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
    Observable<Product> createProduct(Product product);
    Single<Product> validateClientAndProduct(Client client, Product product);
    Observable<Product> saveProduct(Product product);
    Observable<Product> updateProduct(String id, Product updatedProduct);
    Observable<Product> getProductById(String id);
    Observable deleteProductById(String id);
}
