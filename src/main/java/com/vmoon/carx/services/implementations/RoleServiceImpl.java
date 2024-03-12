package com.vmoon.carx.services.implementations;

import com.vmoon.carx.dto.RoleDto;
import com.vmoon.carx.mappers.RoleMapper;
import com.vmoon.carx.repositories.RoleRepository;
import com.vmoon.carx.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    @Override
    public List<RoleDto> findAllRoles() {
        return roleRepository.findAll().stream().map(RoleMapper::mapToRoleDto).toList();
    }
}
