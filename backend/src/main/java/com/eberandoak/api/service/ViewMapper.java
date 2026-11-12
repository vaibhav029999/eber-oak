package com.eberandoak.api.service;

import com.eberandoak.api.dto.*;
import com.eberandoak.api.entity.*;
import java.math.BigDecimal;
import java.util.List;

public final class ViewMapper {
    private ViewMapper() {}
    public static UserView user(UserEntity u){return new UserView(u.getId(),u.getName(),u.getEmail(),u.getPhone(),u.getRole(),u.getCreatedAt());}
    public static RestaurantView restaurant(Restaurant r){return new RestaurantView(r.getId(),r.getName(),r.getCuisine(),r.getDescription(),r.getImageUrl(),r.getRating(),r.getDeliveryMinutes(),r.getDeliveryFee(),r.getMinimumOrder(),r.getPriceLevel(),r.getOffer(),r.isFeatured(),r.isActive());}
    public static CategoryView category(Category c){return new CategoryView(c.getId(),c.getName(),c.getSlug(),c.getImageUrl());}
    public static FoodView food(FoodItem f){return new FoodView(f.getId(),f.getRestaurant().getId(),f.getRestaurant().getName(),f.getCategory().getId(),f.getCategory().getName(),f.getName(),f.getDescription(),f.getImageUrl(),f.getPrice(),f.isVegetarian(),f.isSpicy(),f.isAvailable());}
    public static CartView cart(ShoppingCart c){
        List<CartItemView> items=c.getItems().stream().map(i->{FoodItem f=i.getFoodItem();return new CartItemView(i.getId(),f.getId(),f.getRestaurant().getId(),f.getRestaurant().getName(),f.getCategory().getName(),f.getName(),f.getDescription(),f.getImageUrl(),f.getPrice(),i.getQuantity(),f.isVegetarian());}).toList();
        BigDecimal subtotal=items.stream().map(i->i.price().multiply(BigDecimal.valueOf(i.quantity()))).reduce(BigDecimal.ZERO,BigDecimal::add);
        Restaurant r=c.getItems().isEmpty()?null:c.getItems().get(0).getFoodItem().getRestaurant();
        return new CartView(items,subtotal,r==null?BigDecimal.ZERO:r.getDeliveryFee(),BigDecimal.ZERO,r==null?null:r.getId(),r==null?null:r.getName());
    }
    public static CouponView coupon(Coupon c){return new CouponView(c.getId(),c.getCode(),c.getDiscountPercent(),c.getMinimumAmount(),c.isActive());}
    public static OrderView order(CustomerOrder o){
        List<OrderItemView> items=o.getItems().stream().map(i->new OrderItemView(i.getId(),i.getFoodName(),i.getPrice(),i.getQuantity())).toList();
        return new OrderView(o.getId(),o.getRestaurant().getName(),o.getCustomerName(),o.getPhone(),o.getDeliveryAddress(),o.getSubtotal(),o.getDeliveryFee(),o.getDiscountAmount(),o.getTotalAmount(),o.getPaymentMethod(),o.getStatus(),o.getCouponCode(),o.getCreatedAt(),o.getRestaurant().getDeliveryMinutes(),items);
    }
}
