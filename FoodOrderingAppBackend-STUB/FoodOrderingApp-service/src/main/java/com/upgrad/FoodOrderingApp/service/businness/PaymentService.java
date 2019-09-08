package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.PaymentDao;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
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
   public List<PaymentEntity> paymentMethods() {
       System.out.println("in dao");
       List<PaymentEntity> daoList =paymentDao.getPaymentMethods();
       System.out.println("daoList "+daoList);
       return daoList;
   }
}
