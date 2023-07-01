package com.bcp.proyecto1.bc43.serviceimpl;

import com.bcp.proyecto1.bc43.exceptions.ConflictException;
import com.bcp.proyecto1.bc43.exceptions.NotFoundException;
import com.bcp.proyecto1.bc43.model.Movement;
import com.bcp.proyecto1.bc43.repository.MovementRepository;
import com.bcp.proyecto1.bc43.service.MovementService;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovementServiceImpl implements MovementService {
    private final MovementRepository movementRepository;

    @Autowired
    public MovementServiceImpl(MovementRepository movementRepository) {
        this.movementRepository = movementRepository;
    }

    @Override
    public Flowable<Movement> getAllMovements() {
        return Flowable.fromPublisher(this.movementRepository.findAll());
    }

    @Override
    public Maybe<Movement> createMovement(Movement movement) {
        return getMovementById(movement.getId())
                .flatMap(
                        movementEntity -> Maybe.error(
                                new ConflictException("Movement Code already exists : " + movement.getId())
                        ),
                        (error) -> Maybe.error(error),
                        () -> Maybe.just(movement)
                )
                .flatMapSingle(single-> saveMovement(single).toSingle());
    }

    private Maybe<Movement> saveMovement(Movement movement){
        return Maybe.fromPublisher(movementRepository.save(movement));
    }

    @Override
    public Maybe<Movement> updateMovement(Movement updatedMovement) {
        return getMovementById(updatedMovement.getId())
                .flatMap(
                        existsEntity ->  saveMovement(existsEntity),
                        error -> Maybe.error(error),
                        () -> Maybe.just(updatedMovement)
                );
    }

    @Override
    public Maybe<Movement> getMovementById(String id) {
        return Maybe.fromPublisher(this.movementRepository.findById(id));
    }

    @Override
    public Maybe<Movement> deleteMovementById(String id) {
        return getMovementById(id)
                .switchIfEmpty(Maybe.error(new NotFoundException("Non existent id: " + id)))
                .flatMap(entity -> {
                    return Maybe.fromPublisher(movementRepository.deleteById(entity.getId()))
                            .flatMap(
                                    empty -> {
                                        return Maybe.empty();
                                    },
                                    error -> Maybe.error(error),
                                    () -> Maybe.just(entity)
                            );
                });
    }
}
