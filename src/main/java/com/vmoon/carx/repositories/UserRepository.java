package com.vmoon.carx.repositories;

import com.vmoon.carx.entities.UserEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long>, JpaSpecificationExecutor<UserEntity> {

    @Query("SELECT u FROM users u " +
            "LEFT JOIN FETCH u.roles " +
            "WHERE u.username = :username")
    UserEntity findByUsername(@Param("username") String username);

    @Query("SELECT u FROM users u where u.isDeleted = false")
    @NonNull
    Page<UserEntity> findAll(@NonNull Pageable pageable);

    @Query("SELECT COUNT(u) FROM users u WHERE u.isDeleted = true")
    long countDeleted();

    @Query("SELECT COUNT(u) FROM users u WHERE u.isDeleted = false")
    long count();

    @Query("SELECT u FROM users u " +
            "LEFT JOIN FETCH u.roles" +
            " WHERE u.isDeleted = true")
    @NonNull
    Page<UserEntity> findAllDeleted(@NonNull Pageable pageable);
}

