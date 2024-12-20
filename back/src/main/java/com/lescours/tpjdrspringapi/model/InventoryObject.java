package com.lescours.tpjdrspringapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inventory_object")
@Getter
@Setter
@NoArgsConstructor
public class InventoryObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inventory_id", nullable = true)
    private Inventory inventory;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "characteristics_id")
    private Characteristics characteristics;

    public InventoryObject(Inventory inventory, String name, Characteristics characteristics) {
        this.inventory = inventory;
        this.name = name;
        this.characteristics = characteristics;
    }

    public InventoryObject(String name, Characteristics characteristics) {
        this.name = name;
        this.characteristics = characteristics;
    }

}
