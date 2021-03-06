package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CouponDao {
    @PersistenceContext
    private EntityManager entityManager;

    public CouponEntity getCouponName(final String couponName) {
        try {

            CouponEntity result = entityManager.createNamedQuery("couponByName", CouponEntity.class).setParameter("coupon_name", couponName).getSingleResult();
            return result;
        } catch (NoResultException nre) {
            return null;
        }
    }

    public CouponEntity getCouponById(final String uuid) {
        try {
            CouponEntity result = entityManager.createNamedQuery("couponById", CouponEntity.class).setParameter("uuid", uuid).getSingleResult();
            return result;
        } catch (NoResultException nre) {
            return null;
        }
    }


}