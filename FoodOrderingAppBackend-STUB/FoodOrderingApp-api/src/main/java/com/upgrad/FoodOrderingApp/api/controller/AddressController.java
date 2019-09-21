package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.*;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//RestController annotation specifies that this class represents a REST API(equivalent of @Controller + @ResponseBody)
@RestController
//"@CrossOrigin” annotation enables cross-origin requests for all methods in that specific controller class.
@CrossOrigin
@RequestMapping("/")
public class AddressController {

    //Required services are autowired to enable access to methods defined in respective Business services
    @Autowired
    private AddressService addressService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerAddressService customerAddressService;


    //saveaddress  endpoint requests for all the attributes in “SaveAddressRequest” about the customer and saves the address of a customer successfully.
    @RequestMapping(method = RequestMethod.POST, path = "/address", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveaddress(@RequestBody(required = false) final SaveAddressRequest saveAddressRequest,
                                                           @RequestHeader("accessToken") final String accessToken) throws AuthorizationFailedException, SaveAddressException, AddressNotFoundException {

        String[] bearerToken = accessToken.split("Bearer ");
        final CustomerEntity customerEntity = customerService.getCustomer(bearerToken[1]);
        final StateEntity stateEntity = addressService.getStateByUUID(saveAddressRequest.getStateUuid());
        final AddressEntity addressEntity = new AddressEntity();
        final CustomerAddressEntity customerAddressEntity = new CustomerAddressEntity();

        addressEntity.setUuid(UUID.randomUUID().toString());
        addressEntity.setFlatBuilNumber(saveAddressRequest.getFlatBuildingName());
        addressEntity.setLocality(saveAddressRequest.getLocality());
        addressEntity.setCity(saveAddressRequest.getCity());
        addressEntity.setPinCode(saveAddressRequest.getPincode());
        addressEntity.setState(stateEntity);
        addressEntity.setActive(1);



        final AddressEntity savedAddressEntity = addressService.saveAddress(addressEntity);

        List<CustomerAddressEntity> customerAddressEntities = new ArrayList<>();
        customerAddressEntity.setCustomer(customerEntity);
        customerAddressEntity.setAddress(savedAddressEntity);
        final CustomerAddressEntity savedcustomerAddressEntity = customerAddressService.saveCustomerAddress(customerAddressEntity);
        customerAddressEntities.add(savedcustomerAddressEntity);
        savedAddressEntity.setCustomerAddressEntity(customerAddressEntities);

        final AddressEntity mergedddressEntity = addressService.mergeAddress(savedAddressEntity);


        SaveAddressResponse saveAddressResponse = new SaveAddressResponse()
                .id(mergedddressEntity.getUuid())
                .status("ADDRESS SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SaveAddressResponse>(saveAddressResponse, HttpStatus.CREATED);

    }

    //getallsavedaddresses endpoint retrieves all the addresses of a valid customer present in the database
    @RequestMapping(method = RequestMethod.GET, path = "/address/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> getAddressByUUID(@RequestHeader("accessToken") final String accessToken) throws AuthorizationFailedException {

        String[] bearerToken = accessToken.split("Bearer ");
        final CustomerEntity customerEntity = customerService.getCustomer(bearerToken[1]);
        final List<CustomerAddressEntity> customerAddressEntities = customerAddressService.getCustomerAddressesListByCustomer(customerEntity);

        List<AddressEntity> addressEntities = new ArrayList<>();
        for(CustomerAddressEntity customerAddressEntity : customerAddressEntities){
            AddressEntity addressEntitiy = customerAddressEntity.getAddress();
            addressEntities.add(addressEntitiy);
        }

        Comparator<AddressEntity> compareBySavedTime = new Comparator<AddressEntity>() {
            @Override
            public int compare(AddressEntity a1, AddressEntity a2) {
                return a1.getId().compareTo(a2.getId());
            }
        };
        Collections.sort(addressEntities, compareBySavedTime);


        AddressListResponse addressListResponse = new AddressListResponse();

        for (AddressEntity address : addressEntities) {
            AddressList addressList = new AddressList();
            addressList.id(UUID.fromString(address.getUuid()));
            addressList.flatBuildingName(address.getFlatBuilNumber());
            addressList.locality(address.getLocality());
            addressList.pincode(address.getPinCode());
            addressList.city(address.getCity());

            AddressListState addressListState = new AddressListState();
            addressListState.id(UUID.fromString(address.getState().getUuid()));
            addressListState.stateName(address.getState().getStateName());

            addressList.state(addressListState);

            addressListResponse.addAddressesItem(addressList);
        }

        return new ResponseEntity<AddressListResponse>(addressListResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/address/{address_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DeleteAddressResponse> deleteAddress(@PathVariable("address_id") final String addressUuid,
                                                               @RequestHeader("accessToken") final String accessToken) throws AuthorizationFailedException, AddressNotFoundException {

        String[] bearerToken = accessToken.split("Bearer ");
        final CustomerEntity signedinCustomerEntity = customerService.getCustomer(bearerToken[1]);
        final AddressEntity addressEntityToDelete = addressService.getAddressByUUID(addressUuid, signedinCustomerEntity);
        final String Uuid = addressService.deleteAddress(addressEntityToDelete);

        DeleteAddressResponse deleteAddressResponse = new DeleteAddressResponse()
                .id(UUID.fromString(Uuid))
                .status("ADDRESS DELETED SUCCESSFULLY");
        return new ResponseEntity<DeleteAddressResponse>(deleteAddressResponse, HttpStatus.OK);
    }


    //getallstates endpoint retrieves all the states present in the database
    @RequestMapping(method = RequestMethod.GET, path = "/states", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<StatesList>> getallstates() {

        List<StateEntity> stateEntityList = addressService.getAllStates();
        List<StatesList> statesLists = new ArrayList<StatesList>();

        for (StateEntity stateEntity : stateEntityList) {
            StatesList statesList = new StatesList();
            statesList.setId(UUID.fromString(stateEntity.getUuid()));
            statesList.setStateName(stateEntity.getStateName());
            statesLists.add(statesList);
        }

        return new ResponseEntity<List<StatesList>>(statesLists, HttpStatus.OK);
    }
}