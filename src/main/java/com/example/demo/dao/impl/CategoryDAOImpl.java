package com.example.demo.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.dao.CategoryDAO;
import com.example.demo.model.Category;

/**
 * CategoryDAO 的實作類別
 *
 * <p>此類別繼承 {@link BaseDAOImpl} 並實作 {@link CategoryDAO} 介面，
 * 負責與資料庫互動，提供類別（{@link Category}）相關的 CRUD 與查詢操作。</p>
 *
 * <p>主要功能：</p>
 * <ul>
 *   <li>根據父類別 ID 查詢子分類</li>
 *   <li>查詢所有大分類（parent 為 null）</li>
 *   <li>提供共用 CRUD 功能由 {@link BaseDAOImpl} 實作</li>
 * </ul>
 *
 */
@Repository
public class CategoryDAOImpl extends BaseDAOImpl<Category> implements CategoryDAO {

    /**
     * 根據父分類 ID 查詢子分類。
     *
     * <p>若 parentId 為 null，則查詢所有大分類（parent IS NULL）。
     * 否則查詢指定父分類下的子分類。</p>
     *
     * @param parentId 父分類 ID；若為 {@code null} 則查大分類
     * @return 子分類清單；若無對應分類則回傳空集合
     */
    @Override
    public List<Category> findByParentId(Long parentId) {
        String hql;
        if (parentId == null) {
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
