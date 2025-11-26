package org.example;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

@DisplayName("Класс NightClub")
public class NightClubTest {
    private NightClub nightClub;
    private FaceController faceController;

    @BeforeEach
    void setUp() {
        faceController = Mockito.mock(FaceController.class);
        nightClub = new NightClub(faceController);
    }

    @DisplayName("Пускаем людей в стиле casual, не в пятницу в пустой клуб")
    @Test
    void shouldAcceptComeInForCustomerInCasualNotFriday() {
        BDDMockito.given(faceController.decide(Style.CASUAL)).willReturn(true);

        boolean expectedResult = true;

        boolean actualResult = nightClub.comeIn(Style.CASUAL, false);

        Assertions.assertThat(expectedResult).isEqualTo(actualResult);
    }

    @DisplayName("Пускаем людей в стиле casual не в пятницу в пустой клуб и не пустить второго")
    @Test
    void shouldAcceptComeInForCustomerInCasualNotFridayAndDontAcceptSecond() {
        BDDMockito.given(faceController.decide(Style.CASUAL)).willReturn(true);

        boolean expectedResult1 = true;
        boolean expectedResult2 = false;

        boolean actualResult1 = nightClub.comeIn(Style.CASUAL, false);
        boolean actualResult2 = nightClub.comeIn(Style.CASUAL, false);

        Assertions.assertThat(expectedResult1).isEqualTo(actualResult1);
        Assertions.assertThat(expectedResult2).isEqualTo(actualResult2);
    }

    @DisplayName("Не пускаем людей в стиле sport не в пятницу в пустой клуб")
    @Test
    void shouldAcceptComeInForCustomerInSportNotFriday() {
        BDDMockito.given(faceController.decide(Style.Sport)).willReturn(false);

        boolean expectedResult = false;

        boolean actualResult = nightClub.comeIn(Style.Sport, false);

        Assertions.assertThat(expectedResult).isEqualTo(actualResult);

        // или можно записать так:
        boolean result = nightClub.comeIn(Style.Sport, false);
        Assertions.assertThat(result).isFalse(); //здесь мы говорим, что в результате ожидаем false
    }

    @DisplayName("Пускаем людей в стиле sport в пятницу в пустой клуб")
    @Test
    void shouldAcceptComeInForCustomerInSportInFriday() {
        BDDMockito.given(faceController.decide(Style.Sport)).willReturn(false);

        boolean expectedResult = true;

        boolean actualResult = nightClub.comeIn(Style.Sport, true);

        Assertions.assertThat(expectedResult).isEqualTo(actualResult);
    }

    @DisplayName("Пускаем людей в стиле casual не в пятницу в пустой клуб и не пустить второго, выпустить первого")
    @Test
    void shouldAcceptComeInForCustomerInCasualNotFridayAndDontAcceptSecondAndComeOutFirst() {
        BDDMockito.given(faceController.decide(Style.CASUAL)).willReturn(true);
        BDDMockito.given(faceController.decide(false)).willReturn(true);

        boolean expectedResult1 = true;
        boolean expectedResult2 = false;
        boolean expectedResult3 = true;

        boolean actualResult1 = nightClub.comeIn(Style.CASUAL, false);
        boolean actualResult2 = nightClub.comeIn(Style.CASUAL, false);
        boolean actualResult3 = nightClub.comeOut(false);

        Assertions.assertThat(expectedResult1).isEqualTo(actualResult1);
        Assertions.assertThat(expectedResult2).isEqualTo(actualResult2);
        Assertions.assertThat(expectedResult3).isEqualTo(actualResult3);
    }
}
