package com.vmoon.carx.services.implementations;


import com.vmoon.carx.dto.UserDto;
import com.vmoon.carx.entities.UserEntity;
import com.vmoon.carx.mappers.RoleMapper;
import com.vmoon.carx.mappers.UserMapper;
import com.vmoon.carx.repositories.UserRepository;
import com.vmoon.carx.services.UserService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    public UserServiceImpl(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public @NonNull UserEntity add(@NonNull UserDto entity) {
        UserEntity user = new UserEntity();
        user.setUsername(entity.getUsername());
        user.setPassword(passwordEncoder.encode(entity.getPassword()));
        user.setRoles(entity.getRoles().stream().map(RoleMapper.INSTANCE::mapToRole).collect(Collectors.toSet()));

        return userRepository.save(user);
    }

    @Transactional
    public @NonNull UserEntity update(@NonNull UserDto entity) {
        return userRepository.save(UserMapper.INSTANCE.mapToUser(entity));
    }

    @Transactional
    public @NonNull Page<UserDto> list(@NonNull Pageable pageable) {
        return userRepository.findAll(pageable).map(UserMapper.INSTANCE::mapToUserDto);
    }

    @Override
    public Page<UserDto> listDeleted(Pageable pageable) {
        return userRepository.findAllDeleted(pageable).map(UserMapper.INSTANCE::mapToUserDto);
    }

    @Transactional
    public @NonNull Page<UserDto> list(@NonNull Pageable pageable, @NonNull Specification<UserEntity> filter) {
        return userRepository.findAll(filter, pageable).map(UserMapper.INSTANCE::mapToUserDto);
    }

    public int count() {
        return (int) userRepository.count();
    }

    @Override
    public int countDeleted() {
        return (int) userRepository.countDeleted();
    }

    @Override
    @Transactional(readOnly = true)
    public @NonNull UserEntity findByUsername(@NonNull String username) {
        return userRepository.findByUsername(username);
    }

}
