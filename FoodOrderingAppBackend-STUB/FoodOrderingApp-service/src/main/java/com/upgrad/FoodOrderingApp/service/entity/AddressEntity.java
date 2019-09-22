package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "address", schema = "public")
@NamedQueries(
        {
                @NamedQuery(name = "addressByAddressUuid", query = "select a from AddressEntity a where a.uuid =:uuid"),
                @NamedQuery(name = "allSavedAddresses", query = "select a from AddressEntity a "),//returns all the address records
                @NamedQuery(name = "getAddressById", query = "select a from AddressEntity a where a.id=:id")
        }
)
public class AddressEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @NotNull
    private String uuid;

    @Column(name = "FLAT_BUIL_NUMBER")
    private String flatBuilNo;


    @Column(name = "LOCALITY")
    private String locality;

    @Column(name = "CITY")
    private String city;

    @Column(name = "PINCODE")
    private String pincode;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id")
    private StateEntity state;

    @Column(name = "ACTIVE")
    private Integer active;


    @OneToMany(mappedBy="address")
    private List<CustomerAddressEntity> customerAddressEntity;

    @OneToMany(mappedBy = "address")
    private List<OrderEntity> orders;

    public AddressEntity(){

    }

    public AddressEntity(@NotNull String uuid, String flatBuilNumber, String locality, String city, String pincode, StateEntity state) {
        this.uuid = uuid;
        this.flatBuilNo = flatBuilNo;
        this.locality = locality;
        this.city = city;
        this.pincode = pincode;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFlatBuilNo() {
        return flatBuilNo;
    }

    public void setFlatBuilNo(String flatBuilNo) {
        this.flatBuilNo = flatBuilNo;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public StateEntity getState() {
        return state;
    }

    public void setState(StateEntity state) {
        this.state = state;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public void setPinCode(String pinCode) {
        this.pincode = pinCode;
    }

    public String getPinCode() {
        return pincode;
    }

    public List<CustomerAddressEntity> getCustomerAddressEntity() {
        return customerAddressEntity;
    }

    public void setCustomerAddressEntity(List<CustomerAddressEntity> customerAddressEntity) {
        this.customerAddressEntity = customerAddressEntity;
    }

    public List<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this, obj).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this).hashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}