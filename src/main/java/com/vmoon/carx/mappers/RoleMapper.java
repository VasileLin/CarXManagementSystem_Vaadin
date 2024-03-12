package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.RoleDto;
import com.vmoon.carx.entities.Role;
import org.springframework.stereotype.Service;

@Service
public class RoleMapper {
    public static RoleDto mapToRoleDto(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }


    public static Role mapToRole(RoleDto roleDto) {
        return Role.builder()
                .id(roleDto.getId())
                .name(roleDto.getName())
                .build();
    }
}
