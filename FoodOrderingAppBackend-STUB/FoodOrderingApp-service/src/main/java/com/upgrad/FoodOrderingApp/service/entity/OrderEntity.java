package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders", schema = "public")
@NamedQueries(
        {
                @NamedQuery(name = "getOrdersByCustomerId", query = "select o from OrderEntity o where o.customer = :customer")
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
    private Double bill;

    @Column(name = "DISCOUNT", columnDefinition = "BigDecimal default 0")
    private Double discount;

    @Column(name = "DATE")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "PAYMENT_ID")
    private PaymentEntity payment;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "RESTAURANT_ID")
    private RestaurantEntity restaurant;

    @ManyToOne
    @JoinColumn(name = "COUPON_ID")
    private CouponEntity coupon;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.PERSIST)
    private List<OrderItemEntity> orderItemEntity;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    public OrderEntity(@NotNull String uuid, @NotNull Double bill, CouponEntity coupon, Double discount, Date date, PaymentEntity payment, CustomerEntity customer, AddressEntity address, RestaurantEntity restaurant) {
        this.uuid = uuid;
        this.bill = bill;
        this.discount = discount;
        this.date = date;
        this.payment = payment;
        this.customer = customer;
        this.restaurant = restaurant;
        this.coupon = coupon;
        this.orderItemEntity = orderItemEntity;
        this.address = address;
    }

    public OrderEntity() {

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

    public Double getBill() {
        return bill;
    }

    public void setBill(Double bill) {
        this.bill = bill;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public PaymentEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentEntity payment) {
        this.payment = payment;
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