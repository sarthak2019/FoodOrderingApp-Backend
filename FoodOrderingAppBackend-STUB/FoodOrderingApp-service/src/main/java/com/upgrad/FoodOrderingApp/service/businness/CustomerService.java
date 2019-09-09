package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
public class CustomerService {

    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity saveCustomer(final CustomerEntity customerEntity) throws SignUpRestrictedException {
        //validates email format
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);

        //matches 10-digit numbers only
        String contactNumberRegex = "^[0-9]{10}$";

        //Throws relevant exceptions if the contact number provided already exists in the current database,
        if (customerDao.getCustomerByContactNumber(customerEntity.getContactNumber()) != null) {
            throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number.");

            //If any field other than last name is empty,
        } else if (customerEntity.getFirstName().isEmpty() || customerEntity.getContactNumber().isEmpty() || customerEntity.getEmail().isEmpty() || customerEntity.getPassword().isEmpty()) {
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");

            //If the email ID provided by the customer is not in the correct format, i.e., not in the format of xxx@xx.xx
        } else if (!pattern.matcher(customerEntity.getEmail()).matches()) {
            throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");

            //If the contact number provided by the customer is not in correct format, i.e., it does not contain only numbers and has more or less than 10 digits,
        } else if (!customerEntity.getContactNumber().matches(contactNumberRegex)) {
            throw new SignUpRestrictedException("SGR-003", "Invalid contact number!");

            //If the password provided by the customer is weak, i.e., it doesn’t have at least eight characters and does not contain at least one digit, one uppercase letter,
            // and one of the following characters [#@$%&*!^]
        } else if (customerEntity.getPassword().length() < 8 || !customerEntity.getPassword().matches("(?=.*[0-9]).*") || !customerEntity.getPassword().matches("(?=.*[A-Z]).*") || !customerEntity.getPassword().matches("(?=.*[~!@#$%^&*()_-]).*")) {
            throw new SignUpRestrictedException("SGR-004", "Weak password");

            //Else, save the customer information in the database.
        } else {
            String password = customerEntity.getPassword();
            if (password == null) {
                customerEntity.setPassword("Default@123");
            }
            String[] encryptedText = this.passwordCryptographyProvider.encrypt(customerEntity.getPassword());
            customerEntity.setSalt(encryptedText[0]);
            customerEntity.setPassword(encryptedText[1]);
            return this.customerDao.createCustomer(customerEntity);
        }
    }






}
