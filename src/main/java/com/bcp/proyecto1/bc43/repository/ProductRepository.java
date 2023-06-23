package com.bcp.proyecto1.bc43.repository;

import com.bcp.proyecto1.bc43.model.Product;
import com.bcp.proyecto1.bc43.model.ProductType;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
    @Query("{'idClient': ?0, 'type': { $all: ?1 }}")
    Single<Integer> countByClientIdAndTypeContains(String idClient, ProductType[] types);
}