package com.bcp.proyecto1.bc43.serviceimpl;

import com.bcp.proyecto1.bc43.exceptions.ConflictException;
import com.bcp.proyecto1.bc43.exceptions.NotFoundException;
import com.bcp.proyecto1.bc43.model.AuthorizedSigner;
import com.bcp.proyecto1.bc43.repository.AuthorizedSignerRepository;
import com.bcp.proyecto1.bc43.service.AuthorizedSignerService;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizedSignerServiceImpl implements AuthorizedSignerService {
    private final AuthorizedSignerRepository authorizedSignerRepository;

    @Autowired
    public AuthorizedSignerServiceImpl(AuthorizedSignerRepository authorizedSignerRepository) {
        this.authorizedSignerRepository = authorizedSignerRepository;
    }

    @Override
    public Flowable<AuthorizedSigner> getAllAuthorizedSigners() {
        return Flowable.fromPublisher(this.authorizedSignerRepository.findAll());
    }

    @Override
    public Maybe<AuthorizedSigner> createAuthorizedSigner(AuthorizedSigner authorizedSigner) {
        return getAuthorizedSignerById(authorizedSigner.getId())
                .flatMap(
                        authorizedSignerEntity -> Maybe.error(
                                new ConflictException("AuthorizedSigner Code already exists : " + authorizedSigner.getId())
                        ),
                        (error) -> Maybe.error(error),
                        () -> Maybe.just(authorizedSigner)
                )
                .flatMapSingle(single-> saveAuthorizedSigner(single).toSingle());
    }

    private Maybe<AuthorizedSigner> saveAuthorizedSigner(AuthorizedSigner authorizedSigner){
        return Maybe.fromPublisher(authorizedSignerRepository.save(authorizedSigner));
    }

    @Override
    public Maybe<AuthorizedSigner> updateAuthorizedSigner(AuthorizedSigner updatedAuthorizedSigner) {
        return getAuthorizedSignerById(updatedAuthorizedSigner.getId())
                .flatMap(
                        existsEntity ->  saveAuthorizedSigner(existsEntity),
                        error -> Maybe.error(error),
                        () -> Maybe.just(updatedAuthorizedSigner)
                );
    }

    @Override
    public Maybe<AuthorizedSigner> getAuthorizedSignerById(String id) {
        return Maybe.fromPublisher(this.authorizedSignerRepository.findById(id));
    }

    @Override
    public Maybe<AuthorizedSigner> deleteAuthorizedSignerById(String id) {
        return getAuthorizedSignerById(id)
                .switchIfEmpty(Maybe.error(new NotFoundException("Non existent id: " + id)))
                .flatMap(productEntity -> {
                    return Maybe.fromPublisher(authorizedSignerRepository.deleteById(productEntity.getId()))
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
