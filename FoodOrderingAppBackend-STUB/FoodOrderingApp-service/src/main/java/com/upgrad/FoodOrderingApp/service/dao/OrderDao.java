package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
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
        entityManager.persist(orderEntity);
        return orderEntity;
    }

    public List<OrderEntity> getOrdersByCustomerId(final CustomerEntity customer){
        List<OrderEntity> result = entityManager.createNamedQuery("getOrdersByCustomerId", OrderEntity.class).setParameter("customer",customer).getResultList();
        return result;
    }

    public OrderItemEntity saveOrderItem(OrderItemEntity saveOrderItem ){
        entityManager.persist(saveOrderItem);
        return saveOrderItem;
    }

}