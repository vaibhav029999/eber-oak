package com.eberandoak.api.controller;

import com.eberandoak.api.dto.*;
import com.eberandoak.api.service.CartService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/cart")
public class CartController {
    private final CartService carts;
    public CartController(CartService carts){this.carts=carts;}
    @GetMapping public CartView get(@AuthenticationPrincipal UserDetails user){return carts.get(user.getUsername());}
    @PostMapping("/items") public CartView add(@AuthenticationPrincipal UserDetails user,@Valid @RequestBody AddCartItemRequest body){return carts.add(user.getUsername(),body);}
    @PatchMapping("/items/{id}") public CartView update(@AuthenticationPrincipal UserDetails user,@PathVariable Long id,@Valid @RequestBody QuantityRequest body){return carts.update(user.getUsername(),id,body);}
    @DeleteMapping("/items/{id}") public CartView remove(@AuthenticationPrincipal UserDetails user,@PathVariable Long id){return carts.remove(user.getUsername(),id);}
}
