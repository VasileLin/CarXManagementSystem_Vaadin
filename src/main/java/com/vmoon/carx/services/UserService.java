package com.vmoon.carx.services;

import com.vmoon.carx.dto.UserDto;
import com.vmoon.carx.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface UserService {

    UserDto getById(Long id);

    UserEntity update(UserDto entity);

    UserEntity add(UserDto entity);
    void delete(Long id);
    Page<UserDto> list(Pageable pageable);

    Page<UserDto> list(Pageable pageable, Specification<UserEntity> filter);
    int count();

    UserEntity findByUsername(String username);

    boolean existsByUsername(String value);
}

