package com.bcp.proyecto1.bc43.controller;

import com.bcp.proyecto1.bc43.model.Holder;
import com.bcp.proyecto1.bc43.service.HolderService;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/holders")
public class HolderController {
    @Autowired
    private HolderService holderService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flowable<Holder> getAllUsers() {
        return holderService.getAllHolders();
    }

    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Maybe<Holder> createholder(@RequestBody Holder holder) {
        return holderService.createHolder(holder);
    }

    @PutMapping(value = "/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Maybe<Holder> updateholder(@RequestBody Holder updatedholder) {
        return holderService.updateHolder(updatedholder);
    }

    @GetMapping(value = "/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Maybe<Holder> getholderById(@PathVariable String id) {
        return holderService.getHolderById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Maybe<Holder> deleteholderById(@PathVariable String id) {
        return holderService.deleteHolderById(id);
    }

}
