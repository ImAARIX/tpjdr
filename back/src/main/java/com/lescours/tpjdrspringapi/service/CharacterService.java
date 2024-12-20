package com.lescours.tpjdrspringapi.service;

import com.lescours.tpjdrspringapi.model.*;
import com.lescours.tpjdrspringapi.model.Character;
import com.lescours.tpjdrspringapi.repository.CharacterRepository;
import com.lescours.tpjdrspringapi.util.ConfigurationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final InventoryService inventoryService;
    private final ConfigurationHelper configurationHelper;
    private final MonsterAttackService monsterAttackService;

    public Character save(CharacterRegisterRecord characterRegisterRecord) {
        if (characterRepository.existsCharacterByUsername(characterRegisterRecord.username())) {
            Character oldCharacter = characterRepository.findCharacterByUsername(characterRegisterRecord.username());
            characterRepository.delete(oldCharacter);
        }

        Character character = new Character(characterRegisterRecord.username(),
                characterRegisterRecord.classType(),
                new Inventory());

        inventoryService.save(character.getInventory());

        return characterRepository.save(character);
    }

    public Character save(Character character) {
        return characterRepository.save(character);
    }

    public Character getCharacterByUsername(String username) {
        return characterRepository.findCharacterByUsername(username);
    }

    public boolean existsCharacterByUsername(String username) {
        return characterRepository.existsCharacterByUsername(username);
    }

    public boolean saveObject(String username, InventoryObjectRecord inventoryObjectRecord) {
        Character character = characterRepository.findCharacterByUsername(username);
        inventoryService.saveObject(character, inventoryObjectRecord);

        return true;
    }

    public boolean saveObject(String username, InventoryObject inventoryObject) {
        Character character = characterRepository.findCharacterByUsername(username);
        inventoryService.saveObject(character, inventoryObject);

        return true;
    }

    public Characteristics getCharacteristicsByUsername(String username) {
        Character character = characterRepository.findCharacterByUsername(username);

        Characteristics characteristics = configurationHelper.getCharacteristics(character.getClassType());

        List<InventoryObject> inventoryCharacteristics = inventoryService.getObjectsByInventory(character.getInventory());
        for (InventoryObject inventoryObject : inventoryCharacteristics) {
            var objectCharacteristics = inventoryObject.getCharacteristics();

            characteristics.setStrength(characteristics.getStrength() + objectCharacteristics.getStrength());
            characteristics.setIntelligence(characteristics.getIntelligence() + objectCharacteristics.getIntelligence());
            characteristics.setLuck(characteristics.getLuck() + objectCharacteristics.getLuck());
        }

        return characteristics;
    }

    public Character wasAttacked(String username, MonsterTypeEnum monsterType) {
        Character character = getCharacterByUsername(username);

        if (hasLuck(username)) {
            return character;
        }

        int leftHealth = character.getHealth() - monsterAttackService.getMonsterAttack(monsterType);
        character.setHealth(Math.max(leftHealth, 0));

        return save(character);
    }

    public boolean hasLuck(String username) {
        Random random = new Random();

        int luck = Math.min(getCharacteristicsByUsername(username).getLuck(), 20);
        int randomNumber = random.nextInt(20) + 1;

        return randomNumber <= luck;
    }
}
