package com.example.demo.dao;

import java.util.List;

public interface BaseDAO<T> {

    List<T> findAll();

    T findById(Long id);

    void save(T entity);

    void delete(Long id);
}
