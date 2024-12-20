package com.lescours.tpjdrspringapi.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

class InventoryObjectRecordTests {

    @Test
    void testCreerEnregistrementAvecDonneesValides() {
        // Arrange
        String nom = "Épée";
        int force = 10;
        int intelligence = 5;
        int chance = 3;

        // Act
        InventoryObjectRecord record = new InventoryObjectRecord(nom, force, intelligence, chance);

        // Assert
        assertThat(record.name()).isEqualTo(nom);
        assertThat(record.strength()).isEqualTo(force);
        assertThat(record.intelligence()).isEqualTo(intelligence);
        assertThat(record.luck()).isEqualTo(chance);
    }

    @Test
    void testLancerExceptionQuandForceEstNegative() {
        // Arrange
        String nom = "Épée";
        int force = -1;
        int intelligence = 5;
        int chance = 3;

        // Act & Assert
        assertThatThrownBy(() -> new InventoryObjectRecord(nom, force, intelligence, chance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Stats must be positive");
    }

    @Test
    void testLancerExceptionQuandIntelligenceEstNegative() {
        // Arrange
        String nom = "Épée";
        int force = 10;
        int intelligence = -1;
        int chance = 3;

        // Act & Assert
        assertThatThrownBy(() -> new InventoryObjectRecord(nom, force, intelligence, chance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Stats must be positive");
    }

    @Test
    void testLancerExceptionQuandChanceEstNegative() {
        // Arrange
        String nom = "Épée";
        int force = 10;
        int intelligence = 5;
        int chance = -1;

        // Act & Assert
        assertThatThrownBy(() -> new InventoryObjectRecord(nom, force, intelligence, chance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Stats must be positive");
    }

}