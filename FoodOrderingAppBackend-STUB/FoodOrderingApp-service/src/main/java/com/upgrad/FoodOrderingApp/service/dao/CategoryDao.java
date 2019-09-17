package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    /* The below method fetches a list of all the categories present from the database. */
    public List<CategoryEntity> getAllCategoriesOrderedByName() {
        try {
            return entityManager.createNamedQuery("allCategories", CategoryEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /* The below method fetches the category having a particular UUID from the database. */
    public CategoryEntity getCategoryById(String categoryId) {
        try {
            return entityManager.createNamedQuery("categoryById", CategoryEntity.class).setParameter("categoryId", categoryId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}
