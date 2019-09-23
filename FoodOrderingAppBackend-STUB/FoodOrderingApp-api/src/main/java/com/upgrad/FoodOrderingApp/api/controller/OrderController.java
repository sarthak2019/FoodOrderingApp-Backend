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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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

    @RequestMapping(method = RequestMethod.GET, path = "/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<OrderList>> orders(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, CouponNotFoundException, AddressNotFoundException, PaymentMethodNotFoundException, RestaurantNotFoundException, ItemNotFoundException {
        String[] bearerToken = authorization.split("Bearer ");
        final CustomerEntity customerEntity = customerService.getCustomer(bearerToken[1]);
        final List<OrderEntity> orderEntities = orderService.getOrdersByCustomers(customerEntity.getUuid());

        CustomerOrderResponse customerOrderResponse = new CustomerOrderResponse();
        List<OrderList> orderLists = new ArrayList<>();

        for (OrderEntity orderEntity : orderEntities) {
            OrderList orderList = new OrderList();
            OrderListCoupon orderListCoupon = new OrderListCoupon();
            OrderListPayment orderListPayment = new OrderListPayment();
            OrderListCustomer orderListCustomer = new OrderListCustomer();
            OrderListAddress orderListAddress = new OrderListAddress();
            OrderListAddressState orderListAddressState = new OrderListAddressState();
            orderListCoupon.id(UUID.fromString(orderEntity.getCoupon().getUuid())).couponName(orderEntity.getCoupon().getCouponName())
                    .percent(orderEntity.getCoupon().getPercent());
            orderListPayment.id(UUID.fromString(orderEntity.getPayment().getUuid())).paymentName(orderEntity.getPayment().getPaymentName());
            orderListCustomer.id(UUID.fromString(orderEntity.getCustomer().getUuid())).firstName(orderEntity.getCustomer().getFirstName())
                    .lastName(orderEntity.getCustomer().getLastName()).emailAddress(orderEntity.getCustomer().getEmail())
                    .contactNumber(orderEntity.getCustomer().getContactNumber());
            orderListAddressState.id(UUID.fromString(orderEntity.getAddress().getState().getUuid()))
                    .stateName(orderEntity.getAddress().getState().getStateName());
            orderListAddress.id(UUID.fromString(orderEntity.getAddress().getUuid()))
                    .flatBuildingName(orderEntity.getAddress().getFlatBuilNo())
                    .locality(orderEntity.getAddress().getLocality()).city(orderEntity.getAddress().getCity())
                    .pincode(orderEntity.getAddress().getPinCode()).state(orderListAddressState);
            List<ItemQuantityResponse> itemQuantityResponseList = new ArrayList<>();
            for (OrderItemEntity orderItemEntity : orderEntity.getOrderItemEntity()) {
                ItemQuantityResponse itemQuantityResponse = new ItemQuantityResponse();
                ItemQuantityResponseItem itemQuantityResponseItem = new ItemQuantityResponseItem();
                String itemType = orderItemEntity.getItem().getType();
                String newItemType = null;
                if (itemType.equals("0")) {
                    newItemType = "VEG";
                } else if (itemType.equals("1")) {
                    newItemType = "NON_VEG";
                }
                ItemQuantityResponseItem.TypeEnum typeEnum = ItemQuantityResponseItem.TypeEnum.fromValue(newItemType);
                itemQuantityResponseItem.id(UUID.fromString(orderItemEntity.getItem().getUuid())).itemName(orderItemEntity.getItem().getItemName())
                        .itemPrice(orderItemEntity.getItem().getPrice()).type(typeEnum);
                itemQuantityResponse.item(itemQuantityResponseItem).quantity(orderItemEntity.getQuantity())
                        .price(orderItemEntity.getItem().getPrice());
                itemQuantityResponseList.add(itemQuantityResponse);
            }
            orderList.id(UUID.fromString(orderEntity.getUuid())).bill(BigDecimal.valueOf(orderEntity.getBill())).coupon(orderListCoupon)
                    .discount(BigDecimal.valueOf(orderEntity.getDiscount())).date(orderEntity.getDate().toString()).payment(orderListPayment)
                    .customer(orderListCustomer).address(orderListAddress).itemQuantities(itemQuantityResponseList);
            orderLists.add(orderList);
        }

        return new ResponseEntity<List<OrderList>>(orderLists, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/order", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveOrderResponse> save(@RequestBody(required = false) final SaveOrderRequest saveOrderRequest, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, CouponNotFoundException, AddressNotFoundException, PaymentMethodNotFoundException, RestaurantNotFoundException, ItemNotFoundException {
        final OrderEntity orderEntity = new OrderEntity();
        final Date now = new Date();

        String[] bearerToken = authorization.split("Bearer ");
        final CustomerEntity customerEntity = customerService.getCustomer(bearerToken[1]);

        final PaymentEntity paymentEntity = paymentService.getPaymentByUUID(saveOrderRequest.getPaymentId().toString());

        final CouponEntity couponEntity = orderService.getCouponByCouponId(saveOrderRequest.getCouponId().toString());

        final AddressEntity addressEntity = addressService.getAddressByUUID(saveOrderRequest.getAddressId().toString(), customerEntity);

        final RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(saveOrderRequest.getRestaurantId().toString());

        orderEntity.setUuid(UUID.randomUUID().toString());
        orderEntity.setBill(Double.valueOf(saveOrderRequest.getBill().toString()));
        orderEntity.setCustomer(customerEntity);
        orderEntity.setPayment(paymentEntity);
        orderEntity.setCoupon(couponEntity);
        orderEntity.setAddress(addressEntity);
        orderEntity.setRestaurant(restaurantEntity);
        orderEntity.setDiscount(Double.valueOf(saveOrderRequest.getDiscount().toString()));
        orderEntity.setDate(now);

        List<OrderItemEntity> orderitemEntities = new ArrayList<>();
        for (ItemQuantity itemQuantity : saveOrderRequest.getItemQuantities()) {
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

        return new ResponseEntity<SaveOrderResponse>(saveOrderResponse, HttpStatus.CREATED);
    }
}