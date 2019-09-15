package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CouponDao {
    @PersistenceContext
    private EntityManager entityManager;

    public CouponEntity getCouponName(final String couponName){
        //entityManager.persist(paymentEntity);
        //return paymentEntity;
        try{
            //return entityManager.createNamedQuery("paymentMethods",PaymentEntity.class).getResultList();
            System.out.println("In coupon Dao get");
            CouponEntity result= entityManager.createNamedQuery("couponByName", CouponEntity.class).setParameter("coupon_name",couponName).getSingleResult();

            System.out.println("coupon"+result);
            return  result;
        } catch (NoResultException nre){
            return null;
        }
    }
}