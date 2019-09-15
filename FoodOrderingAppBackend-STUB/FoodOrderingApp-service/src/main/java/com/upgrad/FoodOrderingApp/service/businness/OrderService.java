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

    /*@Transactional(propagation = Propagation.REQUIRED)
    public OrderEntity saveOrder(final OrderEntity orderEntity) throws CouponNotFoundException, AddressNotFoundException, PaymentMethodNotFoundException, RestaurantNotFoundException, ItemNotFoundException {

        OrderEntity orderEntity1 = orderDao.saveOrder(orderEntity);

        if (orderEntity.getCouponId() != null && orderEntity.getCouponId() != orderEntity1.getCouponId()) {
            throw new CouponNotFoundException("CPF-002", "No coupon by this id");
        } else if (orderEntity.getAddressId() != null && orderEntity.getAddressId() != orderEntity1.getAddressId()) {
            throw new AddressNotFoundException("ANF-003", "No address  by this id");
        } else if (orderEntity.getAddressId() != null && orderEntity.getAddressId() != orderEntity1.getAddressId()) {
            throw new AuthorizationFailedException("ATHR-004", "You are not authorized to view/update/delete any one else's address");
        } else if (orderEntity.getPaymentId() != null && orderEntity.getPaymentId() != orderEntity1.getPaymentId()) {
            throw new PaymentMethodNotFoundException("PNF-002", "No payment method found by this id");
        } else if (orderEntity.getRestaurantId() != null && orderEntity.getRestaurantId() != orderEntity1.getRestaurantId()) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        } else if (orderEntity.get() != null && orderEntity.getRestaurantId() != orderEntity1.getRestaurantId()) {
            throw new ItemNotFoundException("INF-003", "No item by this id exist)");
        } else {
            return this.orderDao.saveOrder(orderEntity1);
        }
    }*/

}