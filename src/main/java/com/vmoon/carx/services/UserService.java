package com.vmoon.carx.services;

import com.vmoon.carx.dto.UserDto;
import com.vmoon.carx.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface UserService {

    UserEntity update(UserDto entity);

    void updateSelf(UserDto entity);

    UserEntity add(UserDto entity);

    Page<UserDto> list(Pageable pageable);

    Page<UserDto> listDeleted(Pageable pageable);

    Page<UserDto> list(Pageable pageable, Specification<UserEntity> filter);

    int count();

    int countDeleted();

    UserEntity findByUsername(String username);}

