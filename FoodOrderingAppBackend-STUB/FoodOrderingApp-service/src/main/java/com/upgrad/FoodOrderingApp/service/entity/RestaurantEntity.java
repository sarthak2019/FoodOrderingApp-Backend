package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant", schema = "public")
@NamedQueries(
        {
                @NamedQuery(name = "allRestaurants", query = "select r from RestaurantEntity r"),
                @NamedQuery(name = "restaurantsByName", query = "select r from RestaurantEntity r where lower(r.restaurantName) like :restaurantName"),
                @NamedQuery(name = "restaurantsById", query = "select r from RestaurantEntity r where r.uuid = :restaurantId")
        }
)
public class RestaurantEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @NotNull
    private String uuid;

    @Column(name = "RESTAURANT_NAME")
    @NotNull
    private String restaurantName;

    @Column(name = "PHOTO_URL")
    private String photoUrl;

    @Column(name = "CUSTOMER_RATING")
    @NotNull
    private Double customerRating;

    @Column(name = "AVERAGE_PRICE_FOR_TWO")
    @NotNull
    private Integer avgPrice;

    @Column(name = "NUMBER_OF_CUSTOMERS_RATED")
    @NotNull
    private Integer numberCustomersRated;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private AddressEntity address;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<CategoryEntity> category = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private List<ItemEntity> item = new ArrayList<>();

    @OneToMany(mappedBy="payment")
    private List<OrderEntity> order;

    public List<ItemEntity> getItem() {
        return item;
    }

    public void setItem(List<ItemEntity> item) {
        this.item = item;
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

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Integer getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(Integer avgPrice) {
        this.avgPrice = avgPrice;
    }

    public Double getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(Double customerRating) {
        this.customerRating = customerRating;
    }

    public Integer getNumberCustomersRated() {
        return numberCustomersRated;
    }

    public void setNumberCustomersRated(Integer numberCustomersRated) {
        this.numberCustomersRated = numberCustomersRated;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    public List<CategoryEntity> getCategory() {
        return category;
    }

    public void setCategory(List<CategoryEntity> category) {
        this.category = category;
    }

    public List<OrderEntity> getOrder() {
        return order;
    }

    public void setOrder(List<OrderEntity> order) {
        this.order = order;
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
