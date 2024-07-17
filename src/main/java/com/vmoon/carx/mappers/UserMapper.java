package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.UserDto;
import com.vmoon.carx.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity mapToUser(UserDto userDto);

    UserDto mapToUserDto(UserEntity userEntity);

}
