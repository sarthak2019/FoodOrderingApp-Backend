package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CategoriesListResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET, path = "/category", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoriesListResponse> getAllRestaurants() {
        final List<CategoryEntity> categories = categoryService.getAllCategoriesOrderedByName();
        Comparator<CategoryEntity> compareByCategoryName = new Comparator<CategoryEntity>() {
            @Override
            public int compare(CategoryEntity c1, CategoryEntity c2) {
                return c1.getCategoryName().compareTo(c2.getCategoryName());
            }
        };
        Collections.sort(categories, compareByCategoryName);
        CategoriesListResponse categoriesListResponse = new CategoriesListResponse();
        for(CategoryEntity category : categories){
            CategoryListResponse categoryListResponse = new CategoryListResponse();
            categoryListResponse.id(UUID.fromString(category.getUuid())).categoryName(category.getCategoryName());
            categoriesListResponse.addCategoriesItem(categoryListResponse);
        }
        return new ResponseEntity<CategoriesListResponse>(categoriesListResponse, HttpStatus.OK);
    }

}
