package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.OrderService;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
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

    @RequestMapping(method = RequestMethod.POST, path="/order" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveOrderResponse> save(@RequestBody(required = false) final SaveOrderRequest saveOrderRequest ) throws AuthorizationFailedException,CouponNotFoundException, AddressNotFoundException, PaymentMethodNotFoundException, RestaurantNotFoundException, ItemNotFoundException {
        final OrderEntity orderEntity= new OrderEntity();
        final ZonedDateTime now = ZonedDateTime.now();

        orderEntity.setBill(saveOrderRequest.getBill());
        orderEntity.setCouponId(Integer.parseInt(saveOrderRequest.getCouponId().toString()));
        orderEntity.setDiscount(saveOrderRequest.getDiscount());
        orderEntity.setDate(now);
        orderEntity.setPaymentId(Integer.parseInt(saveOrderRequest.getPaymentId().toString()));
        //orderEntity.setCustomerId(Integer.parseInt(saveOrderRequest.get.toString()));
        orderEntity.setAddressId(Integer.parseInt(saveOrderRequest.getAddressId()));
        orderEntity.setRestaurantId(Integer.parseInt(saveOrderRequest.getRestaurantId().toString()));

        final OrderEntity createdOrderEntity = orderService.saveOrder(orderEntity);

        SaveOrderResponse saveOrderResponse = new SaveOrderResponse()
                .id(createdOrderEntity.getUuid())
                .status("ORDER SUCCESSFULLY PLACED");

        return new ResponseEntity<SaveOrderResponse>(saveOrderResponse,HttpStatus.OK);
    }
}
