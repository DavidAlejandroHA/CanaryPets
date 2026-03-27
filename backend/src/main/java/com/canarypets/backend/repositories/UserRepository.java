package com.canarypets.backend.repositories;

import com.canarypets.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT * FROM `USERS` WHERE nickName = :paramName",
    countQuery = "SELECT count(*) FROM `USERS` WHERE nickName = :paramName",
    nativeQuery = true)
    User findByNickName(@Param("paramName") String name);

    @Query(value = "SELECT * FROM `USERS` WHERE email = :paramName",
    countQuery = "SELECT count(*) FROM `USERS` WHERE email = :paramName",
    nativeQuery = true)
    User findByEmail(@Param("paramName") String email);
}
