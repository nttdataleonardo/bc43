package com.bcp.proyecto1.bc43.serviceimpl;

import com.bcp.proyecto1.bc43.exceptions.ConflictException;
import com.bcp.proyecto1.bc43.exceptions.NotFoundException;
import com.bcp.proyecto1.bc43.model.Holder;
import com.bcp.proyecto1.bc43.repository.HolderRepository;
import com.bcp.proyecto1.bc43.service.HolderService;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HolderServiceImpl implements HolderService {
    private final HolderRepository holderRepository;

    @Autowired
    public HolderServiceImpl(HolderRepository holderRepository) {
        this.holderRepository = holderRepository;
    }

    @Override
    public Flowable<Holder> getAllHolders() {
        return Flowable.fromPublisher(this.holderRepository.findAll());
    }

    @Override
    public Maybe<Holder> createHolder(Holder holder) {
        return getHolderById(holder.getId())
                .flatMap(
                        holderEntity -> Maybe.error(
                                new ConflictException("Holder Code already exists : " + holder.getId())
                        ),
                        (error) -> Maybe.error(error),
                        () -> Maybe.just(holder)
                )
                .flatMapSingle(single-> saveHolder(single).toSingle());
    }

    private Maybe<Holder> saveHolder(Holder holder){
        return Maybe.fromPublisher(holderRepository.save(holder));
    }

    @Override
    public Maybe<Holder> updateHolder(Holder updatedHolder) {
        return getHolderById(updatedHolder.getId())
                .flatMap(
                        existsEntity ->  saveHolder(existsEntity),
                        error -> Maybe.error(error),
                        () -> Maybe.just(updatedHolder)
                );
    }

    @Override
    public Maybe<Holder> getHolderById(String id) {
        return Maybe.fromPublisher(this.holderRepository.findById(id));
    }

    @Override
    public Maybe<Holder> deleteHolderById(String id) {
        return getHolderById(id)
                .switchIfEmpty(Maybe.error(new NotFoundException("Non existent id: " + id)))
                .flatMap(productEntity -> {
                    return Maybe.fromPublisher(holderRepository.deleteById(productEntity.getId()))
                            .flatMap(
                                    empty -> {
                                        return Maybe.empty();
                                    },
                                    error -> Maybe.error(error),
                                    () -> Maybe.just(productEntity)
                            );
                });
    }
}
