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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ADDRESS_ID")
    private AddressEntity address;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COUPON_ID")
    private CustomerEntity coupon;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "order_item",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "item_id")}
    )
    private List<ItemEntity> item = new ArrayList<>();

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

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    public CustomerEntity getCoupon() {
        return coupon;
    }

    public void setCoupon(CustomerEntity coupon) {
        this.coupon = coupon;
    }

    public List<ItemEntity> getItem() {
        return item;
    }

    public void setItem(List<ItemEntity> item) {
        this.item = item;
    }
}