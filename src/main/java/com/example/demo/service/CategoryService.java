package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Category;

public interface CategoryService {
	List<Category> getAllCategories();
	
	Category getCategoryById(Long id);
	
	List<Category> getSubCategories(Long parentId);
	
	void saveCategory(Category category);
	
	void deleteCategory(Long id);
		
	List<Category> getThreeLevelCategories();
}
