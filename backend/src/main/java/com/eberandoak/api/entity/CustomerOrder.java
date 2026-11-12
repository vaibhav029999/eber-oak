package com.eberandoak.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name="orders")
public class CustomerOrder {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="user_id",nullable=false) private UserEntity user;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="restaurant_id",nullable=false) private Restaurant restaurant;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="address_id",nullable=false) private Address address;
    @OneToMany(mappedBy="order",cascade=CascadeType.ALL,orphanRemoval=true) private List<OrderItem> items=new ArrayList<>();
    @Column(name="customer_name",nullable=false,length=100) private String customerName;
    @Column(nullable=false,length=30) private String phone;
    @Column(name="delivery_address",nullable=false,length=420) private String deliveryAddress;
    @Column(name="subtotal",nullable=false,precision=10,scale=2) private BigDecimal subtotal;
    @Column(name="delivery_fee",nullable=false,precision=10,scale=2) private BigDecimal deliveryFee;
    @Column(name="discount_amount",nullable=false,precision=10,scale=2) private BigDecimal discountAmount=BigDecimal.ZERO;
    @Column(name="total_amount",nullable=false,precision=10,scale=2) private BigDecimal totalAmount;
    @Enumerated(EnumType.STRING) @Column(name="payment_method",nullable=false,length=30) private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=30) private OrderStatus status=OrderStatus.PLACED;
    @Column(name="coupon_code",length=40) private String couponCode;
    @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt=Instant.now();
    public CustomerOrder(){}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public UserEntity getUser(){return user;} public void setUser(UserEntity v){user=v;}
    public Restaurant getRestaurant(){return restaurant;} public void setRestaurant(Restaurant v){restaurant=v;}
    public Address getAddress(){return address;} public void setAddress(Address v){address=v;}
    public List<OrderItem> getItems(){return items;} public void setItems(List<OrderItem> v){items=v;}
    public String getCustomerName(){return customerName;} public void setCustomerName(String v){customerName=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public String getDeliveryAddress(){return deliveryAddress;} public void setDeliveryAddress(String v){deliveryAddress=v;}
    public BigDecimal getSubtotal(){return subtotal;} public void setSubtotal(BigDecimal v){subtotal=v;}
    public BigDecimal getDeliveryFee(){return deliveryFee;} public void setDeliveryFee(BigDecimal v){deliveryFee=v;}
    public BigDecimal getDiscountAmount(){return discountAmount;} public void setDiscountAmount(BigDecimal v){discountAmount=v;}
    public BigDecimal getTotalAmount(){return totalAmount;} public void setTotalAmount(BigDecimal v){totalAmount=v;}
    public PaymentMethod getPaymentMethod(){return paymentMethod;} public void setPaymentMethod(PaymentMethod v){paymentMethod=v;}
    public OrderStatus getStatus(){return status;} public void setStatus(OrderStatus v){status=v;}
    public String getCouponCode(){return couponCode;} public void setCouponCode(String v){couponCode=v;}
    public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant v){createdAt=v;}
}
