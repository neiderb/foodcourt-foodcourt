package com.foodcourt.foodcourt;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.mockito.Mockito.times;

class FoodCourtApplicationTest {

    @Test
    void shouldInvokeSpringApplicationRunWhenMainIsCalled() {
        try (MockedStatic<SpringApplication> springAppMock = Mockito.mockStatic(SpringApplication.class)) {
            springAppMock.when(() -> SpringApplication.run(Mockito.eq(FoodCourtApplication.class), Mockito.any(String[].class)))
                    .thenReturn(Mockito.mock(ConfigurableApplicationContext.class));

            FoodCourtApplication.main(new String[]{});

            springAppMock.verify(() -> SpringApplication.run(Mockito.eq(FoodCourtApplication.class), Mockito.any(String[].class)), times(1));
        }
    }

}

