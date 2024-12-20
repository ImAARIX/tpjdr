package com.lescours.tpjdrspringapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Random;

@Entity
@Table(name = "characters")
@Getter
@Setter
@NoArgsConstructor
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "classType", nullable = false)
    @Enumerated(EnumType.STRING)
    private ClassTypeEnum classType;

    @ManyToOne
    @JoinColumn(name = "inventory", nullable = false)
    private Inventory inventory;

    @Column(name = "life", nullable = false)
    private Integer health;

    public Character(String username, ClassTypeEnum classType, Inventory inventory) {
        this.username = username;
        this.classType = classType;
        this.inventory = inventory;
        this.health = 100;
    }

}
