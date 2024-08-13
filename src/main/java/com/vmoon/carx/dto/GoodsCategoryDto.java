package com.vmoon.carx.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsCategoryDto {

    private int id;
    private String name;
    private Boolean isDeleted;

    @Override
    public String toString() {
        return "GoodsCategoryDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
