package com.lescours.tpjdrspringapi.model;

public record CharacterRegisterRecord(String username, ClassTypeEnum classType) {
    public CharacterRegisterRecord {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (classType == null) {
            throw new IllegalArgumentException("Class type cannot be null");
        }
    }
}
