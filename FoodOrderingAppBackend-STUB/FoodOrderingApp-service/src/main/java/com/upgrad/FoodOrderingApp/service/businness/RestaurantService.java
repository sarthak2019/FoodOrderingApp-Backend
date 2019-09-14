package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CustomerDao customerDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> getAllRestaurants() {
        return restaurantDao.getAllRestaurants();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> restaurantsByName(String restaurantName) throws RestaurantNotFoundException {
        if (restaurantName.isEmpty()) {
            throw new RestaurantNotFoundException("RNF-003", "Restaurant name field should not be empty");
        }
        List<RestaurantEntity> restaurantEntityList = restaurantDao.getRestaurantsByName(restaurantName);
        return restaurantEntityList;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> restaurantByCategory(String categoryId) throws CategoryNotFoundException {
        if (categoryId.isEmpty()) {
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }
        CategoryEntity categoryEntity = categoryDao.getCategoryById(categoryId);
        if (categoryEntity == null) {
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }
        List<RestaurantEntity> restaurantEntityList = categoryEntity.getRestaurant();
        return restaurantEntityList;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity restaurantByUUID(String restaurantId) throws RestaurantNotFoundException {
        if (restaurantId.isEmpty()) {
            throw new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty");
        }
        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantsById(restaurantId);
        if(restaurantEntity == null){
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }
        return restaurantEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity updateRestaurantRating(String accessToken, String restaurantId, Double rating) throws AuthorizationFailedException, RestaurantNotFoundException, InvalidRatingException {
        CustomerAuthEntity customerAuthEntity = customerDao.getCustomerAuthToken(accessToken);

        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
            //If the access token provided by the customer exists in the database, but the customer has already logged out
        } else if (customerAuthEntity != null && customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
            //If the access token provided by the customer exists in the database, but the session has expired
        } else if (customerAuthEntity != null && ZonedDateTime.now().isAfter(customerAuthEntity.getExpiresAt())) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }

        if (restaurantId.isEmpty()) {
            throw new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty");
        }
        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantsById(restaurantId);
        if(restaurantEntity == null){
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }
        if(rating.toString().isEmpty() || (rating>5 || rating<1)){
            throw new InvalidRatingException("IRE-001", "Restaurant should be in the range of 1 to 5");
        }
        BigDecimal oldRating = restaurantEntity.getCustomerRating();
        Double oldRatingDouble = oldRating.doubleValue();
        Integer oldNumCustRated = restaurantEntity.getNumberOfCustomersRated();
        Integer newNumCustRated = oldNumCustRated + 1;
        Double newRatingDouble = ((oldRatingDouble*Double.valueOf(oldNumCustRated))+rating)/newNumCustRated;
        BigDecimal newRating = BigDecimal.valueOf(newRatingDouble);
        restaurantEntity.setCustomerRating(newRating);
        restaurantEntity.setNumberOfCustomersRated(newNumCustRated);
        RestaurantEntity newrestaurantEntity = restaurantDao.updateRestaurantRating(restaurantEntity);
        return newrestaurantEntity;
    }

}
