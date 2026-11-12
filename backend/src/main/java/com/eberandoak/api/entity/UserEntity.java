package com.eberandoak.api.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name="users", uniqueConstraints=@UniqueConstraint(name="uk_users_email", columnNames="email"))
public class UserEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, length=100) private String name;
    @Column(nullable=false, length=190) private String email;
    @Column(name="password_hash", nullable=false, length=100) private String passwordHash;
    @Column(length=30) private String phone;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=15) private Role role=Role.USER;
    @Column(name="created_at", nullable=false, updatable=false) private Instant createdAt=Instant.now();
    public UserEntity() {}
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getName(){return name;} public void setName(String name){this.name=name;}
    public String getEmail(){return email;} public void setEmail(String email){this.email=email;}
    public String getPasswordHash(){return passwordHash;} public void setPasswordHash(String value){this.passwordHash=value;}
    public String getPhone(){return phone;} public void setPhone(String phone){this.phone=phone;}
    public Role getRole(){return role;} public void setRole(Role role){this.role=role;}
    public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant value){this.createdAt=value;}
}
