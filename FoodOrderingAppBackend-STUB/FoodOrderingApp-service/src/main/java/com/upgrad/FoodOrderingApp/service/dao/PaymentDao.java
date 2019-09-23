package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PaymentDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<PaymentEntity> getPaymentMethods() {
        try {
            System.out.println("In Dao get");
            List<PaymentEntity> result = entityManager.createNamedQuery("paymentMethods", PaymentEntity.class).getResultList();
            return result;
        } catch (NoResultException nre) {
            return null;
        }
    }

    public PaymentEntity getPaymentByUUID(String uuid) {
        try {
            System.out.println("In Dao get");
            PaymentEntity result = entityManager.createNamedQuery("paymentById", PaymentEntity.class).setParameter("uuid", uuid).getSingleResult();

            return result;
        } catch (NoResultException nre) {
            return null;
        }
    }
}