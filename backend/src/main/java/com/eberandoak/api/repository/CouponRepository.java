package com.eberandoak.api.repository;
import com.eberandoak.api.entity.Coupon;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CouponRepository extends JpaRepository<Coupon,Long> {
    Optional<Coupon> findByCodeIgnoreCaseAndActiveTrue(String code);
    Optional<Coupon> findByCodeIgnoreCase(String code);
    List<Coupon> findAllByActiveTrueOrderByCodeAsc();
    List<Coupon> findAllByOrderByCodeAsc();
}
