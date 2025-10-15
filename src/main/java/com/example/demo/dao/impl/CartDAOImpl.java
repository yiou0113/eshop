package com.example.demo.dao.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.CartDAO;
import com.example.demo.model.Cart;
import com.example.demo.model.User;

@Repository
public class CartDAOImpl implements CartDAO {
	@Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Optional<Cart> findByUserId(Long userId) {
        try {
            Cart cart = getCurrentSession()
                    .createQuery("FROM Cart c WHERE c.user.id = :userId", Cart.class)
                    .setParameter("userId", userId)
                    .uniqueResult();
            return Optional.ofNullable(cart);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(Cart cart) {
        getCurrentSession().saveOrUpdate(cart);
    }

    @Override
    public void delete(Cart cart) {
        getCurrentSession().delete(cart);
    }
    
}
