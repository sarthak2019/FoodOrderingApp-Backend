package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class ItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    public ItemEntity getItemsById(String uuid) {
        try {
            ItemEntity result = entityManager.createNamedQuery("itemById", ItemEntity.class).setParameter("uuid", uuid).getSingleResult();
            return result;
        } catch (NoResultException nre) {
            return null;
        }
    }

}
