package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name= "payment" , schema = "public")
@NamedQueries(
        {
                @NamedQuery(name= "paymentMethods", query = "SELECT p FROM PaymentEntity p" )
        }
)
public class PaymentEntity implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "uuid")
    @NotNull
    @Size(max=200)
    private String uuid;

    @Column(name= "payment_name")
    @NotNull
    @Size(max=255)
    private String paymentName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPayment() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }
}
