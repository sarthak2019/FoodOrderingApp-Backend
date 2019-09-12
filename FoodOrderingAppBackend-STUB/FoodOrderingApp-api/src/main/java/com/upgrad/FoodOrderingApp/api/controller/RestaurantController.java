package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddress;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddressState;
import com.upgrad.FoodOrderingApp.api.model.RestaurantListResponse;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.api.model.RestaurantList;

import java.util.*;

@RestController
@RequestMapping("/")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getAllRestaurants() {
        final List<RestaurantEntity> restaurants = restaurantService.getAllRestaurants();
        Comparator<RestaurantEntity> compareByCustomerRating = new Comparator<RestaurantEntity>() {
            @Override
            public int compare(RestaurantEntity r1, RestaurantEntity r2) {
                return r2.getCustomerRating().compareTo(r1.getCustomerRating());
            }
        };
        Collections.sort(restaurants, compareByCustomerRating);
        RestaurantListResponse reataurantsResponse = restaurantslist(restaurants);
        return new ResponseEntity<RestaurantListResponse>(reataurantsResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/name/{restaurant_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantsByName(@PathVariable("restaurant_name") final String restaurant_name) throws RestaurantNotFoundException {
        final List<RestaurantEntity> restaurants = restaurantService.getRestaurantsByName("%" + restaurant_name + "%");
        if (restaurants == null) {
            return new ResponseEntity<RestaurantListResponse>(new RestaurantListResponse(), HttpStatus.NOT_FOUND);
        }
        Comparator<RestaurantEntity> compareByRestaurantName = new Comparator<RestaurantEntity>() {
            @Override
            public int compare(RestaurantEntity r1, RestaurantEntity r2) {
                return r1.getRestaurantName().compareTo(r2.getRestaurantName());
            }
        };
        RestaurantListResponse reataurantsResponse = restaurantslist(restaurants);
        return new ResponseEntity<RestaurantListResponse>(reataurantsResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantsByCtegoryId(@PathVariable("category_id") final String category_id) throws CategoryNotFoundException {
        final List<RestaurantEntity> restaurants = restaurantService.getRestaurantsByCategoryId(category_id);
        if (restaurants == null) {
            return new ResponseEntity<RestaurantListResponse>(new RestaurantListResponse(), HttpStatus.NOT_FOUND);
        }
        Comparator<RestaurantEntity> compareByRestaurantName = new Comparator<RestaurantEntity>() {
            @Override
            public int compare(RestaurantEntity r1, RestaurantEntity r2) {
                return r1.getRestaurantName().compareTo(r2.getRestaurantName());
            }
        };
        Collections.sort(restaurants, compareByRestaurantName);
        RestaurantListResponse reataurantsResponse = restaurantslist(restaurants);
        return new ResponseEntity<RestaurantListResponse>(reataurantsResponse, HttpStatus.OK);
    }

    public RestaurantListResponse restaurantslist(List<RestaurantEntity> reataurants) {
        RestaurantListResponse reataurantsListResponse = new RestaurantListResponse();
        List<RestaurantList> restaurantLists = new ArrayList<>();
        for (RestaurantEntity restaurantEntity : reataurants) {
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState();
            restaurantDetailsResponseAddressState.id(UUID.fromString(restaurantEntity.getAddress().getState().getUuid())).stateName(restaurantEntity.getAddress().getState().getStateName());
            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
            restaurantDetailsResponseAddress.id(UUID.fromString(restaurantEntity.getAddress().getUuid()))
                    .flatBuildingName(restaurantEntity.getAddress().getFlatBuilNumber())
                    .locality(restaurantEntity.getAddress().getLocality()).city(restaurantEntity.getAddress().getCity())
                    .pincode(restaurantEntity.getAddress().getPincode()).state(restaurantDetailsResponseAddressState);
            RestaurantList restaurantList = new RestaurantList();
            List<CategoryEntity> categories = restaurantEntity.getCategory();
            List<String> categoriyNames = new ArrayList<String>();
            for (CategoryEntity category : categories) {
                categoriyNames.add(category.getCategoryName());
            }
            Collections.sort(categoriyNames);
            String categoriesString = new String();
            for (String categoriyName : categoriyNames) {
                if (categoriesString.isEmpty()) {
                    categoriesString = categoriesString + categoriyName;
                } else {
                    categoriesString = categoriesString + ", " + categoriyName;
                }
            }
            restaurantList.id(UUID.fromString(restaurantEntity.getUuid())).restaurantName(restaurantEntity.getRestaurantName())
                    .photoURL(restaurantEntity.getPhotoUrl()).customerRating(restaurantEntity.getCustomerRating())
                    .averagePrice(restaurantEntity.getAveragePriceForTwo()).numberCustomersRated(restaurantEntity
                    .getNumberOfCustomersRated())
                    .address(restaurantDetailsResponseAddress).categories(categoriesString);
            restaurantLists.add(restaurantList);
        }
        reataurantsListResponse.setRestaurants(restaurantLists);
        return reataurantsListResponse;
    }
}
