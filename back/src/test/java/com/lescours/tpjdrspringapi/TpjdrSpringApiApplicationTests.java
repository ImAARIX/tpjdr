package com.lescours.tpjdrspringapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TpjdrSpringApiApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testApplicationStartupSmokeTest() {
        // Arrange & Act
        TpjdrSpringApiApplication tpjdrSpringApiApplication = applicationContext.getBean(TpjdrSpringApiApplication.class);

        // Assert
        assertThat(tpjdrSpringApiApplication)
                .isNotNull();
    }
}