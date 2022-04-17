package ru.ya.danvu;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;

public class LambdaParametrizedTest {

    @BeforeAll
    static void setUp() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        Configuration.baseUrl = "https://vkusvill.ru/";
        Configuration.browserSize = "1920x1080";
    }

    static Stream<Arguments> commonFishSearchTestDataProvider() {
        return Stream.of(
                Arguments.of("Семга", "О своем продукте мы всегда напишем вкусно"),
                Arguments.of("Щука", "Щука — диетическая рыба: в ней много легкоусвояемого белка, полезных элементов и практически нет жира")
        );
    }

    @ParameterizedTest(name = "Ищем рыбку: {0}")
    @MethodSource("commonFishSearchTestDataProvider")
    void commonFishSearchTest(String testData, String expectedResult) {

       step("Открываем главную страницу", () -> {
           open("");
       });

       step("Вводим название товара", () -> {
           $("input[name='q']").val(testData);
       });

       step("Нажимаем на товар", () -> {
           $(".HeaderSearchBlockProd__Layer").click();
       });

       step("Проверяем, что товар соответствует описанию", () -> {
           $(".Product__descText").shouldHave(text(expectedResult));
       });
    }

    @ParameterizedTest(name = "Находим нужные фрутки: {0}")
    @CsvSource(value = {
            "Киви, Киви – хотя и экзотический, но привычный для нас фрукт",
            "Бананы, Банан настолько популярный и любимый многими фрукт"
    })

    void commonFruitsSearchTest(String testData, String expectedResult) {

        step("Открываем главную страницу", () -> {
            open("");
        });

        step("Вводим название товара", () -> {
            $("input[name='q']").val(testData);
        });

        step("Нажимаем на товар", () -> {
            $(".HeaderSearchBlockProd__Layer").click();
        });

        step("Проверяем, что товар соответствует описанию", () -> {
            $(".Product__descText").shouldHave(text(expectedResult));
        });
    }

    @ParameterizedTest(name = "Находим нужный сыр: {0}")
    @ValueSource(strings = {"сыр Российский_«Российский» изготавливается только из натурального " +
            "пастеризованного коровьего молока", "сыр Виола_В составе Viola `Четыре сыра` – комбинация твердых и полутвердых сортов сыра"})
    void commonCheeseSearchTest(String testData) {
        String[] split = testData.split("_");

        step("Открываем главную страницу", () -> {
            open("");
        });

        step("Вводим название товара", () -> {
            $("input[name='q']").val(testData);
        });

        step("Нажимаем на товар", () -> {
            $(".HeaderSearchBlockProd__Layer").click();
        });

        step("Проверяем, что товар соответствует описанию", () -> {
            $(".Product__descText").shouldHave(text(split[1]))
                    .shouldHave(text(split[1]));
            Allure.getLifecycle().addAttachment(
                    "Исходники страницы",
                    "text/html",
                    "html",
                    WebDriverRunner.getWebDriver().getPageSource().getBytes(StandardCharsets.UTF_8));
        });
    }
}
