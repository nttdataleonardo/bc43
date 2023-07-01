package com.bcp.proyecto1.bc43.controller;

import com.bcp.proyecto1.bc43.model.Movement;
import com.bcp.proyecto1.bc43.service.MovementService;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movements")
public class MovementController {
    @Autowired
    private MovementService movementService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flowable<Movement> getAllUsers() {
        return movementService.getAllMovements();
    }

    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Maybe<Movement> createmovement(@RequestBody Movement movement) {
        return movementService.createMovement(movement);
    }

    @PutMapping(value = "/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Maybe<Movement> updatemovement(@RequestBody Movement updatedmovement) {
        return movementService.updateMovement(updatedmovement);
    }

    @GetMapping(value = "/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Maybe<Movement> getmovementById(@PathVariable String id) {
        return movementService.getMovementById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Maybe<Movement> deletemovementById(@PathVariable String id) {
        return movementService.deleteMovementById(id);
    }

}
