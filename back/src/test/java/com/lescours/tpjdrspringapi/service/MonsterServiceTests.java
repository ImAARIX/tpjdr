package com.lescours.tpjdrspringapi.service;

import com.lescours.tpjdrspringapi.model.*;
import com.lescours.tpjdrspringapi.repository.CharacteristicsRepository;
import com.lescours.tpjdrspringapi.repository.MonsterRepository;
import com.lescours.tpjdrspringapi.util.Objects;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class MonsterServiceTests {

    @Mock
    private MonsterRepository monsterRepository;

    @Mock
    private InventoryService inventoryService;

    @Mock
    private CharacteristicsRepository characteristicsRepository;

    @InjectMocks
    private MonsterService monsterService;

    @Test
    void testCreerNouvelObjet() {
        // Arrange
        MonsterService spyMonsterServiceMock = spy(monsterService);

        MonsterTypeEnum monsterType = MonsterTypeEnum.RAT;
        Objects object = Objects.SWORD;

        when(inventoryService.getCharacteristicsForObject(object))
                .thenReturn(new Characteristics(1, 0, 0));
        when(monsterRepository.save(any(Monster.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        doReturn(object)
                .when(spyMonsterServiceMock)
                .getRandomMonsterObject();

        // Act
        Monster monster = spyMonsterServiceMock.createNew(monsterType);

        // Assert
        assertThat(monster)
                .isNotNull();
        assertThat(monster.getType())
                .isEqualTo(monsterType);
        assertThat(monster.getDroppedObject())
                .isNotNull();
        assertThat(monster.getDroppedObject().getName())
                .isEqualTo(object.name());
    }

    @Test
    void testSauvegardeDuMonstreEtDeSesObjets() {
        // Arrange

        String objectName = "Sword";
        Monster monster = new Monster(MonsterTypeEnum.WOLF, new InventoryObject(objectName, new Characteristics(1, 0, 0)));

        when(monsterRepository.save(any(Monster.class)))
                .thenReturn(monster);

        // Act
        Monster savedMonster = monsterService.save(monster);

        // Assert
        verify(characteristicsRepository, times(1))
                .save(monster.getDroppedObject().getCharacteristics());
        verify(inventoryService, times(1))
                .saveObject(monster.getDroppedObject());
        verify(monsterRepository, times(1))
                .save(monster);
        assertThat(savedMonster)
                .isEqualTo(monster);
    }

    @Test
    void testFindByIdDoitRetournerLeMonstreSiLeMonstreExiste() {
        // Arrange
        Long id = 1L;
        Monster monster = new Monster(MonsterTypeEnum.DEMON, new InventoryObject("Wand", new Characteristics(0, 1, 0)));

        when(monsterRepository.findById(id))
                .thenReturn(Optional.of(monster));

        // Act
        Monster foundMonster = monsterService.findById(id);

        // Assert
        assertThat(foundMonster)
                .isEqualTo(monster);
    }

    @Test
    void testFindByIdDoitRetournerNullSiLeMonstreNExistePas() {
        // Arrange
        Long id = 1L;

        when(monsterRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act
        Monster foundMonster = monsterService.findById(id);

        // Assert
        assertThat(foundMonster)
                .isNull();
    }

    @Test
    void testGetRandomMonsterObjectDoitRetournerUnObjetValide() {
        // Act
        Objects randomObject = monsterService.getRandomMonsterObject();

        // Assert
        assertThat(randomObject)
                .isNotNull();
        assertThat(Arrays.asList(Objects.values()))
                .contains(randomObject);
    }
}