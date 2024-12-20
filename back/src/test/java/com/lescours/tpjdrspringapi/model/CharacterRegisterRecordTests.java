package com.lescours.tpjdrspringapi.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CharacterRegisterRecordTests {

    @Test
    void testCreerEnregistrementQuandNomUtilisateurEtTypeClasseValides() {
        // Arrange
        String username = "utilisateur";
        ClassTypeEnum classType = ClassTypeEnum.WARRIOR;

        // Act
        CharacterRegisterRecord record = new CharacterRegisterRecord(username, classType);

        // Assert
        assertEquals(username, record.username());
        assertEquals(classType, record.classType());
    }

    @Test
    void testLancerExceptionQuandNomUtilisateurEstNull() {
        // Arrange
        String username = null;
        ClassTypeEnum classType = ClassTypeEnum.WARRIOR;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new CharacterRegisterRecord(username, classType);
        });
        assertEquals("Username cannot be null or blank", exception.getMessage());
    }

    @Test
    void testLancerExceptionQuandNomUtilisateurEstVide() {
        // Arrange
        String username = " ";
        ClassTypeEnum classType = ClassTypeEnum.WARRIOR;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new CharacterRegisterRecord(username, classType);
        });
        assertEquals("Username cannot be null or blank", exception.getMessage());
    }

    @Test
    void testLancerExceptionQuandTypeClasseEstNull() {
        // Arrange
        String username = "utilisateur";
        ClassTypeEnum classTypeEnum = null;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new CharacterRegisterRecord(username, classTypeEnum);
        });
        assertEquals("Class type cannot be null", exception.getMessage());
    }
}