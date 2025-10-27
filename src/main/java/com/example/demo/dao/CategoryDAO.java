package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.Category;

public interface CategoryDAO extends BaseDAO<Category> {
	List<Category> findByParentId(Long parentId);
}
