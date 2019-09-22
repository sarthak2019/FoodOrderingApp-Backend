package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAddressDao;
import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService {

    //Respective Data access object has been autowired to access the method defined in respective Dao
    /*@Autowired
    private CustomerAddressDao customerAddressDao;*/
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private StateDao stateDao;
    @Autowired
    private CustomerAddressDao customerAddressDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public StateEntity getStateByUUID(final String StateUuid) throws AddressNotFoundException, SaveAddressException {
        StateEntity stateEntity = stateDao.getStateByStateUuid(StateUuid);
        if (StateUuid.isEmpty()) {
            throw new SaveAddressException("SAR-001", "No field can be empty");
        }
        if (stateEntity == null) {
            throw new AddressNotFoundException("ANF-002", "No state by this id");
        } else {
            return stateEntity;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public StateEntity getStateByUUID(final long id) {
        return stateDao.getStateById(id);

    }


    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity saveAddress(final AddressEntity addressEntity) throws SaveAddressException {

        String pinCodeRegex = "^[0-9]{6}$";

        if (addressEntity.getFlatBuilNumber().isEmpty() || addressEntity.getLocality().isEmpty() || addressEntity.getCity().isEmpty() || addressEntity.getPinCode().isEmpty() || addressEntity.getUuid().isEmpty()) {
            throw new SaveAddressException("SAR-001", "No field can be empty");
        } else if (!addressEntity.getPinCode().matches(pinCodeRegex)) {
            throw new SaveAddressException("SAR-002", "Invalid pincode");
        } else {
            return addressDao.createAddress(addressEntity);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity mergeAddress(final AddressEntity addressEntity) {
        return addressDao.mergeAddress(addressEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<StateEntity> getAllStates() {
        return stateDao.getAllStates();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String deleteAddress(AddressEntity addressEntity) throws AuthorizationFailedException {
        return addressDao.deleteAddress(addressEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity getAddressByUUID(String addressUuid, final CustomerEntity signedincustomerEntity) throws AddressNotFoundException {
        if (addressUuid.isEmpty()) {
            throw new AddressNotFoundException("ANF-005", "Address id can not be empty");
        }
        AddressEntity addressEntity = addressDao.getAddressByAddressUuid(addressUuid);
        if (addressEntity == null) {
            throw new AddressNotFoundException("ANF-003", "No address by this id");
        }
        List<CustomerAddressEntity> customerAddressEntities = customerAddressDao.getCustomerAddressesListByCustomer(signedincustomerEntity);
        for (CustomerAddressEntity customerAddressEntity : customerAddressEntities) {
            if (customerAddressEntity.getAddress().getUuid().equals(addressEntity.getUuid())) {
                return addressEntity;
            }
        }
        throw new AddressNotFoundException("ATHR-004", "You are not authorized to view/update/delete any one else's address");

    }
}