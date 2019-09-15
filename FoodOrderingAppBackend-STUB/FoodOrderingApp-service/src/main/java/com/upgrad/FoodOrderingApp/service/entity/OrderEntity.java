package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "orders", schema = "public")
@NamedQueries(
        {

        }
)
public class OrderEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @NotNull
    private String uuid;

    public Integer getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public BigDecimal getBill() {
        return bill;
    }

    public void setBill(BigDecimal bill) {
        this.bill = bill;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    public RestaurantEntity getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantEntity restaurant) {
        this.restaurant = restaurant;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "BILL")
    @NotNull
    private BigDecimal bill;

    @Column(name = "COUPON_ID")
    private Integer couponId;

    @Column(name = "DISCOUNT", columnDefinition = "BigDecimal default 0")
    private BigDecimal discount;

    @Column(name = "DATE")
    private ZonedDateTime date;

    @Column(name = "PAYMENT_ID")
    private Integer paymentId;

    @Column(name = "CUSTOMER_ID")
    @NotNull
    private Integer customerId;

    @Column(name = "ADDRESS_ID")
    @NotNull
    private Integer addressId;

    @Column(name = "RESTAURANT_ID")
    @NotNull
    private Integer restaurantId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private AddressEntity address;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private RestaurantEntity restaurant;
}