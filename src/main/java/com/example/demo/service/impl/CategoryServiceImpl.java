package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.CategoryDAO;
import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;

/**
 * CategoryService 的實作類別
 *
 * 此類別主要負責處理類別的業務邏輯： 
 * - 取得所有分類
 * - 透過ID取得類別 
 * - 取得所有子分類
 * - 儲存分類
 * - 刪除分類
 * - 取得三層分類
 * - 取得所有子分類ID
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryDAO categoryDAO;
	
	/**
     * 取得所有分類。
     *
     * @return 所有分類的清單
     */
	@Override
	public List<Category> getAllCategories(){
		return categoryDAO.findAll();
	}
	/**
     * 根據分類 ID 查詢分類。
     *
     * @param id 分類的唯一識別碼
     * @return 對應的 Category 物件；若不存在則回傳 null
     */
	@Override
	public Category getCategoryById(Long id) {
		return categoryDAO.findById(id);
	}
	/**
     * 取得指定分類的所有子分類。
     *
     * @param parentId 父分類 ID
     * @return 子分類清單；若無子分類則回傳空集合
     */
	@Override
	public List<Category> getSubCategories(Long parentId){
		List<Category> children = categoryDAO.findByParentId(parentId);
        return children != null ? children : List.of();	
    }
	/**
     * 儲存或更新分類資料。
     *
     * @param category 要儲存的分類物件
     */
	@Override
	public void saveCategory(Category category) {
		categoryDAO.save(category);
	}
	/**
     * 根據分類 ID 刪除分類。
     *
     * @param id 要刪除的分類 ID
     */
	@Override
	public void deleteCategory(Long id) {
		categoryDAO.delete(id);
	}
	/**
     * 取得三層分類（大分類 → 中分類 → 小分類）結構。
     *
     * 此方法會先查詢所有大分類（parentId 為 null），
     * 再遞迴查出中分類與小分類，建立階層樹狀結構。
     *
     * @return 三層分類的清單（包含巢狀子分類）
     */
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
    /**
     * 取得指定分類及其所有子分類的 ID 清單（包含自己）。
     *
     * @param categoryId 目標分類的 ID
     * @return 該分類及其所有子孫分類 ID 的集合
     */
    @Override
    public List<Long> getAllChildCategoryIds(Long categoryId) {
        List<Long> result = new ArrayList<>();
        result.add(categoryId); // 包含自己
        fetchChildren(categoryId, result);
        return result;
    }
    /**
     * 遞迴取得子分類的 ID，並加入結果清單。
     *
     * @param parentId 父分類 ID
     * @param result   存放結果的清單
     */
    private void fetchChildren(Long parentId, List<Long> result) {
        List<Category> children = categoryDAO.findByParentId(parentId);
        for (Category child : children) {
            result.add(child.getId());
            fetchChildren(child.getId(), result);
        }
    }
    @Override
    public List<Category> getAllLeafCategories() {
        return categoryDAO.findAllLeafCategories();
    }
}
