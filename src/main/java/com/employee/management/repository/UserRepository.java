package com.employee.management.repository;

import com.employee.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository for user data access
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}