package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.RoleDto;
import com.vmoon.carx.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleDto mapToRoleDto(Role role);

    Role mapToRole(RoleDto roleDto);
}
