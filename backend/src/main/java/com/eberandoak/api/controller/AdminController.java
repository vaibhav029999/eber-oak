package com.eberandoak.api.controller;

import com.eberandoak.api.dto.*;
import com.eberandoak.api.service.CatalogService;
import com.eberandoak.api.service.CouponService;
import com.eberandoak.api.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/admin")
public class AdminController {
    private final CatalogService catalog;private final OrderService orders;private final CouponService coupons;
    public AdminController(CatalogService catalog,OrderService orders,CouponService coupons){this.catalog=catalog;this.orders=orders;this.coupons=coupons;}
    @GetMapping("/restaurants") public List<RestaurantView> restaurants(){return catalog.adminRestaurants();}
    @PostMapping("/restaurants") @ResponseStatus(HttpStatus.CREATED) public RestaurantView addRestaurant(@Valid @RequestBody RestaurantRequest body){return catalog.createRestaurant(body);}
    @PutMapping("/restaurants/{id}") public RestaurantView editRestaurant(@PathVariable Long id,@Valid @RequestBody RestaurantRequest body){return catalog.updateRestaurant(id,body);}
    @DeleteMapping("/restaurants/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void deleteRestaurant(@PathVariable Long id){catalog.archiveRestaurant(id);}
    @GetMapping("/restaurants/{id}/foods") public List<FoodView> foods(@PathVariable Long id){return catalog.adminFoods(id);}
    @GetMapping("/foods") public List<FoodView> allFoods(){return catalog.adminFoods(null);}
    @PostMapping("/foods") @ResponseStatus(HttpStatus.CREATED) public FoodView addFood(@Valid @RequestBody FoodRequest body){return catalog.createFood(body);}
    @PutMapping("/foods/{id}") public FoodView editFood(@PathVariable Long id,@Valid @RequestBody FoodRequest body){return catalog.updateFood(id,body);}
    @DeleteMapping("/foods/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void deleteFood(@PathVariable Long id){catalog.archiveFood(id);}
    @GetMapping("/orders") public List<OrderView> orders(){return orders.allOrders();}
    @PatchMapping("/orders/{id}/status") public OrderView updateOrder(@PathVariable Long id,@Valid @RequestBody OrderStatusRequest body){return orders.updateStatus(id,body.status());}
    @GetMapping("/customers") public List<CustomerAdminView> customers(){return orders.customers();}
    @GetMapping("/coupons") public List<CouponView> coupons(){return coupons.allCoupons();}
    @PostMapping("/coupons") @ResponseStatus(HttpStatus.CREATED) public CouponView addCoupon(@Valid @RequestBody CouponRequest body){return coupons.create(body);}
    @PutMapping("/coupons/{id}") public CouponView editCoupon(@PathVariable Long id,@Valid @RequestBody CouponRequest body){return coupons.update(id,body);}
    @DeleteMapping("/coupons/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void deleteCoupon(@PathVariable Long id){coupons.delete(id);}
}
