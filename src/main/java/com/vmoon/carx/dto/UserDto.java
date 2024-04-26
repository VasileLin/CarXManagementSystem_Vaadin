package com.vmoon.carx.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private int id;
    @NotEmpty
    private String username;
    private String password;
    @NotNull
    private Set<RoleDto> roles;
}
