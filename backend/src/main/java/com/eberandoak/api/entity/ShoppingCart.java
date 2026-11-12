package com.eberandoak.api.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name="carts")
public class ShoppingCart {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @OneToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="user_id",nullable=false,unique=true) private UserEntity user;
    @OneToMany(mappedBy="cart",cascade=CascadeType.ALL,orphanRemoval=true) private List<CartItem> items=new ArrayList<>();
    @Column(name="updated_at",nullable=false) private Instant updatedAt=Instant.now();
    public ShoppingCart(){}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public UserEntity getUser(){return user;} public void setUser(UserEntity v){user=v;}
    public List<CartItem> getItems(){return items;} public void setItems(List<CartItem> v){items=v;}
    public Instant getUpdatedAt(){return updatedAt;} public void setUpdatedAt(Instant v){updatedAt=v;}
}
