package com.eberandoak.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity @Table(name="order_items")
public class OrderItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="order_id",nullable=false) private CustomerOrder order;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="food_item_id") private FoodItem foodItem;
    @Column(name="food_name",nullable=false,length=120) private String foodName;
    @Column(nullable=false,precision=10,scale=2) private BigDecimal price;
    @Column(nullable=false) private Integer quantity;
    public OrderItem(){}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public CustomerOrder getOrder(){return order;} public void setOrder(CustomerOrder v){order=v;}
    public FoodItem getFoodItem(){return foodItem;} public void setFoodItem(FoodItem v){foodItem=v;}
    public String getFoodName(){return foodName;} public void setFoodName(String v){foodName=v;}
    public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal v){price=v;}
    public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
}
