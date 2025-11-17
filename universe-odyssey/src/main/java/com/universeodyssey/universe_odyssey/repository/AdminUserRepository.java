package com.universeodyssey.universe_odyssey.repository;

import com.universeodyssey.universe_odyssey.model.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    AdminUser findByUsername(String username);
}
