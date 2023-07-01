package com.bcp.proyecto1.bc43.controller;

import com.bcp.proyecto1.bc43.model.Client;
import com.bcp.proyecto1.bc43.service.ClientService;
import io.reactivex.rxjava3.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Observable<Client> getAllUsers() {
        return clientService.getAllClients();
    }

    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Observable<Client> createClient(@RequestBody Client client) {
        return clientService.createClient(client);
    }

    @PutMapping(value = "/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Observable<Client> updateClient(@PathVariable String id, @RequestBody Client updatedClient) {
        return clientService.updateClient(id, updatedClient);
    }

    @GetMapping(value = "/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Maybe<Client> getClientById(@PathVariable String id) {
        return clientService.getClientById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Observable<Void> deleteClientById(@PathVariable String id) {
        return clientService.deleteClientById(id);
    }
}
