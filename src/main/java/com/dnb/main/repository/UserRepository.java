// src/main/java/com/dnb/main/repository/UserRepository.java
package com.dnb.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dnb.main.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);


    Boolean existsByEmployeeId(String employeeId);



// In UserRepository.java
@Query("SELECT u FROM User u WHERE u.role = 'ADMIN'")
List<User> findAllAdmins();

    }