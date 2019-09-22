package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "customer_address", schema = "public")
@NamedQueries(
        {
                @NamedQuery(name = "customerAddressByAddressId", query = "select c from CustomerAddressEntity c where c.address =:address"),
                @NamedQuery(name = "customerAddressesListByCustomerId", query = "select c from CustomerAddressEntity c where c.customer =:customer")
        }
)
public class CustomerAddressEntity {

    @Id
    @Column(
            name = "ID"
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Integer id;

    @OneToOne
    @JoinColumn(name="address_id")
    private AddressEntity address;

    @OneToOne
    @JoinColumn(name="customer_id")
    private CustomerEntity customer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }
}
