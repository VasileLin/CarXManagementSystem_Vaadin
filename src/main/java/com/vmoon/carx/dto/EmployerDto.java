package com.vmoon.carx.dto;

import com.vmoon.carx.entities.Role;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployerDto {
    private Long id;
    @NotEmpty(message = "Full name must not be empty")
    private String fullName;
    private String phone;
    private LocalDate dateOfBirth;
    private String address;
    @Email(message = "Write an valid email address")
    private String email;
    private RoleDto role;

}
