package com.eberandoak.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity @Table(name="restaurants")
public class Restaurant {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,length=120) private String name;
    @Column(nullable=false,length=100) private String cuisine;
    @Column(length=500) private String description;
    @Column(name="image_url",length=1000) private String imageUrl;
    @Column(precision=2,scale=1) private BigDecimal rating=BigDecimal.valueOf(4.5);
    @Column(name="delivery_minutes",nullable=false) private Integer deliveryMinutes=35;
    @Column(name="delivery_fee",nullable=false,precision=10,scale=2) private BigDecimal deliveryFee=BigDecimal.ZERO;
    @Column(name="minimum_order",nullable=false,precision=10,scale=2) private BigDecimal minimumOrder=BigDecimal.ZERO;
    @Column(name="price_level",length=10) private String priceLevel="₹₹";
    @Column(length=100) private String offer;
    @Column(nullable=false) private boolean active=true;
    @Column(nullable=false) private boolean featured=false;
    public Restaurant() {}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getCuisine(){return cuisine;} public void setCuisine(String v){cuisine=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public String getImageUrl(){return imageUrl;} public void setImageUrl(String v){imageUrl=v;}
    public BigDecimal getRating(){return rating;} public void setRating(BigDecimal v){rating=v;}
    public Integer getDeliveryMinutes(){return deliveryMinutes;} public void setDeliveryMinutes(Integer v){deliveryMinutes=v;}
    public BigDecimal getDeliveryFee(){return deliveryFee;} public void setDeliveryFee(BigDecimal v){deliveryFee=v;}
    public BigDecimal getMinimumOrder(){return minimumOrder;} public void setMinimumOrder(BigDecimal v){minimumOrder=v;}
    public String getPriceLevel(){return priceLevel;} public void setPriceLevel(String v){priceLevel=v;}
    public String getOffer(){return offer;} public void setOffer(String v){offer=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
    public boolean isFeatured(){return featured;} public void setFeatured(boolean v){featured=v;}
}
