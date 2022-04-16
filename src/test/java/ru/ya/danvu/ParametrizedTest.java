package ru.ya.danvu;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
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

    @BeforeAll
    static void setUp() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        Configuration.baseUrl = "https://vkusvill.ru/";
        Configuration.browserSize = "1920x1080";
    }

    static Stream<Arguments> commonFishSearchTestDataProvider() {
        return Stream.of(
                Arguments.of("Семга", "О своем продукте мы всегда напишем вкусно"),
                Arguments.of("Щука", "Щука — диетическая рыба: в ней много легкоусвояемого белка, полезных элементов и практически нет 1жира")
        );
    }

    @ParameterizedTest(name = "Ищем рыбку: {0}")
    @MethodSource("commonFishSearchTestDataProvider")
    void commonFishSearchTest(String testData, String expectedResult) {

        open("");
        $("input[name='q']").val(testData);
        $(".HeaderSearchBlockProd__Layer").click();
        $(".Product__descText").shouldHave(text(expectedResult));
    }

    @ParameterizedTest(name = "Находим нужные фрутки: {0}")
    @CsvSource(value = {
            "Киви, Киви – хотя и экзотический, но привычный для нас фрукт",
            "Бананы, Банан настолько популярный и любимый многими фрукт"
    })

    void commonFruitsSearchTest(String testData, String expectedResult) {

        open("");
        $("input[name='q']").val(testData);
        $(".HeaderSearchBlockProd__Layer").click();
        $(".Product__descText").shouldHave(text(expectedResult));
    }

    @ParameterizedTest(name = "Находим нужный сыр: {0}")
    @ValueSource(strings = {"сыр Российский_«Российский» изготавливается только из натурального " +
            "пастеризованного коровьего молока", "сыр пошехонский_У этого сыра кисловатый немного пряный вкус"})
    void commonCheeseSearchTest(String testData) {
        String[] split = testData.split("_");

        open("");
        $("input[name='q']").val(split[0]);
        $(".HeaderSearchBlockProd__Layer").click();
        $(".Product__descText").shouldHave(text(split[1]))
                .shouldHave(text(split[1]));
    }
}
