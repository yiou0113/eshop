package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.CategoryDAO;
import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryDAO categoryDAO;
	
	@Override
	public List<Category> getAllCategories(){
		return categoryDAO.findAll();
	}
	@Override
	public Category getCategoryById(Long id) {
		return categoryDAO.findById(id);
	}
	
	@Override
	public List<Category> getSubCategories(Long parentId){
		List<Category> children = categoryDAO.findByParentId(parentId);
        return children != null ? children : List.of();	
    }
	@Override
	public void saveCategory(Category category) {
		categoryDAO.save(category);
	}
	@Override
	public void deleteCategory(Long id) {
		categoryDAO.delete(id);
	}
	
    @Override
    public List<Category> getThreeLevelCategories() {
        // Step 1：查所有大分類（parent = null）
        List<Category> topCategories = categoryDAO.findByParentId(null);

        for (Category top : topCategories) {
            // Step 2：查中分類
            List<Category> middleCategories = categoryDAO.findByParentId(top.getId());
            top.setChildren(middleCategories);

            for (Category mid : middleCategories) {
                // Step 3：查小分類
                List<Category> smallCategories = categoryDAO.findByParentId(mid.getId());
                mid.setChildren(smallCategories);
            }
        }
        return topCategories;
    }
    
    @Override
    public List<Long> getAllChildCategoryIds(Long categoryId) {
        List<Long> result = new ArrayList<>();
        result.add(categoryId); // 包含自己
        fetchChildren(categoryId, result);
        return result;
    }

    private void fetchChildren(Long parentId, List<Long> result) {
        List<Category> children = categoryDAO.findByParentId(parentId);
        for (Category child : children) {
            result.add(child.getId());
            fetchChildren(child.getId(), result); // 遞迴找子孫
        }
    }

}
