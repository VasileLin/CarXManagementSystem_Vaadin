package com.vmoon.carx.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Input date of birth")
    private LocalDate dateOfBirth;
    @NotEmpty(message = "Address must not be empty")
    private String address;
    @Email(message = "Write an valid email address")
    @NotEmpty(message = "Write an valid email address")
    private String email;
    @NotNull(message = "Chose role of employer")
    private RoleDto role;
    private boolean isDeleted;

}
