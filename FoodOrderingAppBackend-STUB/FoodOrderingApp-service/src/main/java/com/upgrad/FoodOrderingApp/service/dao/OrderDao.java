package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

@Repository
public class OrderDao {
    @PersistenceContext
    private EntityManager entityManager;

    public OrderEntity saveOrder( OrderEntity orderEntity ){
        this.entityManager.persist(orderEntity);
        return orderEntity;
    }

    public List<OrderEntity> getOrdersByCustomerId( final UUID customerId){
        //this.entityManager.;
        System.out.println("In Dao get");
        //List<OrderEntity> result = entityManager.createNamedQuery("getOrdersByCustomerId", OrderEntity.class).setParameter("CUSTOMER_ID",customerId).getResultList();
        List<OrderEntity> result = entityManager.createNamedQuery("getOrdersByCustomerId", OrderEntity.class).setParameter("CUSTOMER_ID",customerId).getResultList();

        System.out.println("result" + result);
        return result;
    }

}