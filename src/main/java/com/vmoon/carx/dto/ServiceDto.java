package com.vmoon.carx.dto;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceDto {
    private Long id;
    private String name;
    private Double price;
    private Boolean isDeleted;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceDto that = (ServiceDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }
}


