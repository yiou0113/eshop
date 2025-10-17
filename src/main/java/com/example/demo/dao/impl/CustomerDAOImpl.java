package com.example.demo.dao.impl;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.CustomerDAO;
import com.example.demo.model.Customer;
import com.example.demo.model.User;
@Repository
public class CustomerDAOImpl implements CustomerDAO {
	@Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    @Override
    public void save(Customer customer) {
        getCurrentSession().saveOrUpdate(customer);
    }
    @Override
    public Customer findById(Long id) {
        return getCurrentSession().get(Customer.class, id);
    }
    @Override
    public Customer findByUserId(Long userId) {
        try {
            return getCurrentSession()
                    .createQuery("FROM Customer c WHERE c.user.id = :userId", Customer.class)
                    .setParameter("userId", userId)
                    .uniqueResult(); // 找不到會回傳 null
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 發生例外也回傳 null
        }
    }

}
