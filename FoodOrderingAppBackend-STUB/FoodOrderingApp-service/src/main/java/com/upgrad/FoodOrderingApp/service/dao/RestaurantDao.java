package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<RestaurantEntity> getAllRestaurants() {
        try {
            return entityManager.createNamedQuery("allRestaurants", RestaurantEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<RestaurantEntity> getRestaurantsByName(String restaurantName) {
        try {
            return entityManager.createNamedQuery("restaurantsByName", RestaurantEntity.class).setParameter("restaurantName", restaurantName).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public CategoryEntity getCategoryById(String categoryId) {
        try {
            return entityManager.createNamedQuery("categoryById", CategoryEntity.class).setParameter("categoryId", categoryId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<RestaurantEntity> getRestaurantsByCategory(List<CategoryEntity> category) {
        try {
            return entityManager.createNamedQuery("restaurantsByCategory", RestaurantEntity.class).setParameter("category", category).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

}
