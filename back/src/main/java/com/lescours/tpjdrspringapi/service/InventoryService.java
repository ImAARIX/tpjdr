package com.lescours.tpjdrspringapi.service;

import com.lescours.tpjdrspringapi.model.*;
import com.lescours.tpjdrspringapi.model.Character;
import com.lescours.tpjdrspringapi.repository.InventoryObjectRepository;
import com.lescours.tpjdrspringapi.repository.InventoryRepository;
import com.lescours.tpjdrspringapi.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryObjectRepository inventoryObjectRepository;

    public void save(Inventory inventory) {
        inventoryRepository.save(inventory);
    }

    public void saveObject(Character character, InventoryObjectRecord inventoryObjectRecord) {
        Characteristics characteristics = new Characteristics(inventoryObjectRecord.strength(),
                inventoryObjectRecord.intelligence(),
                inventoryObjectRecord.luck());
        InventoryObject inventoryObject = new InventoryObject(character.getInventory(),
                inventoryObjectRecord.name(),
                characteristics);

        inventoryObjectRepository.save(inventoryObject);
    }

    public void saveObject(Character character, InventoryObject inventoryObject) {
        inventoryObject.setInventory(character.getInventory());
        inventoryObjectRepository.save(inventoryObject);
    }

    public void saveObject(InventoryObject object) {
        inventoryObjectRepository.save(object);
    }

    public List<InventoryObject> getObjectsByInventory(Inventory inventory) {
        return inventoryObjectRepository.findByInventory(inventory);
    }

    public Characteristics getCharacteristicsForObject(Objects object) {
        return switch (object) {
            case SWORD -> new Characteristics(1, 0, 0);
            case WAND -> new Characteristics(0, 1, 0);
            case LOTTERY_TICKET -> new Characteristics(0, 0, 1);
        };
    }
}
