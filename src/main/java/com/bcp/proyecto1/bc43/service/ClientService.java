package com.bcp.proyecto1.bc43.service;

import com.bcp.proyecto1.bc43.model.Client;
import com.bcp.proyecto1.bc43.repository.ClientRepository;
import io.reactivex.rxjava3.core.*;
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

    public Observable<Client> getAllClients() {
        return Observable.fromPublisher(clientRepository.findAll()).doOnNext(System.out::println);
    }

    public Observable<Client> createClient(Client client) {
        return Observable.create(emitter -> {
            clientRepository.save(client)
                    .doOnSuccess(savedClient -> emitter.onComplete())
                    .doOnError(error -> emitter.onError(error))
                    .subscribe();
        });
    }

    public Observable<Client> updateClient(String id, Client updatedClient) {
        return Observable.create(emitter -> {
            clientRepository.findById(id)
                    .subscribe(existingClient -> {
                        existingClient.setName(updatedClient.getName());
                        existingClient.setType(updatedClient.getType());
                        clientRepository.save(existingClient)
                                .subscribe(savedClient -> {
                                    emitter.onNext(savedClient);
                                    emitter.onComplete();
                                }, error -> emitter.onError(error));
                    }, error -> emitter.onError(error));
        });
    }


    public Observable<Client> getClientById(String id) {
        return Observable.fromPublisher(clientRepository.findById(id));
    }

    /*public Completable deleteClientById(String id) {
        return Completable.fromPublisher(clientRepository.deleteById(id));
    }*/

    public Observable<Void> deleteClientById(String id) {
        return Observable.fromPublisher(clientRepository.deleteById(id))
                .doOnComplete(() -> System.out.println("Cliente eliminado con éxito"))
                .doOnError(error -> System.out.println("Error al eliminar el cliente: " + error.getMessage()));
    }
}
