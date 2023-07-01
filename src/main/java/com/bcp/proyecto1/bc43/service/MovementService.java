package com.bcp.proyecto1.bc43.service;

import com.bcp.proyecto1.bc43.model.Movement;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

public interface MovementService {
    Flowable<Movement> getAllMovements();
    Maybe<Movement> createMovement(Movement Movement);
    Maybe<Movement> updateMovement(Movement updatedMovement);
    Maybe<Movement> getMovementById(String id);
    Maybe<Movement> deleteMovementById(String id);
}
