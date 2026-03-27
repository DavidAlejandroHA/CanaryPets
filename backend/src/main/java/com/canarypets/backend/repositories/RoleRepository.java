package com.canarypets.backend.repositories;

import com.canarypets.backend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query(value = "SELECT * FROM `ROLES` WHERE name = :paramName",
    countQuery = "SELECT count(*) FROM `ROLES` WHERE name = :paramName",
    nativeQuery = true)
    Role findByName(@Param("paramName") String name);
}
