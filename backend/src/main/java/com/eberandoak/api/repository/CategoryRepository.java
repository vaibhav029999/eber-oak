package com.eberandoak.api.repository;
import com.eberandoak.api.entity.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findAllByOrderByNameAsc();
}
