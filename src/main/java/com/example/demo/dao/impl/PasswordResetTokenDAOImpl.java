package com.example.demo.dao.impl;

import java.time.LocalDateTime;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.example.demo.dao.PasswordResetTokenDAO;
import com.example.demo.model.PasswordResetToken;

/**
 * PasswordResetTokenDAO 的實作類別
 *
 * <p>此類別繼承 {@link BaseDAOImpl} 並實作 {@link PasswordResetTokenDAO}，
 * 負責與資料庫互動，提供密碼重置 Token（{@link PasswordResetToken}）的 CRUD 與查詢操作。</p>
 *
 * <p>主要功能：</p>
 * <ul>
 *   <li>依 token 查詢對應的 PasswordResetToken</li>
 *   <li>依使用者 ID 刪除 Token</li>
 *   <li>刪除過期 Token</li>
 *   <li>提供基本 CRUD 功能由 {@link BaseDAOImpl} 實作</li>
 * </ul>
 */
@Repository
public class PasswordResetTokenDAOImpl extends BaseDAOImpl<PasswordResetToken> implements PasswordResetTokenDAO {
	 /**
     * 根據 token 查詢對應的 PasswordResetToken。
     *
     * <p>若查詢不到結果會回傳 {@code null}。</p>
     *
     * @param tokenHash 密碼重置 Token 字串
     * @return 對應的 {@link PasswordResetToken}，若查無資料則回傳 {@code null}
     */
	@Override
	public PasswordResetToken findByToken(String tokenHash) {
	    try {
	        return getCurrentSession()
	                .createQuery("FROM PasswordResetToken t WHERE t.token = :tokenHash", PasswordResetToken.class)
	                .setParameter("tokenHash", tokenHash)
	                .getSingleResult();
	    } catch (NoResultException e) {
	        return null;
	    }
	}
	 /**
     * 根據使用者 ID 刪除其所有 PasswordResetToken。
     *
     * @param id 使用者的唯一識別碼
     */
	@Override
	public void  deleteByUserId(Long id) {
		 getCurrentSession()
	        .createQuery("DELETE FROM PasswordResetToken t WHERE t.user.id = :userId")
	        .setParameter("userId", id)
	        .executeUpdate();
	}
	/**
     * 刪除所有已過期的 PasswordResetToken。
     *
     * @param now 當前時間，用於判斷哪些 Token 已過期
     */
	@Override
	public void deleteByExpiryDateBefore(LocalDateTime now) {
		getCurrentSession().createQuery("DELETE FROM PasswordResetToken t WHERE t.expiryDate < :now")
				.setParameter("now", now).executeUpdate();
	}

}