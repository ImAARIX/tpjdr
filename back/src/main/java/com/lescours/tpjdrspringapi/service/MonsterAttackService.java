package com.lescours.tpjdrspringapi.service;

import com.lescours.tpjdrspringapi.model.MonsterTypeEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MonsterAttackService {

    @Value("${monster.rat.attack}") private int ratAttack;
    @Value("${monster.wolf.attack}") private int wolfAttack;
    @Value("${monster.demon.attack}") private int demonAttack;

    public int getMonsterAttack(MonsterTypeEnum monsterTypeEnum) {
        return switch (monsterTypeEnum) {
            case RAT -> ratAttack;
            case WOLF -> wolfAttack;
            case DEMON -> demonAttack;
        };
    }

}
