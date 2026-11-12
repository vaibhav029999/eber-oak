package com.eberandoak.api.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name="addresses")
public class Address {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="user_id",nullable=false) private UserEntity user;
    @Column(name="customer_name",nullable=false,length=100) private String customerName;
    @Column(nullable=false,length=30) private String phone;
    @Column(name="address_line",nullable=false,length=255) private String addressLine;
    @Column(nullable=false,length=100) private String city;
    @Column(nullable=false,length=15) private String pincode;
    @Column(name="created_at",nullable=false) private Instant createdAt=Instant.now();
    public Address(){}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public UserEntity getUser(){return user;} public void setUser(UserEntity v){user=v;}
    public String getCustomerName(){return customerName;} public void setCustomerName(String v){customerName=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public String getAddressLine(){return addressLine;} public void setAddressLine(String v){addressLine=v;}
    public String getCity(){return city;} public void setCity(String v){city=v;}
    public String getPincode(){return pincode;} public void setPincode(String v){pincode=v;}
    public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant v){createdAt=v;}
    public String getFullAddress(){return addressLine+", "+city+" — "+pincode;}
}
