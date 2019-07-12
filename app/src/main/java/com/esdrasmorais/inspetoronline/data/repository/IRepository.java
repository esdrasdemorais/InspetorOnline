package com.esdrasmorais.inspetoronline.data.repository;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public interface IRepository<T> {
    public Boolean set(T object);
    public List<T> get();
    public Boolean update(T object, String id);
    public Boolean delete(T object);
    public T getById(Long id);
}