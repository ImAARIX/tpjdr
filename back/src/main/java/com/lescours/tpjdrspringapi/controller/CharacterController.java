package com.lescours.tpjdrspringapi.controller;

import com.lescours.tpjdrspringapi.model.*;
import com.lescours.tpjdrspringapi.model.Character;
import com.lescours.tpjdrspringapi.service.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("character")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CharacterController {

    private final CharacterService characterService;

    @GetMapping("{username}")
    public Character getCharacter(@PathVariable String username) {
        return characterService.getCharacterByUsername(username);
    }

    @PutMapping("{username}/attacked/{monsterType}")
    public Character wasAttacked(@PathVariable String username, @PathVariable MonsterTypeEnum monsterType) {
        return characterService.wasAttacked(username, monsterType);
    }

    @PostMapping
    public Character saveCharacter(@RequestBody CharacterRegisterRecord characterRegisterRecord) {
        return characterService.save(characterRegisterRecord);
    }

    @GetMapping("{username}/characteristics")
    public Characteristics getCharacteristics(@PathVariable String username) {
        return characterService.getCharacteristicsByUsername(username);
    }

    @PostMapping("{username}/object")
    public boolean saveObject(@PathVariable String username, @RequestBody InventoryObjectRecord inventoryObjectRecord) {
        return characterService.saveObject(username, inventoryObjectRecord);
    }

    @GetMapping("{username}/exists")
    public boolean existsCharacter(@PathVariable String username) {
        return characterService.existsCharacterByUsername(username);
    }

}
