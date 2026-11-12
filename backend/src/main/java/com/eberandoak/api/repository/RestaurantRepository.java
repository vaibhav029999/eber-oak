package com.eberandoak.api.repository;
import com.eberandoak.api.entity.Restaurant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    List<Restaurant> findAllByActiveTrueOrderByFeaturedDescRatingDesc();
    List<Restaurant> findAllByOrderByNameAsc();
}
