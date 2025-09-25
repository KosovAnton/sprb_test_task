package com.spribe.enums;

public enum Role {
    SUPERVISOR("supervisor"),
    ADMIN("admin"),
    USER("user");

    Role(String role) {
        this.role = role;
    }

    private final String role;

    public String getRole() {
        return role;
    }
}
