package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CategoriesListResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//RestController annotation specifies that this class represents a REST API(equivalent of @Controller + @ResponseBody)
@RestController
//"@CrossOrigin” annotation enables cross-origin requests for all methods in that specific controller class.
@CrossOrigin
@RequestMapping("/")
public class CategoryController {

    //Required services are autowired to enable access to methods defined in respective Business services
    @Autowired
    private CategoryService categoryService;

    /* /category endpoint retrieves all the categories present in the database, ordered
    by their name and displays the response in a JSON format with the corresponding HTTP status. */
    @RequestMapping(method = RequestMethod.GET, path = "/category", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoriesListResponse> getAllCategoriesOrderedByName() {
        final List<CategoryEntity> categories = categoryService.getAllCategoriesOrderedByName();
        Comparator<CategoryEntity> compareByCategoryName = new Comparator<CategoryEntity>() {
            @Override
            public int compare(CategoryEntity c1, CategoryEntity c2) {
                return c1.getCategoryName().compareTo(c2.getCategoryName());
            }
        };
        Collections.sort(categories, compareByCategoryName);
        CategoriesListResponse categoriesListResponse = new CategoriesListResponse();
        for (CategoryEntity category : categories) {
            CategoryListResponse categoryListResponse = new CategoryListResponse();
            categoryListResponse.id(UUID.fromString(category.getUuid())).categoryName(category.getCategoryName());
            categoriesListResponse.addCategoriesItem(categoryListResponse);
        }
        return new ResponseEntity<CategoriesListResponse>(categoriesListResponse, HttpStatus.OK);
    }

    /* /category/{category_id} endpoint retrieve that category with all items within
    that category for the category_id path variable and then display the response in a JSON
    format with the corresponding HTTP status. */
    @RequestMapping(method = RequestMethod.GET, path = "/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoryDetailsResponse> getCategoryById(@PathVariable("category_id") final String category_id) throws CategoryNotFoundException {
        final CategoryEntity category = categoryService.getCategoryById(category_id);
        List<ItemEntity> items = category.getItems();
        Comparator<ItemEntity> compareByItemName = new Comparator<ItemEntity>() {
            @Override
            public int compare(ItemEntity i1, ItemEntity i2) {
                return i1.getItemName().toLowerCase().compareTo(i2.getItemName().toLowerCase());
            }
        };
        Collections.sort(items, compareByItemName);
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
        CategoryDetailsResponse categoryDetailsResponse = new CategoryDetailsResponse();
        categoryDetailsResponse.id(UUID.fromString(category.getUuid())).categoryName(category.getCategoryName()).itemList(itemLists);

        return new ResponseEntity<CategoryDetailsResponse>(categoryDetailsResponse, HttpStatus.OK);
    }

}
