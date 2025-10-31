package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.Category;

/**
 * CategoryDAO 介面
 *
 * <p>此介面繼承 {@link BaseDAO}，負責定義與「商品分類（Category）」相關的
 * 資料存取操作（Data Access Operations）。</p>
 *
 * <p>除基本的 CRUD 功能外，另提供根據父分類 ID 查詢子分類的方法。</p>
 *
 * <p>主要功能包含：</p>
 * <ul>
 *   <li>查詢所有分類</li>
 *   <li>依 ID 查詢單一分類</li>
 *   <li>新增或更新分類資料</li>
 *   <li>刪除分類</li>
 *   <li>依父分類 ID 查詢其子分類</li>
 * </ul>
 */
public interface CategoryDAO extends BaseDAO<Category> {
	List<Category> findByParentId(Long parentId);
}
