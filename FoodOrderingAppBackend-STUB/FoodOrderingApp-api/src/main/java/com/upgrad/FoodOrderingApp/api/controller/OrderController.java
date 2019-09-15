package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CouponDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.OrderList;
import com.upgrad.FoodOrderingApp.api.model.OrderListCoupon;
import com.upgrad.FoodOrderingApp.service.businness.OrderService;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(method= RequestMethod.GET, path= "/order/coupon/{coupon_name}" , produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> coupon(@PathVariable("coupon_name") final String couponName , @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, CouponNotFoundException {
        CouponDetailsResponse couponDetailsResponse= new CouponDetailsResponse();
        //final OrderListCoupon orderListCoupon= orderService.getCouponByCouponName(couponName,authorization);
        CouponEntity couponEntity= orderService.getCouponByCouponName(couponName,authorization);
        couponDetailsResponse.setId(UUID.fromString(couponEntity.getUuid()));
        couponDetailsResponse.setCouponName(couponEntity.getCouponName());
        couponDetailsResponse.setPercent(couponEntity.getPercent());
        return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse, HttpStatus.OK);
    }
}
