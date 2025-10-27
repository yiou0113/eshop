package com.example.demo.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.dao.CategoryDAO;
import com.example.demo.model.Category;

@Repository
public class CategoryDAOImpl extends BaseDAOImpl<Category> implements CategoryDAO {
	@Override
	public List<Category> findByParentId(Long parentId){
	    String hql;
	    if(parentId == null) {
	        // 查大分類
	        hql = "FROM Category c WHERE c.parent IS NULL";
	        return getCurrentSession()
	                .createQuery(hql, Category.class)
	                .list();
	    } else {
	        // 查子分類
	        hql = "FROM Category c WHERE c.parent.id = :parentId";
	        return getCurrentSession()
	                .createQuery(hql, Category.class)
	                .setParameter("parentId", parentId)
	                .list();
	    }
	}

}
