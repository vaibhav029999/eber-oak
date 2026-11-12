package com.eberandoak.api.entity;

import jakarta.persistence.*;

@Entity @Table(name="cart_items",uniqueConstraints=@UniqueConstraint(name="uk_cart_food",columnNames={"cart_id","food_item_id"}))
public class CartItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="cart_id",nullable=false) private ShoppingCart cart;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="food_item_id",nullable=false) private FoodItem foodItem;
    @Column(nullable=false) private Integer quantity=1;
    public CartItem(){}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public ShoppingCart getCart(){return cart;} public void setCart(ShoppingCart v){cart=v;}
    public FoodItem getFoodItem(){return foodItem;} public void setFoodItem(FoodItem v){foodItem=v;}
    public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
}
