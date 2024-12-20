package com.lescours.tpjdrspringapi.model;

public record InventoryObjectRecord(String name, int strength, int intelligence, int luck) {

    public InventoryObjectRecord {
        if (strength < 0 || intelligence < 0 || luck < 0) {
            throw new IllegalArgumentException("Stats must be positive");
        }
    }

}
