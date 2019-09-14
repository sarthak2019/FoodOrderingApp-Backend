package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantDao {

    @PersistenceContext
    private EntityManager entityManager;

    /* The below method fetches a list of all the restaurants present in the database. */
    public List<RestaurantEntity> getAllRestaurants() {
        try {
            return entityManager.createNamedQuery("allRestaurants", RestaurantEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /* The below method fetches the restaurant based on the restaurantName from the database. */
    public List<RestaurantEntity> getRestaurantsByName(String restaurantName) {
        try {
            return entityManager.createNamedQuery("restaurantsByName", RestaurantEntity.class).setParameter("restaurantName", restaurantName.toLowerCase()).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /* The below method fetches the restaurant having a particular UUID from the database. */
    public RestaurantEntity getRestaurantsById(String restaurantId) {
        try {
            return entityManager.createNamedQuery("restaurantsById", RestaurantEntity.class).setParameter("restaurantId", restaurantId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /* The below method updates restaurant details in the databse for a particular UUID. */
    public RestaurantEntity updateRestaurantRating (RestaurantEntity restaurantEntity){
        entityManager.merge(restaurantEntity);
        return restaurantEntity;
    }
}
