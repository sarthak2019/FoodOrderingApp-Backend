package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CouponDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    CustomerDao customerDao;

    @Autowired
    CouponDao couponDao;

    @Autowired
    OrderDao orderDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public CouponEntity getCouponByCouponName(final String couponName) throws AuthorizationFailedException, CouponNotFoundException {

        CouponEntity couponEntity = couponDao.getCouponName(couponName);

        if (couponEntity == null) {
            throw new CouponNotFoundException("CPF-001", "No coupon by this name");
        }
        //if(customerAuthEntity.getUser().getId() != couponEntity.getId()){
        return couponEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public OrderEntity saveOrder(final OrderEntity orderEntity) throws CouponNotFoundException, AddressNotFoundException, PaymentMethodNotFoundException, RestaurantNotFoundException, ItemNotFoundException, AuthorizationFailedException {

        //OrderEntity orderEntity1 = orderDao.saveOrder(orderEntity);
        //OrderEntity orderEntity1= new OrderEntity();

        /*if (orderEntity.getCoupon() != null && orderEntity.getCoupon() != orderEntity1.getCoupon()) {
            throw new CouponNotFoundException("CPF-002", "No coupon by this id");
        } else if (orderEntity.getAddress() != null && orderEntity.getAddress() != orderEntity1.getAddress()) {
            throw new AddressNotFoundException("ANF-003", "No address  by this id");
        } else if (orderEntity.getAddress() != null && orderEntity.getAddress() != orderEntity1.getAddress()) {
            throw new AuthorizationFailedException("ATHR-004", "You are not authorized to view/update/delete any one else's address");
        } else if (orderEntity.getPaymentId() != null && orderEntity.getPaymentId() != orderEntity1.getPaymentId()) {
            throw new PaymentMethodNotFoundException("PNF-002", "No payment method found by this id");
        } else if (orderEntity.getRestaurantId() != null && orderEntity.getRestaurantId() != orderEntity1.getRestaurantId()) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        } else if (orderEntity.get() != null && orderEntity.getRestaurantId() != orderEntity1.getRestaurantId()) {
            throw new ItemNotFoundException("INF-003", "No item by this id exist)");
        }*/

        //customerDao.getCustomerByContactNumber(customerEntity.getContactNumber()) != null
        //customerEntity.getFirstName().isEmpty() || customerEntity.getContactNumber().isEmpty() || customerEntity.getEmail().isEmpty() || customerEntity.getPassword().isEmpty()

        CouponEntity couponEntity= new CouponEntity();
        couponEntity.setUuid(orderEntity.getCoupon().toString());

        AddressEntity addressEntity= new AddressEntity();
        addressEntity.setUuid(orderEntity.getAddress().toString());

        PaymentEntity paymentEntity= new PaymentEntity();
        paymentEntity.setUuid(orderEntity.getPayment().toString());

        RestaurantEntity restaurantEntity= new RestaurantEntity();
        restaurantEntity.setUuid(orderEntity.getRestaurant().toString());

        ItemEntity itemEntity= new ItemEntity();
        itemEntity.setUuid(orderEntity.getItem().toString());

        if (orderDao.getCouponByUuid(UUID.fromString(couponEntity.getUuid())) != null ) {
            throw new CouponNotFoundException("CPF-002", "No coupon by this id");
        } else if (orderDao.getAddressByUuid(UUID.fromString(addressEntity.getUuid())) != null ) {
            throw new AddressNotFoundException("ANF-003", "No address  by this id");
        } /*else if (orderEntity.getAddress() != null && orderEntity.getAddress() != orderEntity1.getAddress()) {
            throw new AuthorizationFailedException("ATHR-004", "You are not authorized to view/update/delete any one else's address");
        } */else if (orderDao.getPaymentByUuid(UUID.fromString(paymentEntity.getUuid())) != null ) {
            throw new PaymentMethodNotFoundException("PNF-002", "No payment method found by this id");
        } else if (orderDao.getRestaurantByUuid(UUID.fromString(restaurantEntity.getUuid())) != null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        } else if (orderDao.getItemByUuid(UUID.fromString(itemEntity.getUuid())) != null) {
            throw new ItemNotFoundException("INF-003", "No item by this id exist)");
        } else {
            return this.orderDao.saveOrder(orderEntity);
        }
    }

}