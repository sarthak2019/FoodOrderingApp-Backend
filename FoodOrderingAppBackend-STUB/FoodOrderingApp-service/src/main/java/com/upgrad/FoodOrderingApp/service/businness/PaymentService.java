package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.PaymentDao;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.PaymentMethodNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    /* public List<PaymentEntity> paymentMethods(PaymentEntity paymentEntity){
         return paymentDao.getPaymentMethods(paymentEntity);
     }*/
    @Transactional(propagation = Propagation.REQUIRED)
    public List<PaymentEntity> getAllPaymentMethods() {
        System.out.println("in dao");
        List<PaymentEntity> daoList = paymentDao.getPaymentMethods();
        System.out.println("daoList " + daoList);
        return daoList;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public PaymentEntity getPaymentByUUID(String uuid) throws PaymentMethodNotFoundException {
        PaymentEntity paymentEntity = paymentDao.getPaymentByUUID(uuid);
        if (paymentEntity == null) {
            throw new PaymentMethodNotFoundException("PNF-002", "No payment method found by this id");
            //If the access token provided by the customer exists in the database, but the customer has already logged out
        }
        return paymentEntity;
    }
}