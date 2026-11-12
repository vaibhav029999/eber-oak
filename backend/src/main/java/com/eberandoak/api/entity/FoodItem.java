package com.eberandoak.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity @Table(name="food_items")
public class FoodItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="restaurant_id",nullable=false) private Restaurant restaurant;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="category_id",nullable=false) private Category category;
    @Column(nullable=false,length=120) private String name;
    @Column(length=500) private String description;
    @Column(name="image_url",length=1000) private String imageUrl;
    @Column(nullable=false,precision=10,scale=2) private BigDecimal price;
    @Column(nullable=false) private boolean vegetarian=true;
    @Column(nullable=false) private boolean spicy=false;
    @Column(nullable=false) private boolean available=true;
    public FoodItem(){}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public Restaurant getRestaurant(){return restaurant;} public void setRestaurant(Restaurant v){restaurant=v;}
    public Category getCategory(){return category;} public void setCategory(Category v){category=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public String getImageUrl(){return imageUrl;} public void setImageUrl(String v){imageUrl=v;}
    public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal v){price=v;}
    public boolean isVegetarian(){return vegetarian;} public void setVegetarian(boolean v){vegetarian=v;}
    public boolean isSpicy(){return spicy;} public void setSpicy(boolean v){spicy=v;}
    public boolean isAvailable(){return available;} public void setAvailable(boolean v){available=v;}
}
