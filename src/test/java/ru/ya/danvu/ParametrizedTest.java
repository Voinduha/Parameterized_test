package ru.ya.danvu;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class ParametrizedTest {

    static Stream<Arguments> commonFishSearchTestDataProvider() {
        return Stream.of(
                Arguments.of("Семга", "О своем продукте мы всегда напишем вкусно"),
                Arguments.of("Форель", "Форель – рыба семейства лососевых")
        );
    }
    @ParameterizedTest(name = "Ищем рыбку: {0}")
    @MethodSource("commonFishSearchTestDataProvider")
    void commonFishSearchTest(String testData, String expectedResult) {

        open("https://vkusvill.ru/");
        $("input[name='q']").val(testData);
        $(".HeaderSearchBlockProd__Layer").click();
        $(".Product__descText").shouldHave(text(expectedResult));
    }

    @ParameterizedTest(name = "Находим нужные фрутки: {0}")
    @CsvSource(value = {
            "Морковь, Морковь очень полезна",
            "Бананы, Бананы рекомендуются для диетического питания"
    })
    void commonFruitsSearchTest(String testData, String expectedResult) {

        open("https://vkusvill.ru/");
        $("input[name='q']").val(testData);
        $(".HeaderSearchBlockProd__Layer").click();
        $(".Product__descText").shouldHave(text(expectedResult));
    }

    @ParameterizedTest(name = "Находим нужный сыр: {0}")
    @ValueSource(strings = {"сыр Российский_«Российский» изготавливается только из натурального " +
            "пастеризованного коровьего молока", "сыр плавленный_О своем продукте мы всегда напишем вкусно"})
    void commonCheeseSearchTest(String testData) {
        String[] split = testData.split("_");

        open("https://vkusvill.ru/");
        $("input[name='q']").val(split[0]);
        $(".HeaderSearchBlockProd__Layer").click();
        $(".Product__descText").shouldHave(text(split[1]))
                .shouldHave(text(split[1]));
    }
}
