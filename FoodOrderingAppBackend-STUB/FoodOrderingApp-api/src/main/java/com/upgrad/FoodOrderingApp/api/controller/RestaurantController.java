package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddress;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddressState;
import com.upgrad.FoodOrderingApp.api.model.RestaurantListResponse;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.api.model.RestaurantList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getAllRestaurants(){


        final List<RestaurantEntity> reataurants = restaurantService.getAllRestaurants();

        RestaurantListResponse reataurantsResponse = restaurantslist(reataurants);

        return new ResponseEntity<RestaurantListResponse>(reataurantsResponse, HttpStatus.OK);


    }

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/name/{restaurant_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> createAnswer (@PathVariable("restaurant_name") final String restaurant_name) throws RestaurantNotFoundException{
        final List<RestaurantEntity> reataurants = restaurantService.getRestaurantsByName("%" +restaurant_name + "%");
        RestaurantListResponse reataurantsResponse = restaurantslist(reataurants);
        return new ResponseEntity<RestaurantListResponse>(reataurantsResponse, HttpStatus.OK);
    }

    public RestaurantListResponse restaurantslist(List<RestaurantEntity> reataurants){
        RestaurantListResponse reataurantsListResponse = new RestaurantListResponse();
        List<RestaurantList> restaurantLists = new ArrayList<>();
        for ( RestaurantEntity restaurantEntity : reataurants){
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
            for(CategoryEntity category : categories){
                categoriyNames.add(category.getCategoryName());
            }
            Collections.sort(categoriyNames);
            String categoriesString = new String();
            for(String  categoriyName : categoriyNames){
                if(categoriesString.isEmpty()) {
                    categoriesString = categoriesString + categoriyName;
                }
                else {
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
