package com.canarypets.backend.repositories;

import com.canarypets.backend.models.Product;
import com.canarypets.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM `USERS` WHERE nickName = :paramName",
    countQuery = "SELECT count(*) FROM `USERS` WHERE nickName = :paramName",
    nativeQuery = true)
    User findByNickName(@Param("paramName") String name);

    @Query(value = "SELECT * FROM `USERS` WHERE email = :paramName",
    countQuery = "SELECT count(*) FROM `USERS` WHERE email = :paramName",
    nativeQuery = true)
    User findByEmail(@Param("paramName") String email);

    //Optional<User> findByNickName(String nickName);
    boolean existsByNickName(String nickName);

    @Query("SELECT u.favorites FROM User u WHERE u.id = :userId")
    Set<Product> findFavoritesByUserId(Long userId);
}
