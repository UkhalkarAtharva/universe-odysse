package com.universeodyssey.universe_odyssey.service;

import com.universeodyssey.universe_odyssey.model.AdminUser;
import com.universeodyssey.universe_odyssey.repository.AdminUserRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminUserService {

    private final AdminUserRepository userRepository;

    public AdminUserService(AdminUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AdminUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
