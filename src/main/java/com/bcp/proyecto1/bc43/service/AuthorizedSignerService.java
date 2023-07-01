package com.bcp.proyecto1.bc43.service;

import com.bcp.proyecto1.bc43.model.AuthorizedSigner;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

public interface AuthorizedSignerService {
    Flowable<AuthorizedSigner> getAllAuthorizedSigners();
    Maybe<AuthorizedSigner> createAuthorizedSigner(AuthorizedSigner AuthorizedSigner);
    Maybe<AuthorizedSigner> updateAuthorizedSigner(AuthorizedSigner updatedAuthorizedSigner);
    Maybe<AuthorizedSigner> getAuthorizedSignerById(String id);
    Maybe<AuthorizedSigner> deleteAuthorizedSignerById(String id);
}
