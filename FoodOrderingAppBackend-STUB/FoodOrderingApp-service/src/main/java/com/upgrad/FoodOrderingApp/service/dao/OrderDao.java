package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.UUID;

@Repository
public class OrderDao {
    @PersistenceContext
    private EntityManager entityManager;

    public OrderEntity saveOrder( OrderEntity orderEntity ){
        this.entityManager.persist(orderEntity);
        return orderEntity;
    }

    public CouponEntity getCouponByUuid(UUID couponUuid){
        try {
            //return (CouponEntity) this.entityManager.createNamedQuery("couponByCouponUuid", CouponEntity.class).setParameter("couponUuid", couponUuid).getSingleResult();
            return (CouponEntity) this.entityManager.createNamedQuery("getCouponUuid", CouponEntity.class).setParameter("couponUuid", couponUuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public AddressEntity getAddressByUuid(UUID addressUuid){
        try {
            return (AddressEntity) this.entityManager.createNamedQuery("addressByAddressUuid", AddressEntity.class).setParameter("uuid", addressUuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public PaymentEntity getPaymentByUuid(UUID paymentUuid){
        try {
            return (PaymentEntity) this.entityManager.createNamedQuery("paymentByPaymentUuid", PaymentEntity.class).setParameter("paymentUuid", paymentUuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public RestaurantEntity getRestaurantByUuid(UUID restaurantUuid){
        try {
            return (RestaurantEntity) this.entityManager.createNamedQuery("restaurantsById", RestaurantEntity.class).setParameter("restaurantId", restaurantUuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public ItemEntity getItemByUuid(UUID itemUuid){
        try {
            return (ItemEntity) this.entityManager.createNamedQuery("itemByItemUuid", ItemEntity.class).setParameter("itemUuid", itemUuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}