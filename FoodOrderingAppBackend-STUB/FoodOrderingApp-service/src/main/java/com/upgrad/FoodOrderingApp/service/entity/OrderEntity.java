/*
package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "order", schema = "public")
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

    @Column(name = "COUPON_ID")
    private Integer couponId;

    @Column(name = "DISCOUNT", columnDefinition = "BigDecimal default 0")
    private BigDecimal discount;

    @Column(name = "DATE")
    private Date date;

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
*/
