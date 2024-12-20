package com.lescours.tpjdrspringapi.service;

import com.lescours.tpjdrspringapi.model.Characteristics;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterAttackServiceTests {

    @Mock
    private CharacterService characterService;

    @InjectMocks
    private CharacterAttackService characterAttackService;

    @Test
    void testCalculDeLAttaqueDuJoueur() {
        // Arrange
        String username = "utilisateur";
        Characteristics characteristics = new Characteristics(10, 5, 3);

        when(characterService.getCharacteristicsByUsername(username))
                .thenReturn(characteristics);

        // Act
        int attack = characterAttackService.getCharacterAttack(username);

        // Assert
        assertThat(attack).isEqualTo(15);
    }
}