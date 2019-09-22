package com.upgrad.FoodOrderingApp.service.entity;

import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "order_item", schema = "public")
@NamedQueries(
        {

        }
)
public class OrderItemEntity {

    @Id
    @Column(
            name = "ID"
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Integer id;

    @Column(name = "QUANTITY")
    @NotNull
    private Integer quantity;

    @Column(name = "PRICE")
    @NotNull
    private Integer price;

    @ManyToOne
    @JoinColumn(name="item_id")
    private ItemEntity item;

    @ManyToOne
    @JoinColumn(name="order_id")
    private OrderEntity orders;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public ItemEntity getItem() {
        return item;
    }

    public void setItem(ItemEntity item) {
        this.item = item;
    }

    public OrderEntity getOrders() {
        return orders;
    }

    public void setOrders(OrderEntity orders) {
        this.orders = orders;
    }
}
