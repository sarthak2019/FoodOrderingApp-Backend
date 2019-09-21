package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerAddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class CustomerAddressService {

    @Autowired
    private CustomerAddressDao customerAddressDao;


    @Transactional(propagation = Propagation.REQUIRED)
    public List<CustomerAddressEntity> getCustomerAddressesListByCustomer(final CustomerEntity customer) throws AuthorizationFailedException {
        List<CustomerAddressEntity> CustomerAddressEntities = customerAddressDao.getCustomerAddressesListByCustomer(customer);

        return CustomerAddressEntities;
    }

}
