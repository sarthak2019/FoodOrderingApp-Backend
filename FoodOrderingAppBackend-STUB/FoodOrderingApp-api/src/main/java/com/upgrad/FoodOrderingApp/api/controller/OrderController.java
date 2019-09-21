package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.*;
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
    private PaymentService paymentService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private ItemService itemService;

    @RequestMapping(method = RequestMethod.GET, path = "/order/coupon/{coupon_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> coupon(@PathVariable("coupon_name") final String couponName, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, CouponNotFoundException {
        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse();
        //final OrderListCoupon orderListCoupon= orderService.getCouponByCouponName(couponName,authorization);

        String[] bearerToken = authorization.split("Bearer ");

        CustomerEntity customerEntity = customerService.getCustomer(bearerToken[1]);

        CouponEntity couponEntity = orderService.getCouponByCouponName(couponName);
        couponDetailsResponse.setId(UUID.fromString(couponEntity.getUuid()));
        couponDetailsResponse.setCouponName(couponEntity.getCouponName());
        couponDetailsResponse.setPercent(couponEntity.getPercent());
        return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path="/order", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveOrderResponse> save(@RequestBody(required = false) final SaveOrderRequest saveOrderRequest, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException,CouponNotFoundException, AddressNotFoundException, PaymentMethodNotFoundException, RestaurantNotFoundException, ItemNotFoundException {
        final OrderEntity orderEntity= new OrderEntity();
        final LocalDateTime now = LocalDateTime.now();

        String[] bearerToken = authorization.split("Bearer ");
        final CustomerEntity customerEntity = customerService.getCustomer(bearerToken[1]);

        final PaymentEntity paymentEntity = paymentService.getPaymentByUUID(saveOrderRequest.getPaymentId().toString());

        final CouponEntity couponEntity = orderService.getCouponByCouponId(saveOrderRequest.getCouponId().toString());

        final AddressEntity addressEntity = addressService.getAddressByUUID(saveOrderRequest.getAddressId().toString(), customerEntity);

        final RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(saveOrderRequest.getRestaurantId().toString());
                //couponEntity.setUuid(saveOrderRequest.getCouponId().toString());
        //couponEntity.setUuid(UUID.fromString(saveOrderRequest.getCouponId().toString()));



        orderEntity.setUuid(UUID.randomUUID().toString());
        orderEntity.setBill(saveOrderRequest.getBill());
        orderEntity.setCustomer(customerEntity);
        orderEntity.setPayment(paymentEntity);
        orderEntity.setCoupon(couponEntity);
        orderEntity.setAddress(addressEntity);
        orderEntity.setRestaurant(restaurantEntity);
        orderEntity.setDiscount(saveOrderRequest.getDiscount());
        orderEntity.setDate(now);

        List<OrderItemEntity> orderitemEntities = new ArrayList<>();
        for(ItemQuantity itemQuantity : saveOrderRequest.getItemQuantities()) {
            ItemEntity itemEntity = itemService.getItemsById(itemQuantity.getItemId().toString());
            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setQuantity(itemQuantity.getQuantity());
            orderItemEntity.setPrice(itemQuantity.getPrice());
            orderItemEntity.setItem(itemEntity);
            orderItemEntity.setOrders(orderEntity);
            orderitemEntities.add(orderItemEntity);
        }

        orderEntity.setOrderItemEntity(orderitemEntities);

        final OrderEntity createdOrderEntity = orderService.saveOrder(orderEntity);

        SaveOrderResponse saveOrderResponse = new SaveOrderResponse()
                .id(createdOrderEntity.getUuid())
                .status("ORDER SUCCESSFULLY PLACED");

        //SaveOrderResponse saveOrderResponse = new SaveOrderResponse();
        return new ResponseEntity<SaveOrderResponse>(saveOrderResponse,HttpStatus.OK);
    }
}