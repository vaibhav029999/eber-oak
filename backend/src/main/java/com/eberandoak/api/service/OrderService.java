package com.eberandoak.api.service;

import com.eberandoak.api.dto.*;
import com.eberandoak.api.entity.*;
import com.eberandoak.api.exception.ApiException;
import com.eberandoak.api.repository.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orders; private final CartService carts; private final CartRepository cartRepository;
    private final AddressRepository addresses; private final UserRepository users; private final CouponRepository coupons;
    public OrderService(OrderRepository orders,CartService carts,CartRepository cartRepository,AddressRepository addresses,UserRepository users,CouponRepository coupons){this.orders=orders;this.carts=carts;this.cartRepository=cartRepository;this.addresses=addresses;this.users=users;this.coupons=coupons;}
    @Transactional public OrderView place(String email,CheckoutRequest request){
        UserEntity user=users.findByEmailIgnoreCase(email).orElseThrow(()->ApiException.notFound("Account not found."));ShoppingCart cart=carts.cartForCheckout(email);
        if(cart.getItems().isEmpty())throw ApiException.badRequest("Your bag is empty. Add something lovely before checking out.");
        Restaurant restaurant=cart.getItems().get(0).getFoodItem().getRestaurant();BigDecimal subtotal=cart.getItems().stream().map(i->i.getFoodItem().getPrice().multiply(BigDecimal.valueOf(i.getQuantity()))).reduce(BigDecimal.ZERO,BigDecimal::add);
        if(subtotal.compareTo(restaurant.getMinimumOrder())<0)throw ApiException.badRequest("This kitchen has a minimum order of ₹"+restaurant.getMinimumOrder().stripTrailingZeros().toPlainString()+".");
        BigDecimal discount=BigDecimal.ZERO;String couponCode=null;
        if(request.couponCode()!=null&&!request.couponCode().isBlank()){
            Coupon coupon=coupons.findByCodeIgnoreCaseAndActiveTrue(request.couponCode().trim()).orElseThrow(()->ApiException.badRequest("That offer is no longer available."));
            if(subtotal.compareTo(coupon.getMinimumAmount())<0)throw ApiException.badRequest("Your order no longer meets the offer’s minimum amount.");
            discount=subtotal.multiply(BigDecimal.valueOf(coupon.getDiscountPercent())).divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP);couponCode=coupon.getCode();
        }
        Address address=addresses.findByUser_IdAndAddressLineAndCityAndPincode(user.getId(),request.address().trim(),request.city().trim(),request.pincode().trim()).orElseGet(Address::new);
        address.setUser(user);address.setCustomerName(request.customerName().trim());address.setPhone(request.phone().trim());address.setAddressLine(request.address().trim());address.setCity(request.city().trim());address.setPincode(request.pincode().trim());addresses.save(address);
        CustomerOrder order=new CustomerOrder();order.setUser(user);order.setRestaurant(restaurant);order.setAddress(address);order.setCustomerName(request.customerName().trim());order.setPhone(request.phone().trim());order.setDeliveryAddress(address.getFullAddress());order.setSubtotal(subtotal);order.setDeliveryFee(restaurant.getDeliveryFee());order.setDiscountAmount(discount);order.setTotalAmount(subtotal.add(restaurant.getDeliveryFee()).subtract(discount).max(BigDecimal.ZERO));order.setPaymentMethod(request.paymentMethod());order.setStatus(OrderStatus.PLACED);order.setCouponCode(couponCode);order.setCreatedAt(Instant.now());
        for(CartItem cartItem:cart.getItems()){OrderItem item=new OrderItem();item.setOrder(order);item.setFoodItem(cartItem.getFoodItem());item.setFoodName(cartItem.getFoodItem().getName());item.setPrice(cartItem.getFoodItem().getPrice());item.setQuantity(cartItem.getQuantity());order.getItems().add(item);}
        orders.save(order);carts.clear(cart);return ViewMapper.order(order);
    }
    @Transactional(readOnly=true) public List<OrderView> userOrders(String email){Long uid=users.findByEmailIgnoreCase(email).orElseThrow(()->ApiException.notFound("Account not found.")).getId();return orders.findAllByUser_IdOrderByCreatedAtDesc(uid).stream().map(ViewMapper::order).toList();}
    @Transactional(readOnly=true) public OrderView userOrder(String email,Long id){Long uid=users.findByEmailIgnoreCase(email).orElseThrow(()->ApiException.notFound("Account not found.")).getId();return orders.findByIdAndUser_Id(id,uid).map(ViewMapper::order).orElseThrow(()->ApiException.notFound("Order not found."));}
    @Transactional(readOnly=true) public List<OrderView> allOrders(){return orders.findAllByOrderByCreatedAtDesc().stream().map(ViewMapper::order).toList();}
    @Transactional public OrderView updateStatus(Long id,OrderStatus status){CustomerOrder order=orders.findById(id).orElseThrow(()->ApiException.notFound("Order not found."));order.setStatus(status);return ViewMapper.order(orders.save(order));}
    @Transactional(readOnly=true) public List<CustomerAdminView> customers(){return users.findAllByRoleOrderByCreatedAtDesc(Role.USER).stream().map(u->new CustomerAdminView(u.getId(),u.getName(),u.getEmail(),u.getPhone(),u.getCreatedAt(),orders.countByUser_Id(u.getId()))).toList();}
}
