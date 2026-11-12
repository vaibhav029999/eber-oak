package com.eberandoak.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity @Table(name="coupons",uniqueConstraints=@UniqueConstraint(name="uk_coupons_code",columnNames="code"))
public class Coupon {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,length=40) private String code;
    @Column(name="discount_percent",nullable=false) private Integer discountPercent;
    @Column(name="minimum_amount",nullable=false,precision=10,scale=2) private BigDecimal minimumAmount=BigDecimal.ZERO;
    @Column(nullable=false) private boolean active=true;
    public Coupon(){}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getCode(){return code;} public void setCode(String v){code=v;}
    public Integer getDiscountPercent(){return discountPercent;} public void setDiscountPercent(Integer v){discountPercent=v;}
    public BigDecimal getMinimumAmount(){return minimumAmount;} public void setMinimumAmount(BigDecimal v){minimumAmount=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
