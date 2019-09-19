package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "coupon", schema = "public")
@NamedQueries(
        {
                @NamedQuery(name = "couponByName", query = "SELECT cN FROM CouponEntity cN WHERE cN.couponName=:coupon_name"),
                @NamedQuery(name = "couponById", query = "SELECT cN FROM CouponEntity cN WHERE cN.uuid=:uuid")
        }
)
public class CouponEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @NotNull
    private String uuid;

    @Column(name = "coupon_name")
    @NotNull
    private String couponName;

    @Column(name = "percent")
    @NotNull
    private Integer percent;

    @OneToOne(mappedBy="coupon")
    private OrderEntity order;

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

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

}