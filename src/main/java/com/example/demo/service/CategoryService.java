package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Category;

/**
 * CategoryService介面 — 定義類別（Category）相關的業務邏輯操作。
 * 
 * 此介面負責處理類別基本CRUD與其他相關操作。如取得三層類別中的大中小類並取得所有子分類
 */
public interface CategoryService {
	List<Category> getAllCategories();
	
	Category getCategoryById(Long id);
	
	List<Category> getSubCategories(Long parentId);
	
	void saveCategory(Category category);
	
	void deleteCategory(Long id);
		
	List<Category> getThreeLevelCategories();
	
	List<Long> getAllChildCategoryIds(Long categoryId);
	
    List<Category> getAllLeafCategories();
}
