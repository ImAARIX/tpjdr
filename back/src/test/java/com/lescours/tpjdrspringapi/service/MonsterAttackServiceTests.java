package com.lescours.tpjdrspringapi.service;

import com.lescours.tpjdrspringapi.model.MonsterTypeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "monster.rat.attack=5",
        "monster.wolf.attack=10",
        "monster.demon.attack=15"
})
public class MonsterAttackServiceTests {

    @Value("${monster.rat.attack}")
    private int ratAttack;

    @Value("${monster.wolf.attack}")
    private int wolfAttack;

    @Value("${monster.demon.attack}")
    private int demonAttack;

    @Autowired
    private MonsterAttackService monsterAttackService;

    @Test
    void testGetMonsterAttackDoitRetournerLesValeursDesAttaques() {
        // Assert
        assertThat(monsterAttackService.getMonsterAttack(MonsterTypeEnum.RAT))
                .isEqualTo(ratAttack);
        assertThat(monsterAttackService.getMonsterAttack(MonsterTypeEnum.WOLF))
                .isEqualTo(wolfAttack);
        assertThat(monsterAttackService.getMonsterAttack(MonsterTypeEnum.DEMON))
                .isEqualTo(demonAttack);
    }

}
