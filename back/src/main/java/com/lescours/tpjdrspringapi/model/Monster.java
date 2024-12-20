package com.lescours.tpjdrspringapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "monster")
@Getter
@Setter
@NoArgsConstructor
public class Monster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false)
    @Enumerated(EnumType.STRING)
    private MonsterTypeEnum type;

    @Column(name = "health", nullable = false)
    private Integer health;

    @ManyToOne
    @JoinColumn(name = "dropped_object")
    private InventoryObject droppedObject;

    public Monster(MonsterTypeEnum type, InventoryObject droppedObject) {
        this.type = type;
        this.droppedObject = droppedObject;
        this.health = 100;
    }

}
