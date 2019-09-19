package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.ItemDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private ItemDao itemDao;

    /* The below method returns the list of top 5 items by popularity for a particular restaurant. */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<ItemEntity> getItemsByPopularity(RestaurantEntity restaurantEntity) {
        List<ItemEntity> itemEntities = restaurantEntity.getItem();
        return itemEntities;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ItemEntity getItemsById(String uuid) throws ItemNotFoundException {
        ItemEntity itemEntity = itemDao.getItemsById(uuid);

        if (itemEntity == null) {
            throw new ItemNotFoundException("INF-003", "No item by this id exist");
        }

        return itemEntity;
    }

}
