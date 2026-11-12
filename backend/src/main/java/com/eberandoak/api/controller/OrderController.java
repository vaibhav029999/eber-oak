package com.eberandoak.api.controller;

import com.eberandoak.api.dto.*;
import com.eberandoak.api.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orders;
    public OrderController(OrderService orders){this.orders=orders;}
    @PostMapping public OrderView place(@AuthenticationPrincipal UserDetails user,@Valid @RequestBody CheckoutRequest body){return orders.place(user.getUsername(),body);}
    @GetMapping public List<OrderView> mine(@AuthenticationPrincipal UserDetails user){return orders.userOrders(user.getUsername());}
    @GetMapping("/{id}") public OrderView one(@AuthenticationPrincipal UserDetails user,@PathVariable Long id){return orders.userOrder(user.getUsername(),id);}
}
