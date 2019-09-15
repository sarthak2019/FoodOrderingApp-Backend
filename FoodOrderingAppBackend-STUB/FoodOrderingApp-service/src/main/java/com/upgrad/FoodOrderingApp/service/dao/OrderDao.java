package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class OrderDao {
    @PersistenceContext
    private EntityManager entityManager;

    public OrderEntity saveOrder( OrderEntity orderEntity ){
        this.entityManager.persist(orderEntity);
        return orderEntity;
    }

}