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
/*
    public List<PaymentEntity> getPaymentMethods(PaymentEntity paymentEntity){
        //entityManager.persist(paymentEntity);
        //return paymentEntity;
        try{
            return entityManager.createNamedQuery("paymentEntity.findAll",PaymentEntity.class).getResultList();
        } catch (NoResultException nre){
            return null;
        }
    }

 */
public List<PaymentEntity> getPaymentMethods(){
    //entityManager.persist(paymentEntity);
    //return paymentEntity;
    try{
        //return entityManager.createNamedQuery("paymentMethods",PaymentEntity.class).getResultList();
        System.out.println("In Dao get");
        List<PaymentEntity> result= entityManager.createNamedQuery("paymentMethods",PaymentEntity.class).getResultList();

        System.out.println("result"+result);
        return  result;
    } catch (NoResultException nre){
        return null;
    }
}
}
