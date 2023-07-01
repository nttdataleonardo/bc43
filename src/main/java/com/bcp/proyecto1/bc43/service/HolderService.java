package com.bcp.proyecto1.bc43.service;

import com.bcp.proyecto1.bc43.model.Client;
import com.bcp.proyecto1.bc43.model.Holder;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

public interface HolderService {
    Flowable<Holder> getAllHolders();
    Maybe<Holder> createHolder(Holder Holder);
    Maybe<Holder> updateHolder(Holder updatedHolder);
    Maybe<Holder> getHolderById(String id);
    Maybe<Holder> deleteHolderById(String id);
}
