package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.OrderService;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET, path = "/order/coupon/{coupon_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> coupon(@PathVariable("coupon_name") final String couponName, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, CouponNotFoundException {
        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse();
        //final OrderListCoupon orderListCoupon= orderService.getCouponByCouponName(couponName,authorization);

        String[] bearerToken = authorization.split("Bearer ");

        CustomerEntity customerEntity = customerService.getCustomer(bearerToken[1]);

        CouponEntity couponEntity = orderService.getCouponByCouponName(couponName);
        //couponDetailsResponse.setId(UUID.fromString(couponEntity.getUuid().toString()));
        couponDetailsResponse.setId(couponEntity.getUuid());
        couponDetailsResponse.setCouponName(couponEntity.getCouponName());
        couponDetailsResponse.setPercent(couponEntity.getPercent());
        return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path="/order" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveOrderResponse> save(@RequestBody(required = false) final SaveOrderRequest saveOrderRequest ) throws AuthorizationFailedException,CouponNotFoundException, AddressNotFoundException, PaymentMethodNotFoundException, RestaurantNotFoundException, ItemNotFoundException {
        final OrderEntity orderEntity= new OrderEntity();
        final LocalDateTime now = LocalDateTime.now();
        final CouponEntity couponEntity= new CouponEntity();
        final PaymentEntity paymentEntity= new PaymentEntity();
        final CustomerEntity customerEntity= new CustomerEntity();
        final AddressEntity addressEntity= new AddressEntity();
        final RestaurantEntity restaurantEntity= new RestaurantEntity();
        final List<ItemEntity> itemEntities= new ArrayList<ItemEntity>();

        //couponEntity.setUuid(saveOrderRequest.getCouponId().toString());
        //couponEntity.setUuid(UUID.fromString(saveOrderRequest.getCouponId().toString()));
        couponEntity.setUuid(saveOrderRequest.getCouponId());
        orderEntity.setBill(saveOrderRequest.getBill());
        orderEntity.setCoupon(couponEntity);
        orderEntity.setDiscount(saveOrderRequest.getDiscount());
        orderEntity.setDate(now);
        paymentEntity.setUuid(saveOrderRequest.getPaymentId().toString());
        orderEntity.setPayment(paymentEntity);
        //orderEntity.setCustomerId(Integer.parseInt(saveOrderRequest.get.toString()));
        /*customerEntity.setUuid(saveOrderRequest.);
        orderEntity.setCustomer(customerEntity);*/
        //orderEntity.setAddressId(Integer.parseInt(saveOrderRequest.getAddressId()));
        addressEntity.setUuid(saveOrderRequest.getAddressId());
        orderEntity.setAddress(addressEntity);
        //orderEntity.setRestaurantId(Integer.parseInt(saveOrderRequest.getRestaurantId().toString()));
        restaurantEntity.setUuid(saveOrderRequest.getRestaurantId().toString());
        orderEntity.setRestaurant(restaurantEntity);

        //for(ItemEntity itemEntity:itemEntities){
            orderEntity.setItem(itemEntities);
        //}

        final OrderEntity createdOrderEntity = orderService.saveOrder(orderEntity);

        SaveOrderResponse saveOrderResponse = new SaveOrderResponse()
                .id(createdOrderEntity.getUuid())
                .status("ORDER SUCCESSFULLY PLACED");

        //SaveOrderResponse saveOrderResponse = new SaveOrderResponse();
        return new ResponseEntity<SaveOrderResponse>(saveOrderResponse,HttpStatus.OK);
    }
}