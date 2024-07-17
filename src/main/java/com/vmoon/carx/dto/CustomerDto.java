package com.vmoon.carx.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
    private long id;
    @NotEmpty(message = "Enter costumer name!")
    @NotBlank(message = "Enter costumer name!")
    private String name;
    private String phone;
    @NotEmpty(message = "Enter costumer car number!")
    private String carNumber;
    private CarModelDto carModel;
    @Email(message = "Enter an valid email")
    @NotEmpty(message = "Enter an valid email")
    private String email;
    private CarBrandDto carBrand;
    private Boolean isDeleted;
}
