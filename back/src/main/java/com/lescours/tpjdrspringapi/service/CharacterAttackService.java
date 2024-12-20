package com.lescours.tpjdrspringapi.service;

import com.lescours.tpjdrspringapi.model.Characteristics;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CharacterAttackService {

    private final CharacterService characterService;

    public int getCharacterAttack(String username) {
        Characteristics characteristics = characterService.getCharacteristicsByUsername(username);
        return characteristics.getStrength() + characteristics.getIntelligence();
    }

}
