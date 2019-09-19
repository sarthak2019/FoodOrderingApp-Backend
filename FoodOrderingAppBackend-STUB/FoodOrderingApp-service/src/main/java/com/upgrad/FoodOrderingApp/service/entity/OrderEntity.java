package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "BILL")
    @NotNull
    private BigDecimal bill;

    @Column(name = "DISCOUNT", columnDefinition = "BigDecimal default 0")
    private BigDecimal discount;

    @Column(name = "DATE")
    private LocalDateTime date;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PAYMENT_ID")
    private PaymentEntity payment;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CUSTOMER_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CustomerEntity customer;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RESTAURANT_ID")
    private RestaurantEntity restaurant;

    @OneToMany(mappedBy="orders", cascade = CascadeType.PERSIST)
    private List<AddressEntity> addresses;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COUPON_ID")
    private CouponEntity coupon;

    @OneToMany(mappedBy="orders", cascade = CascadeType.PERSIST)
    private List<OrderItemEntity> orderItemEntity;

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

    public BigDecimal getBill() {
        return bill;
    }

    public void setBill(BigDecimal bill) {
        this.bill = bill;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public PaymentEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentEntity payment) {
        this.payment = payment;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public RestaurantEntity getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantEntity restaurant) {
        this.restaurant = restaurant;
    }

    public List<AddressEntity> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressEntity> addresses) {
        this.addresses = addresses;
    }

    public CouponEntity getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponEntity coupon) {
        this.coupon = coupon;
    }

    public List<OrderItemEntity> getOrderItemEntity() {
        return orderItemEntity;
    }

    public void setOrderItemEntity(List<OrderItemEntity> orderItemEntity) {
        this.orderItemEntity = orderItemEntity;
    }
}