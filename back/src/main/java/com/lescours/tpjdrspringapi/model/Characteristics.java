package com.lescours.tpjdrspringapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "characteristics")
@Getter
@Setter
@NoArgsConstructor
public class Characteristics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "strength", nullable = false)
    private int strength;

    @Column(name = "intelligence", nullable = false)
    private int intelligence;

    @Column(name = "luck", nullable = false)
    private int luck;

    public Characteristics(int strength, int intelligence, int luck) {
        this.strength = strength;
        this.intelligence = intelligence;
        this.luck = luck;
    }

}
