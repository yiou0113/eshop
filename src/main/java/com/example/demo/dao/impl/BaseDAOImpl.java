package com.example.demo.dao.impl;

import com.example.demo.dao.BaseDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.util.List;

@Transactional
public abstract class BaseDAOImpl<T> implements BaseDAO<T> {

    @Autowired
    protected SessionFactory sessionFactory;

    private final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public BaseDAOImpl() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<T> findAll() {
        return getCurrentSession()
                .createQuery("FROM " + entityClass.getSimpleName(), entityClass)
                .list();
    }

    @Override
    public T findById(Long id) {
        return getCurrentSession().get(entityClass, id);
    }

    @Override
    public void save(T entity) {
        getCurrentSession().saveOrUpdate(entity);
    }

    @Override
    public void delete(Long id) {
        T entity = findById(id);
        if (entity != null) {
            getCurrentSession().delete(entity);
        }
    }
}
