package com.lescours.tpjdrspringapi.service;

import com.lescours.tpjdrspringapi.model.InventoryObject;
import com.lescours.tpjdrspringapi.model.Monster;
import com.lescours.tpjdrspringapi.model.MonsterTypeEnum;
import com.lescours.tpjdrspringapi.repository.CharacteristicsRepository;
import com.lescours.tpjdrspringapi.repository.MonsterRepository;
import com.lescours.tpjdrspringapi.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MonsterService {

    private final MonsterRepository monsterRepository;
    private final InventoryService inventoryService;
    private final CharacteristicsRepository characteristicsRepository;
    private final CharacterAttackService characterAttackService;
    private final CharacterService characterService;

    @Value("${monster.rat.attack}") private int ratAttack;
    @Value("${monster.wolf.attack}") private int wolfAttack;
    @Value("${monster.demon.attack}") private int demonAttack;

    public Monster createNew(MonsterTypeEnum monsterType) {
        Objects object = getRandomMonsterObject();
        InventoryObject inventoryObject = new InventoryObject(object.name(),
                inventoryService.getCharacteristicsForObject(object));

        return save(new Monster(monsterType, inventoryObject));
    }

    public Monster save(Monster monster) {
        if (monster.getDroppedObject() != null) {
            characteristicsRepository.save(monster.getDroppedObject().getCharacteristics());
            inventoryService.saveObject(monster.getDroppedObject());
        }

        return monsterRepository.save(monster);
    }

    public Monster findById(Long id) {
        return monsterRepository.findById(id).orElse(null);
    }


    public Objects getRandomMonsterObject() {
        Objects[] objects = Objects.values();
        int randomIndex = new Random().nextInt(objects.length);

        return objects[randomIndex];
    }

    public Monster wasAttacked(Long monsterId, String username) throws IllegalAccessException {
        Monster monster = findById(monsterId);
        if (null == monster) throw new IllegalAccessException("Monster not found");

        monster.setHealth(monster.getHealth() - characterAttackService.getCharacterAttack(username));
        InventoryObject object = monster.getDroppedObject();

        if (monster.getHealth() <= 0) {
            characterService.saveObject(username, object);
            monsterRepository.delete(monster);

            return null;
        }

        return save(monster);
    }

}
