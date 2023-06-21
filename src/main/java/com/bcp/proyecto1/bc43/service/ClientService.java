package com.bcp.proyecto1.bc43.service;

import com.bcp.proyecto1.bc43.model.Client;
import com.bcp.proyecto1.bc43.repository.ClientRepository;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository ClientRepository) {
        this.clientRepository = ClientRepository;
    }

    public Flowable<Client> getAllClients() {
        return Flowable.fromPublisher(clientRepository.findAll());
    }

    public Completable createClient(Client client) {
        return Completable.create(emitter -> {
            clientRepository.save(client)
                    .doOnSuccess(savedClient -> emitter.onComplete())
                    .doOnError(error -> emitter.onError(error))
                    .subscribe();
        });
    }

    public Completable updateClient(String id, Client updatedClient) {
        return Maybe.fromPublisher(clientRepository.findById(id))
                .flatMapCompletable(existingClient -> {
                    existingClient.setName(updatedClient.getName());
                    existingClient.setType(updatedClient.getType());
                    return Completable.fromPublisher(clientRepository.save(existingClient));
                });
    }

    public Flowable<Client> getClientById(String id) {
        return Flowable.fromPublisher(clientRepository.findById(id));
    }

    public Completable deleteClientById(String id) {
        return Completable.fromPublisher(clientRepository.deleteById(id));
    }
}
