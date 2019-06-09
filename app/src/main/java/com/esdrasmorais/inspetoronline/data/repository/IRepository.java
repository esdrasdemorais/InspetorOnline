package com.esdrasmorais.inspetoronline.data.repository;

public interface IRepository<T> {
    public Boolean set(T object);
}