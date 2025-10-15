package com.example.demo.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.OrderDAO;
import com.example.demo.model.Order;
import com.example.demo.model.User;

@Repository
public class OrderDAOImpl implements OrderDAO {
	@Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void save(Order order) {
        getSession().saveOrUpdate(order);
    }

    public Order findById(Long id) {
        return getSession().get(Order.class, id);
    }

    public List<Order> findAll() {
        return getSession().createQuery("from Order", Order.class).list();
    }
}
