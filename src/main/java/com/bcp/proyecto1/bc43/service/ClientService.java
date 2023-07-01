package com.bcp.proyecto1.bc43.service;

import com.bcp.proyecto1.bc43.model.Client;
import io.reactivex.rxjava3.core.*;

public interface ClientService {

    Observable<Client> getAllClients();
    Observable<Client> createClient(Client client);
    Observable<Client> updateClient(String id, Client updatedClient);
    Maybe<Client> getClientById(String id);
    Observable<Void> deleteClientById(String id);
}
