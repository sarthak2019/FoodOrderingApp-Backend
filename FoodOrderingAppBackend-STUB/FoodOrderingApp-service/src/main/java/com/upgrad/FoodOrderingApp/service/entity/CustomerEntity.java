package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(
        name = "customer", schema = "public"
)
@NamedQueries(
        {       //return customer record matching with a particular contact number
                @NamedQuery(name = "customerByContactNumber", query = "select c from CustomerEntity c where c.contactNumber = :contactNumber")
        }
)
public class CustomerEntity implements Serializable {

    @Id
    @Column(
            name = "ID"
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;
    //uuid must be UNIQUE & NOTNULL
    @Column(
            name = "UUID"
    )
    @Size(
            max = 200
    )
    private String uuid;

    @Column(
            name = "FIRSTNAME"
    )
    @NotNull
    @Size(
            max = 30
    )
    private String firstName;

    @Column(
            name = "LASTNAME"
    )
    //lastName can be NULL
    @Size(
            max = 30
    )
    private String lastName;

    //email can be NULL
    @Column(
            name = "EMAIL"
    )

    @Size(
            max = 50
    )
    private String email;
    //contactNumber must be UNIQUE & NOTNULL
    @Column(
            name = "CONTACT_NUMBER"
    )
    @Size(
            max = 30
    )

    private String contactNumber;

    @Column(
            name = "PASSWORD"
    )
    @NotNull
    private String password;

    @OneToOne(mappedBy="customer")
    private OrderEntity order;

    @OneToOne(mappedBy="customer")
    private CustomerAddressEntity customerAddressEntity;


    private String salt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public CustomerAddressEntity getCustomerAddressEntity() {
        return customerAddressEntity;
    }

    public void setCustomerAddressEntity(CustomerAddressEntity customerAddressEntity) {
        this.customerAddressEntity = customerAddressEntity;
    }

    public boolean equals(Object obj) {
        return (new EqualsBuilder()).append(this, obj).isEquals();
    }

    public int hashCode() {
        return (new HashCodeBuilder()).append(this).hashCode();
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}