package com.example.demo.dao;

import java.util.List;

/**
 * BaseDAO 介面
 *
 * <p>此介面定義了所有資料存取物件（DAO, Data Access Object）的共通基本操作。</p>
 * <p>透過泛型 <code>&lt;T&gt;</code>，可讓不同實體類別（如 User、Product、Category 等）
 * 共用相同的資料存取方法。</p>
 *
 * <p>主要功能包含：</p>
 * <ul>
 *   <li>查詢所有資料</li>
 *   <li>依 ID 查詢單一資料</li>
 *   <li>新增或更新資料</li>
 *   <li>依 ID 刪除資料</li>
 * </ul>
 *
 * @param <T> 資料表對應的實體類別型別（Entity Class）
 */
public interface BaseDAO<T> {

    List<T> findAll();

    T findById(Long id);

    void save(T entity);

    void delete(Long id);
}
