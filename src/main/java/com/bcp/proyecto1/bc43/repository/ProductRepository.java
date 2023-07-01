package com.bcp.proyecto1.bc43.repository;

import com.bcp.proyecto1.bc43.model.Product;
import com.bcp.proyecto1.bc43.model.ProductType;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Document(collection = "products")
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

    @Query("{'idClient': ?0, 'type': { $in: ?1 }}")
    Maybe<Product> existsByClientIdAndTypeContains(String idClient, ProductType[] types);

    @Query("{'id': ?0, 'type': { $in: ?1 }}")
    Maybe<Product> existsByIdAndTypeContains(String idProduct, ProductType[] types);


    @Query("{'idClient': ?0, 'type': { $in: ?1 }}")
    Flowable<Product> existsByClientIdAndTypeContainsAll(String idClient, ProductType[] types);
}