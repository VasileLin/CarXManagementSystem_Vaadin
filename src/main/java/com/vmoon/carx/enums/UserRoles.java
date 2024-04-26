package com.vmoon.carx.enums;

import lombok.Getter;

@Getter
public enum UserRoles {
    CASHIER("CASHIER"),
    MANAGER("MANAGER"),
    ADMINISTRATOR("ADMIN");

    private final String value;

    UserRoles(String value) {
        this.value = value;
    }
}
