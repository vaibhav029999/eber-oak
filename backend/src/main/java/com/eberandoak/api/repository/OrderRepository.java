package com.eberandoak.api.repository;
import com.eberandoak.api.entity.CustomerOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OrderRepository extends JpaRepository<CustomerOrder,Long> {
    @EntityGraph(attributePaths={"restaurant","items","address"})
    List<CustomerOrder> findAllByUser_IdOrderByCreatedAtDesc(Long userId);
    @EntityGraph(attributePaths={"restaurant","items","address","user"})
    Optional<CustomerOrder> findByIdAndUser_Id(Long id,Long userId);
    @EntityGraph(attributePaths={"restaurant","items","address","user"})
    List<CustomerOrder> findAllByOrderByCreatedAtDesc();
    long countByUser_Id(Long userId);
}
