package com.bcp.proyecto1.bc43.repository;

import com.bcp.proyecto1.bc43.model.Client;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
public interface ClientRepository extends ReactiveMongoRepository<Client, String> {

}