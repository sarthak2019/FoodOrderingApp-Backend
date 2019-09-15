package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CouponDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    CouponDao couponDao;

    @Autowired
    CustomerDao customerDao;

    @Autowired
    OrderDao orderDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public CouponEntity getCouponByCouponName(final String couponName, final String authorizationToken) throws AuthorizationFailedException, CouponNotFoundException {

        CustomerAuthEntity customerAuthEntity = customerDao.getCustomerAuthToken(authorizationToken);
        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        } else if (customerAuthEntity != null && customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        } else if (customerAuthEntity != null && ZonedDateTime.now().isAfter(customerAuthEntity.getExpiresAt())) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        } else {
            CouponEntity couponEntity = couponDao.getCouponName(couponName);

            if (couponEntity == null) {
                throw new CouponNotFoundException("CPF-001", "No coupon by this name");
            }
            //if(customerAuthEntity.getUser().getId() != couponEntity.getId()){
            return couponEntity;
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public OrderEntity saveOrder(final OrderEntity orderEntity) throws AuthorizationFailedException,CouponNotFoundException,AddressNotFoundException,PaymentMethodNotFoundException,RestaurantNotFoundException,ItemNotFoundException{
        CustomerAuthEntity customerAuthEntity = orderDao.getCustomerAuthToken(authorizationToken);
        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        } else if (customerAuthEntity != null && customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        } else if (customerAuthEntity != null && ZonedDateTime.now().isAfter(customerAuthEntity.getExpiresAt())) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        } else {
            OrderEntity orderEntity1 = orderDao.saveOrder(orderEntity);

            if (orderEntity.getCouponId()!= null && orderEntity.getCouponId()!=orderEntity1.getCouponId() ) {
                throw new CouponNotFoundException("CPF-002", "No coupon by this id");
            } else if (orderEntity.getAddressId()!= null && orderEntity.getAddressId()!=orderEntity1.getAddressId() ) {
                throw new AddressNotFoundException("ANF-003", "No address  by this id");
            } else if (orderEntity.getAddressId()!= null && orderEntity.getAddressId()!=orderEntity1.getAddressId() ) {
                throw new AuthorizationFailedException("ATHR-004", "You are not authorized to view/update/delete any one else's address");
            } else if (orderEntity.getPaymentId()!= null && orderEntity.getPaymentId()!=orderEntity1.getPaymentId() ) {
                throw new PaymentMethodNotFoundException("PNF-002", "No payment method found by this id");
            } else if (orderEntity.getRestaurantId()!= null && orderEntity.getRestaurantId()!=orderEntity1.getRestaurantId() ) {
                throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
            } else if (orderEntity.get()!= null && orderEntity.getRestaurantId()!=orderEntity1.getRestaurantId() ) {
                throw new ItemNotFoundException("INF-003", "No item by this id exist)");
            }
            else {
                return this.orderDao.saveOrder(orderEntity1);
            }

        }
    }


}
