package com.eberandoak.api.repository;
import com.eberandoak.api.entity.FoodItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FoodItemRepository extends JpaRepository<FoodItem,Long> {
    List<FoodItem> findByRestaurant_IdAndAvailableTrueOrderByCategory_NameAscNameAsc(Long restaurantId);
    List<FoodItem> findByRestaurant_IdOrderByNameAsc(Long restaurantId);
}
