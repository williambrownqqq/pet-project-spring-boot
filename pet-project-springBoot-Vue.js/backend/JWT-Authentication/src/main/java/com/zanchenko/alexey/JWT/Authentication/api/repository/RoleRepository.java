package com.zanchenko.alexey.JWT.Authentication.api.repository;

import com.zanchenko.alexey.JWT.Authentication.api.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
