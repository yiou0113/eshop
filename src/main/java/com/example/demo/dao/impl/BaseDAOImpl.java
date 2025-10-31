package com.example.demo.dao.impl;

import com.example.demo.dao.BaseDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * BaseDAOImpl 抽象類別
 *
 * <p>此類別實作 {@link BaseDAO}，提供所有 DAO 實作類別的共用 CRUD 功能，
 * 並透過 Hibernate 的 {@link SessionFactory} 操作資料庫。</p>
 *
 * <p>特性：</p>
 * <ul>
 *   <li>支援泛型實體類別 T</li>
 *   <li>提供共用的 findAll、findById、save、delete 方法</li>
 *   <li>可被具體 DAO 實作類別繼承，減少重複程式碼</li>
 *   <li>支援 Spring 的 Transaction 管理（{@link Transactional}）</li>
 * </ul>
 *
 * @param <T> DAO 對應的實體類別型別
 */
@Transactional
public abstract class BaseDAOImpl<T> implements BaseDAO<T> {

    @Autowired
    protected SessionFactory sessionFactory;

    /**
     * 實體類別型別，用於泛型 CRUD 操作。
     */
    private final Class<T> entityClass;

    /**
     * 建構子，取得泛型實體類別的 Class 物件。
     * <p>透過反射機制自動推斷泛型型別 T。</p>
     */
    @SuppressWarnings("unchecked")
    public BaseDAOImpl() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    /**
     * 取得當前 Hibernate Session。
     *
     * @return 當前 Session 物件
     */
    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 查詢所有實體資料。
     *
     * @return 所有 T 實體的清單；若無資料則回傳空集合
     */
    @Override
    public List<T> findAll() {
        return getCurrentSession()
                .createQuery("FROM " + entityClass.getSimpleName(), entityClass)
                .list();
    }

    /**
     * 根據主鍵 ID 查詢單一實體。
     *
     * @param id 實體的唯一識別碼
     * @return 對應的 T 物件；若不存在則回傳 {@code null}
     */
    @Override
    public T findById(Long id) {
        return getCurrentSession().get(entityClass, id);
    }

    /**
     * 儲存或更新實體資料。
     *
     * <p>若該實體尚未存在，則進行新增；若已存在，則進行更新。</p>
     *
     * @param entity 要儲存或更新的實體物件
     */
    @Override
    public void save(T entity) {
        getCurrentSession().saveOrUpdate(entity);
    }

    /**
     * 根據主鍵 ID 刪除實體資料。
     *
     * @param id 要刪除的實體 ID
     */
    @Override
    public void delete(Long id) {
        T entity = findById(id);
        if (entity != null) {
            getCurrentSession().delete(entity);
        }
    }
}
