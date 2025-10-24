package com.example.demo.dao;

import com.example.demo.model.Product;

import java.util.List;
/**
 * ProductDAO 介面 — 定義商品（Product）資料存取操作（Data Access Object）。
 *
 * 此介面負責與資料庫互動，提供商品的 CRUD 操作及分頁查詢。
 */
public interface ProductDAO extends BaseDAO<Product> {    
    List<Product> findPaginated(int page, int size);
    
}
