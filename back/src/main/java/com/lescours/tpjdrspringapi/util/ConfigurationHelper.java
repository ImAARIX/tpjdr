package com.lescours.tpjdrspringapi.util;

import com.lescours.tpjdrspringapi.model.Characteristics;
import com.lescours.tpjdrspringapi.model.ClassTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConfigurationHelper {

    @Value("${classtype.warrior.strength}") private int warriorStrength;
    @Value("${classtype.warrior.intelligence}") private int warriorIntelligence;
    @Value("${classtype.warrior.luck}") private int warriorLuck;

    @Value("${classtype.mage.strength}") private int mageStrength;
    @Value("${classtype.mage.intelligence}") private int mageIntelligence;
    @Value("${classtype.mage.luck}") private int mageLuck;

    @Value("${classtype.poor.strength}") private int poorStrength;
    @Value("${classtype.poor.intelligence}") private int poorIntelligence;
    @Value("${classtype.poor.luck}") private int poorLuck;

    public Characteristics getCharacteristics(ClassTypeEnum classType) {
        return switch (classType) {
            case WARRIOR -> new Characteristics(warriorStrength, warriorIntelligence, warriorLuck);
            case MAGE -> new Characteristics(mageStrength, mageIntelligence, mageLuck);
            case POOR -> new Characteristics(poorStrength, poorIntelligence, poorLuck);
        };
    }
}
