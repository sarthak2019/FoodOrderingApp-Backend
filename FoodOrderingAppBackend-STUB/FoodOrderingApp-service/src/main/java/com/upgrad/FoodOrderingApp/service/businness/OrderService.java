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
import java.util.ArrayList;
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

    @Autowired
    CustomerService customerService;

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
    public List<OrderEntity> getOrdersByCustomers(final UUID customerId) throws NullPointerException{
        System.out.println("In getOrdersByCustomers");
        List<OrderEntity> orderEntities = orderDao.getOrdersByCustomerId(customerId);

        return orderEntities;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public OrderEntity saveOrder(final OrderEntity orderEntity) {
        return this.orderDao.saveOrder(orderEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CouponEntity getCouponByCouponId(final String couponId) throws CouponNotFoundException {

        CouponEntity couponEntity = couponDao.getCouponById(couponId);

        if (couponEntity == null) {
            throw new CouponNotFoundException("CPF-002", "No coupon by this id");
        }
        return couponEntity;
    }

}