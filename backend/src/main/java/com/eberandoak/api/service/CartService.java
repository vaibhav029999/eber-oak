package com.eberandoak.api.service;

import com.eberandoak.api.dto.*;
import com.eberandoak.api.entity.*;
import com.eberandoak.api.exception.ApiException;
import com.eberandoak.api.repository.*;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {
    private final CartRepository carts; private final FoodItemRepository foods; private final UserRepository users;
    public CartService(CartRepository carts,FoodItemRepository foods,UserRepository users){this.carts=carts;this.foods=foods;this.users=users;}
    @Transactional public CartView get(String email){return ViewMapper.cart(findOrCreate(email));}
    @Transactional public CartView add(String email,AddCartItemRequest request){
        ShoppingCart cart=findOrCreate(email);FoodItem food=foods.findById(request.foodItemId()).filter(FoodItem::isAvailable).orElseThrow(()->ApiException.notFound("That dish is no longer on the menu."));
        if(!cart.getItems().isEmpty()&&!cart.getItems().get(0).getFoodItem().getRestaurant().getId().equals(food.getRestaurant().getId()))throw ApiException.badRequest("A single order can come from one restaurant. Finish this bag before choosing another kitchen.");
        CartItem existing=cart.getItems().stream().filter(i->i.getFoodItem().getId().equals(food.getId())).findFirst().orElse(null);
        if(existing==null){existing=new CartItem();existing.setCart(cart);existing.setFoodItem(food);existing.setQuantity(request.quantity());cart.getItems().add(existing);}
        else {int next=existing.getQuantity()+request.quantity();if(next>99)throw ApiException.badRequest("You can add up to 99 of one dish.");existing.setQuantity(next);}
        cart.setUpdatedAt(Instant.now());carts.save(cart);return ViewMapper.cart(carts.findByUser_Id(cart.getUser().getId()).orElseThrow());
    }
    @Transactional public CartView update(String email,Long itemId,QuantityRequest request){ShoppingCart cart=findOrCreate(email);CartItem item=ownedItem(cart,itemId);item.setQuantity(request.quantity());cart.setUpdatedAt(Instant.now());return ViewMapper.cart(carts.save(cart));}
    @Transactional public CartView remove(String email,Long itemId){ShoppingCart cart=findOrCreate(email);CartItem item=ownedItem(cart,itemId);cart.getItems().remove(item);cart.setUpdatedAt(Instant.now());return ViewMapper.cart(carts.save(cart));}
    @Transactional public ShoppingCart cartForCheckout(String email){return findOrCreate(email);}
    @Transactional public void clear(ShoppingCart cart){cart.getItems().clear();cart.setUpdatedAt(Instant.now());carts.save(cart);}
    private CartItem ownedItem(ShoppingCart cart,Long id){return cart.getItems().stream().filter(i->i.getId().equals(id)).findFirst().orElseThrow(()->ApiException.notFound("That item isn’t in your bag."));}
    private ShoppingCart findOrCreate(String email){
        UserEntity user=users.findByEmailIgnoreCase(email).orElseThrow(()->ApiException.notFound("Account not found."));
        return carts.findByUser_Id(user.getId()).orElseGet(()->{ShoppingCart c=new ShoppingCart();c.setUser(user);return carts.save(c);});
    }
}
