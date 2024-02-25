package com.heuy.Security_test.repo;

import com.heuy.Security_test.user.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepo extends JpaRepository<Admin, Integer> {

    Optional<Admin> findByEmail(String email);
}
