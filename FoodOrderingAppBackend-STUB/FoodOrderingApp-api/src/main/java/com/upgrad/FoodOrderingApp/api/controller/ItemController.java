package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.api.model.ItemListResponse;
import com.upgrad.FoodOrderingApp.api.model.RestaurantListResponse;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//RestController annotation specifies that this class represents a REST API(equivalent of @Controller + @ResponseBody)
@RestController
//"@CrossOrigin” annotation enables cross-origin requests for all methods in that specific controller class.
@CrossOrigin
@RequestMapping("/")
public class ItemController {

    //Required services are autowired to enable access to methods defined in respective Business services
    @Autowired
    private ItemService itemService;

    //Required services are autowired to enable access to methods defined in respective Business services
    @Autowired
    private RestaurantService restaurantService;

    /* /item/restaurant/{restaurant_id} endpoint retrieves the top five items of the restaurant
    corresponding to the restaurant_id path variable based on the number of times that item was
    ordered and then display the response in a JSON format with the corresponding HTTP status. */
    @RequestMapping(method = RequestMethod.GET, path = "/item/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ItemListResponse> getItemsByPopularity(@PathVariable("restaurant_id") final String restaurant_id) throws RestaurantNotFoundException {
        final RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurant_id);
        final List<ItemEntity> items = itemService.getItemsByPopularity(restaurantEntity);
        List<ItemList> itemLists = new ArrayList<ItemList>();
        for (ItemEntity item : items) {
            ItemList itemList = new ItemList();
            String itemType = item.getType();
            String newItemType = null;
            if (itemType.equals("0")) {
                newItemType = "VEG";
            } else if (itemType.equals("1")) {
                newItemType = "NON_VEG";
            }
            ItemList.ItemTypeEnum itemTypeEnum = ItemList.ItemTypeEnum.fromValue(newItemType);
            itemList.id(UUID.fromString(item.getUuid())).itemName(item.getItemName()).price(item.getPrice()).itemType(itemTypeEnum);
            itemLists.add(itemList);
        }
        ItemListResponse itemListResponse = new ItemListResponse();
        itemListResponse.addAll(itemLists);
        return new ResponseEntity<ItemListResponse>(itemListResponse, HttpStatus.OK);
    }

}
