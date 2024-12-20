package com.lescours.tpjdrspringapi.util;

import com.lescours.tpjdrspringapi.model.Characteristics;
import com.lescours.tpjdrspringapi.model.ClassTypeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "classtype.warrior.strength=10",
        "classtype.warrior.intelligence=5",
        "classtype.warrior.luck=3",
        "classtype.mage.strength=3",
        "classtype.mage.intelligence=10",
        "classtype.mage.luck=5",
        "classtype.poor.strength=1",
        "classtype.poor.intelligence=1",
        "classtype.poor.luck=1"
})
class ConfigurationHelperTests {

    @Value("${classtype.warrior.strength}")
    private int warriorStrength;

    @Value("${classtype.warrior.intelligence}")
    private int warriorIntelligence;

    @Value("${classtype.warrior.luck}")
    private int warriorLuck;

    @Value("${classtype.mage.strength}")
    private int mageStrength;

    @Value("${classtype.mage.intelligence}")
    private int mageIntelligence;

    @Value("${classtype.mage.luck}")
    private int mageLuck;

    @Value("${classtype.poor.strength}")
    private int poorStrength;

    @Value("${classtype.poor.intelligence}")
    private int poorIntelligence;

    @Value("${classtype.poor.luck}")
    private int poorLuck;

    @Autowired
    private ConfigurationHelper configurationHelper;

    @Test
    void getCharacteristicsShouldReturnWarriorCharacteristics() {
        // Arrange
        ClassTypeEnum classType = ClassTypeEnum.WARRIOR;

        // Act
        Characteristics characteristics = configurationHelper.getCharacteristics(classType);

        // Assert
        assertThat(characteristics.getStrength())
                .isEqualTo(warriorStrength);
        assertThat(characteristics.getIntelligence())
                .isEqualTo(warriorIntelligence);
        assertThat(characteristics.getLuck())
                .isEqualTo(warriorLuck);
    }

    @Test
    void getCharacteristicsShouldReturnMageCharacteristics() {
        // Arrange
        ClassTypeEnum classType = ClassTypeEnum.MAGE;

        // Act
        Characteristics characteristics = configurationHelper.getCharacteristics(classType);

        // Assert
        assertThat(characteristics.getStrength())
                .isEqualTo(mageStrength);
        assertThat(characteristics.getIntelligence())
                .isEqualTo(mageIntelligence);
        assertThat(characteristics.getLuck())
                .isEqualTo(mageLuck);
    }

    @Test
    void getCharacteristicsShouldReturnPoorCharacteristics() {
        // Arrange
        ClassTypeEnum classType = ClassTypeEnum.POOR;

        // Act
        Characteristics characteristics = configurationHelper.getCharacteristics(classType);

        // Assert
        assertThat(characteristics.getStrength())
                .isEqualTo(poorStrength);
        assertThat(characteristics.getIntelligence())
                .isEqualTo(poorIntelligence);
        assertThat(characteristics.getLuck())
                .isEqualTo(poorLuck);
    }
}