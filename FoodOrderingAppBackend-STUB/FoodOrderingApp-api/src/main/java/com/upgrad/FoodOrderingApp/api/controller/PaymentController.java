package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.PaymentListResponse;
import com.upgrad.FoodOrderingApp.api.model.PaymentResponse;
import com.upgrad.FoodOrderingApp.service.businness.PaymentService;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @RequestMapping(method= RequestMethod.GET, path= "/payment" , produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    //public ResponseEntity<List<PaymentListResponse>> payment() throws NullPointerException{
    public ResponseEntity<PaymentListResponse> payment() throws NullPointerException{
        //final PaymentEntity paymentEntity= new PaymentEntity();
        //final PaymentListResponse> paymentListResponses = new ArrayList<PaymentListResponse>();

            final List<PaymentEntity> paymentMethods = paymentService.getAllPaymentMethods();

        final List<PaymentListResponse> paymentListResponses = new ArrayList<PaymentListResponse>();
        final PaymentListResponse paymentListResponse = new PaymentListResponse();
        //final List<PaymentEntity> paymentMethods=paymentService.paymentMethods(paymentEntity);
        //final List<PaymentEntity> paymentMethods=
               //System.out.println(paymentService.paymentMethods(paymentEntity));

        for(PaymentEntity paymentEntity: paymentMethods){
            //PaymentListResponse paymentListResponse=new PaymentListResponse();
            //paymentListResponses.add(paymentListResponse);
            PaymentResponse paymentResponse=new PaymentResponse().paymentName(paymentEntity.getPayment());
            paymentResponse.id((UUID.fromString(paymentEntity.getUuid())));
            //PaymentListResponse paymentListResponse=new PaymentListResponse().addPaymentMethodsItem(paymentResponse);
            paymentListResponse.addPaymentMethodsItem(paymentResponse);
            //paymentListResponses.add(paymentListResponse);
        }
        paymentListResponses.add(paymentListResponse);
        System.out.println(paymentListResponses);
        //return new ResponseEntity<List<PaymentListResponse>>(paymentListResponses, HttpStatus.OK);
        //return new ResponseEntity<List<PaymentListResponse>>(paymentListResponses, HttpStatus.OK);
        return new ResponseEntity<PaymentListResponse>(paymentListResponses.get(0), HttpStatus.OK);
        //return new ResponseEntity(HttpStatus.OK);
    }
}
