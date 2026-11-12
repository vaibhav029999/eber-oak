package com.eberandoak.api.repository;
import com.eberandoak.api.entity.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    Optional<CartItem> findByIdAndCart_Id(Long id,Long cartId);
}
