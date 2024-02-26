package ru.netology.test;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.*;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.Faker;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.netology.data.DataGenerator;

import java.time.Duration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class CardDeliveryTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("credentials_enable_service", false);
        prefs.put("password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        Configuration.browserCapabilities = options;
    }


    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        open("http://localhost:9999");
        $("[data-test-id='city'] [placeholder='Город']").sendKeys(validUser.getCity());
        $("[data-test-id='date'] [placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").sendKeys(firstMeetingDate);
        $("[data-test-id='name'] [name='name']").sendKeys("Иван Иванов");
        $("[data-test-id='phone'] [name='phone']").sendKeys("+79182321062");
        $("[data-test-id=agreement]").click();
        $("[type='button'] .button__text").click();
        $(".notification__title").find(withText("Успешно"));
        $(".notification__content").find(withText("Встреча успешно запланирована на " + firstMeetingDate));
        $("[data-test-id='date'] [placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] [placeholder='Дата встречи']").setValue(secondMeetingDate);
        $("[type='button'] .button__text").click();
        $("[data-test-id=replan-notification]").should(visible, Duration.ofSeconds(15));
        $(".notification__content").find(withText("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $(byText("Перепланировать")).shouldBe(visible, Duration.ofSeconds(5)).click();
        $("[data-test-id=success-notification] .notification__content").should(visible, Duration.ofSeconds(15)).should(exactText("Встреча успешно запланирована на " + secondMeetingDate));
    }
}