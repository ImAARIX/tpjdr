package com.lescours.tpjdrspringapi.controller;

import com.lescours.tpjdrspringapi.model.*;
import com.lescours.tpjdrspringapi.repository.MonsterRepository;
import com.lescours.tpjdrspringapi.service.CharacterAttackService;
import com.lescours.tpjdrspringapi.service.CharacterService;
import com.lescours.tpjdrspringapi.service.InventoryService;
import com.lescours.tpjdrspringapi.service.MonsterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("monster")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MonsterController {

    private final MonsterService monsterService;

    @GetMapping("{id}")
    public Monster getMonsterById(@PathVariable Long id) {
        return monsterService.findById(id);
    }

    @GetMapping("save/{monsterType}")
    public Monster createNew(@PathVariable MonsterTypeEnum monsterType) {
        return monsterService.createNew(monsterType);
    }

    @PutMapping("{monsterId}/attacked/{username}")
    public Monster wasAttacked(@PathVariable Long monsterId, @PathVariable String username)
            throws IllegalAccessException {
        return monsterService.wasAttacked(monsterId, username);
    }

}
