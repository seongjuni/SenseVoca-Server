package com.sensevoca.backend.repository;

import com.sensevoca.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsById(String user_id);
}