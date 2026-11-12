package com.eberandoak.api.repository;
import com.eberandoak.api.entity.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CartRepository extends JpaRepository<ShoppingCart,Long> {
    @EntityGraph(attributePaths={"items","items.foodItem","items.foodItem.restaurant","items.foodItem.category"})
    Optional<ShoppingCart> findByUser_Id(Long userId);
}
