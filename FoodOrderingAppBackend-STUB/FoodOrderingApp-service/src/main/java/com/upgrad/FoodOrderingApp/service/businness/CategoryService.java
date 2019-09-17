package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    /* The below method returns a list of all the categories present in the database. */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<CategoryEntity> getAllCategoriesOrderedByName() {
        List<CategoryEntity> categoryEntities = categoryDao.getAllCategoriesOrderedByName();
        return categoryEntities;
    }

    /* The below method returns the category having a particular UUID. */
    @Transactional(propagation = Propagation.REQUIRED)
    public CategoryEntity getCategoryById(String categoryId) throws CategoryNotFoundException {
        if (categoryId.isEmpty()) {
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }
        CategoryEntity categoryEntity = categoryDao.getCategoryById(categoryId);
        if (categoryEntity == null) {
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }
        CategoryEntity category = categoryDao.getCategoryById(categoryId);
        return category;
    }

}
