package com.eberandoak.api.repository;
import com.eberandoak.api.entity.Role;
import com.eberandoak.api.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    List<UserEntity> findAllByRoleOrderByCreatedAtDesc(Role role);
}
