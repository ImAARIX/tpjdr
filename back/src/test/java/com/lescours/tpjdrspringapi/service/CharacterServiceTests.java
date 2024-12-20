package com.lescours.tpjdrspringapi.service;

import com.lescours.tpjdrspringapi.model.*;
import com.lescours.tpjdrspringapi.model.Character;
import com.lescours.tpjdrspringapi.repository.CharacterRepository;
import com.lescours.tpjdrspringapi.util.ConfigurationHelper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterServiceTests {

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private InventoryService inventoryService;

    @Mock
    private ConfigurationHelper configurationHelper;

    @Mock
    private MonsterAttackService monsterAttackService;

    @InjectMocks
    private CharacterService characterService;

    @Test
    void testCreerNouveauPersonnage() {
        // Arrange
        String username = "utilisateur";
        CharacterRegisterRecord record = new CharacterRegisterRecord(username, ClassTypeEnum.WARRIOR);
        Character character = new Character(username, ClassTypeEnum.WARRIOR, new Inventory());

        when(characterRepository.existsCharacterByUsername(username))
                .thenReturn(false);
        when(characterRepository.save(any(Character.class)))
                .thenReturn(character);

        // Act
        Character savedCharacter = characterService.save(record);

        // Assert
        assertThat(savedCharacter.getUsername())
                .isEqualTo(username);
        verify(characterRepository, times(1))
                .save(any(Character.class));
    }

    @Test
    void testSupprimerAncienPersonnageEtSauvegarderLeNouveau() {
        // Arrange
        String username = "utilisateur";

        CharacterRegisterRecord record = new CharacterRegisterRecord(username, ClassTypeEnum.WARRIOR);
        Character oldCharacter = new Character(username, ClassTypeEnum.MAGE, new Inventory());
        Character newCharacter = new Character(username, ClassTypeEnum.WARRIOR, new Inventory());

        when(characterRepository.existsCharacterByUsername(username))
                .thenReturn(true);
        when(characterRepository.findCharacterByUsername(username))
                .thenReturn(oldCharacter);
        when(characterRepository.save(any(Character.class)))
                .thenReturn(newCharacter);

        // Act
        Character savedCharacter = characterService.save(record);

        // Assert
        assertThat(savedCharacter.getClassType())
                .isEqualTo(ClassTypeEnum.WARRIOR);
        verify(characterRepository, times(1))
                .delete(oldCharacter);
        verify(characterRepository, times(1))
                .save(any(Character.class));
    }

    @Test
    void testDoisRetournerLePersonnageParNomDUtilisteur() {
        // Arrange
        String username = "utilisateur";
        Character character = new Character(username, ClassTypeEnum.WARRIOR, new Inventory());

        when(characterRepository.findCharacterByUsername(username))
                .thenReturn(character);

        // Act
        Character foundCharacter = characterService.getCharacterByUsername(username);

        // Assert
        assertThat(foundCharacter)
                .isEqualTo(character);
    }

    @Test
    void testDoisRetournerTrueSiLePersonnageExisteParNomDUtilisateur() {
        // Arrange
        String username = "username";
        when(characterRepository.existsCharacterByUsername(username))
                .thenReturn(true);

        // Act
        boolean exists = characterService.existsCharacterByUsername(username);

        // Assert
        assertThat(exists)
                .isTrue();
    }

    @Test
    void testDoisRetournerFalseSiPersonnageNExistePas() {
        // Arrange
        String username = "utilisateur";
        when(characterRepository.existsCharacterByUsername(username))
                .thenReturn(false);

        // Act
        boolean exists = characterService.existsCharacterByUsername(username);

        // Assert
        assertThat(exists)
                .isFalse();
    }

    @Test
    void testDoisSauvegarderLObjetDuPersonnage() {
        // Arrange
        String username = "utilisateur";

        Character character = new Character(username, ClassTypeEnum.WARRIOR, new Inventory());
        InventoryObjectRecord inventoryObjectRecord = new InventoryObjectRecord("Sword", 10, 5, 3);

        when(characterRepository.findCharacterByUsername(username))
                .thenReturn(character);

        // Act
        boolean result = characterService.saveObject(username, inventoryObjectRecord);

        // Assert
        assertThat(result)
                .isTrue();
        verify(inventoryService, times(1))
                .saveObject(character, inventoryObjectRecord);
    }

    @Test
    void testDoisRetournerLesCaracteristiquesDUnJoueur() {
        // Arrange
        String username = "utilisateur";
        Character character = new Character(username, ClassTypeEnum.WARRIOR, new Inventory());
        Characteristics characteristics = new Characteristics(10, 5, 3);

        when(characterRepository.findCharacterByUsername(username))
                .thenReturn(character);
        when(configurationHelper.getCharacteristics(ClassTypeEnum.WARRIOR))
                .thenReturn(characteristics);

        Characteristics result = characterService.getCharacteristicsByUsername(username);

        assertThat(result).isEqualTo(characteristics);
    }

    @Test
    void testNeDoisPasPerdreDeVieQuandLePersonnageADeLaLuck() {
        // Arrange
        CharacterService spyCharacterService = spy(characterService);

        String username = "utilisateur";
        ClassTypeEnum classType = ClassTypeEnum.WARRIOR;

        Character character = new Character(username, classType, new Inventory());

        doReturn(character)
                .when(spyCharacterService)
                .getCharacterByUsername(username);
        doReturn(true)
                .when(spyCharacterService)
                .hasLuck(username);

        // Act
        Character result = spyCharacterService.wasAttacked(username, MonsterTypeEnum.DEMON);

        // Assert
        assertThat(result).isEqualTo(character);
    }

    @Test
    void testDoisReduireLaVieDuPersonnageQuandAttaqueParUnMonstreEtPasDeLuck() {
        // Arrange
        CharacterService spyCharacterService = spy(characterService);

        String username = "utilisateur";
        Integer health = 100;
        Integer attack = 20;
        Integer expectedHealth = health - attack;

        Character character = new Character(username, ClassTypeEnum.WARRIOR, new Inventory());
        character.setHealth(health);

        doReturn(character)
                .when(spyCharacterService)
                .getCharacterByUsername(username);
        doReturn(false)
                .when(spyCharacterService)
                .hasLuck(username);
        doReturn(attack)
                .when(monsterAttackService)
                .getMonsterAttack(MonsterTypeEnum.DEMON);
        doReturn(character)
                .when(characterRepository)
                .save(character);

        // Act
        Character result = spyCharacterService.wasAttacked(username, MonsterTypeEnum.DEMON);

        // Assert
        assertThat(result.getHealth()).isEqualTo(expectedHealth);
    }

    @Test
    void testDoisRetournerTrueQuandLaLuckEstAVingt() {
        // Arrange
        String username = "utilisateur";
        Characteristics characteristics = new Characteristics(10, 5, 20);
        when(characterRepository.findCharacterByUsername(username))
                .thenReturn(new Character(username, ClassTypeEnum.WARRIOR, new Inventory()));
        doReturn(characteristics)
                .when(configurationHelper)
                .getCharacteristics(ClassTypeEnum.WARRIOR);

        // Act
        boolean hasLuck = characterService.hasLuck(username);

        // Assert
        assertThat(hasLuck).isTrue();
    }

    @Test
    void testDoitRetournerTrueQuandLaLuckEstAuDessusDeVingt() {
        // Arrange
        String username = "utilisteur";
        Characteristics characteristics = new Characteristics(10, 5, 21);
        when(characterRepository.findCharacterByUsername(username))
                .thenReturn(new Character(username, ClassTypeEnum.WARRIOR, new Inventory()));
        doReturn(characteristics)
                .when(configurationHelper)
                .getCharacteristics(ClassTypeEnum.WARRIOR);

        // Act
        boolean hasLuck = characterService.hasLuck(username);

        // Assert
        assertThat(hasLuck).isTrue();
    }

    @Test
    void testDoisRetournerFalseQuandLaLuckEstAZero() {
        // Arrange
        String username = "utilisateur";

        Characteristics characteristics = new Characteristics(10, 5, 0);
        when(characterRepository.findCharacterByUsername(username))
                .thenReturn(new Character(username, ClassTypeEnum.WARRIOR, new Inventory()));
        doReturn(characteristics)
                .when(configurationHelper)
                .getCharacteristics(ClassTypeEnum.WARRIOR);

        // Act
        boolean hasLuck = characterService.hasLuck(username);

        // Assert
        assertThat(hasLuck).isFalse();
    }
}
