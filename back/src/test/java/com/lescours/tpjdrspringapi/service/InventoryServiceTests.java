package com.lescours.tpjdrspringapi.service;

import com.lescours.tpjdrspringapi.model.*;
import com.lescours.tpjdrspringapi.model.Character;
import com.lescours.tpjdrspringapi.repository.InventoryObjectRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class InventoryServiceTests {

    @Mock
    private InventoryObjectRepository inventoryObjectRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    void testDoisRetournerUneListeVideQuandAucunObjectDansLInventaire() {
        // Arrange
        Inventory inventory = new Inventory();
        doReturn(List.of())
                .when(inventoryObjectRepository)
                .findByInventory(inventory);

        // Act
        List<InventoryObject> objects = inventoryService.getObjectsByInventory(inventory);

        // Assert
        assertThat(objects)
                .isEmpty();
    }

    @Test
    void testDoisRetournerLesObjetsDeLInventaire() {
        // Arrange
        String objectName1 = "Sword";
        String objectName2 = "Wand";

        Inventory inventory = new Inventory();
        InventoryObject object1 = new InventoryObject(inventory, objectName1, new Characteristics(1, 0, 0));
        InventoryObject object2 = new InventoryObject(inventory, objectName2, new Characteristics(0, 1, 0));

        doReturn(List.of(object1, object2))
                .when(inventoryObjectRepository)
                .findByInventory(any());

        // Act
        List<InventoryObject> objects = inventoryService.getObjectsByInventory(inventory);

        // Assert
        assertThat(objects)
                .containsExactlyInAnyOrder(object1, object2);
    }

    @Test
    void testDoisSauvegarderLObjetDansLInventaire() {
        // Arrange
        String username = "utilisateur";
        ClassTypeEnum classType = ClassTypeEnum.WARRIOR;
        String objectName = "Sword";

        Character character = new Character(username, classType, new Inventory());
        InventoryObjectRecord record = new InventoryObjectRecord(objectName, 1, 0, 0);

        // Act
        inventoryService.saveObject(character, record);

        // Assert
        verify(inventoryObjectRepository, times(1))
                .save(any(InventoryObject.class));
    }

    @Test
    void testDoisSauvegarderLObjetPourLePersonnage() {
        // Arrange
        String username = "utilisateur";
        ClassTypeEnum classType = ClassTypeEnum.WARRIOR;
        String objectName = "Sword";

        Inventory inventory = new Inventory();
        Character character = new Character(username, classType, inventory);
        InventoryObject inventoryObject = new InventoryObject(character.getInventory(), objectName, new Characteristics(1, 0, 0));

        // Act
        inventoryService.saveObject(character, inventoryObject);

        // Assert
        verify(inventoryObjectRepository, times(1))
                .save(inventoryObject);
    }

}