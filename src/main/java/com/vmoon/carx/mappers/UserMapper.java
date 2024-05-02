package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.UserDto;
import com.vmoon.carx.entities.UserEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public static UserEntity mapToUser(UserDto userDto){

        return UserEntity.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .roles(userDto.getRoles().stream().map(RoleMapper::mapToRole).collect(Collectors.toSet()))
                .isDeleted(userDto.isDeleted())
                .build();
    }

    public static UserDto mapToUserDto(UserEntity userEntity){

        return UserDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getRoles().stream().map(RoleMapper::mapToRoleDto).collect(Collectors.toSet()))
                .isDeleted(userEntity.isDeleted())
                .build();
    }

}
