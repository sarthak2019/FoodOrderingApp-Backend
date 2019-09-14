package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;

import java.math.BigDecimal;
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
        final List<RestaurantEntity> restaurants = restaurantService.restaurantsByName("%" + restaurant_name + "%");
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
    public ResponseEntity<RestaurantListResponse> getRestaurantsByCategoryId(@PathVariable("category_id") final String category_id) throws CategoryNotFoundException {
        final List<RestaurantEntity> restaurants = restaurantService.restaurantByCategory(category_id);
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

    @RequestMapping(method = RequestMethod.GET, path = "/api/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
        public ResponseEntity<RestaurantDetailsResponse> getRestaurantsById(@PathVariable("restaurant_id") final String restaurant_id) throws RestaurantNotFoundException {
        final RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurant_id);
        RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse();
        RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState();
        restaurantDetailsResponseAddressState.id(UUID.fromString(restaurantEntity.getAddress().getState().getUuid())).stateName(restaurantEntity.getAddress().getState().getStateName());
        RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
        restaurantDetailsResponseAddress.id(UUID.fromString(restaurantEntity.getAddress().getUuid()))
                .flatBuildingName(restaurantEntity.getAddress().getFlatBuilNumber())
                .locality(restaurantEntity.getAddress().getLocality()).city(restaurantEntity.getAddress().getCity())
                .pincode(restaurantEntity.getAddress().getPincode()).state(restaurantDetailsResponseAddressState);
        RestaurantList restaurantList = new RestaurantList();
        List<CategoryEntity> categories = restaurantEntity.getCategory();
        Comparator<CategoryEntity> compareByCategoryName = new Comparator<CategoryEntity>() {
            @Override
            public int compare(CategoryEntity c1, CategoryEntity c2) {
                return c1.getCategoryName().compareTo(c2.getCategoryName());
            }
        };
        Collections.sort(categories, compareByCategoryName);
        List<CategoryList> categoryLists = new ArrayList<CategoryList>();
        for(CategoryEntity category : categories){
            List<ItemEntity> items = category.getItem();
            Comparator<ItemEntity> compareByItemName = new Comparator<ItemEntity>() {
                @Override
                public int compare(ItemEntity i1, ItemEntity i2) {
                    return i1.getItemName().toLowerCase().compareTo(i2.getItemName().toLowerCase());
                }
            };
            Collections.sort(items, compareByItemName);
            List<ItemList> itemLists = new ArrayList<ItemList>();
            for(ItemEntity item : items){
                ItemList itemList = new ItemList();
                String itemType = item.getType();
                String newItemType = null;
                if(itemType.equals("0")) {
                    newItemType = "VEG";
                }
                else if(itemType.equals("1")){
                    newItemType = "NON_VEG";
                }
                ItemList.ItemTypeEnum itemTypeEnum = ItemList.ItemTypeEnum.fromValue(newItemType);
                itemList.id(UUID.fromString(item.getUuid())).itemName(item.getItemName()).price(item.getPrice()).itemType(itemTypeEnum);
                itemLists.add(itemList);
            }
            CategoryList categoryList = new CategoryList();
            categoryList.id(UUID.fromString(category.getUuid())).categoryName(category.getCategoryName()).itemList(itemLists);
            categoryLists.add(categoryList);
        }
        restaurantDetailsResponse.id(UUID.fromString(restaurantEntity.getUuid())).restaurantName(restaurantEntity.getRestaurantName())
                .photoURL(restaurantEntity.getPhotoUrl()).customerRating(restaurantEntity.getCustomerRating())
                .averagePrice(restaurantEntity.getAveragePriceForTwo()).numberCustomersRated(restaurantEntity
                .getNumberOfCustomersRated())
                .address(restaurantDetailsResponseAddress).categories(categoryLists);
        return new ResponseEntity<RestaurantDetailsResponse>(restaurantDetailsResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/api/restaurant/{restaurant_id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantUpdatedResponse> updateRestaurantDetails  (@PathVariable("restaurant_id") final String restaurant_id, @RequestHeader("authorization") final String authorization, @RequestParam("customer_rating") Double customer_rating) throws AuthorizationFailedException, RestaurantNotFoundException, InvalidRatingException {
        RestaurantEntity newRestaurantEntity = restaurantService.updateRestaurantRating(restaurant_id, customer_rating);
        RestaurantUpdatedResponse restaurantUpdatedResponse = new RestaurantUpdatedResponse();
        restaurantUpdatedResponse.id(UUID.fromString(newRestaurantEntity.getUuid())).status("RESTAURANT RATING UPDATED SUCCESSFULLY");
        return new ResponseEntity<RestaurantUpdatedResponse>(restaurantUpdatedResponse, HttpStatus.OK);
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
            List<String> categoryNames = new ArrayList<String>();
            for (CategoryEntity category : categories) {
                categoryNames.add(category.getCategoryName());
            }
            Collections.sort(categoryNames);
            String categoriesString = new String();
            for (String categoryName : categoryNames) {
                if (categoriesString.isEmpty()) {
                    categoriesString = categoriesString + categoryName;
                } else {
                    categoriesString = categoriesString + ", " + categoryName;
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
