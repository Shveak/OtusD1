package org.otus;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.otus.annotations.Driver;
import org.otus.components.BannersCourses;
import org.otus.driver.WebDriverFactory;
import org.otus.pages.CoursePage;
import org.otus.pages.MainPage;

import java.util.UUID;

//@ExtendWith(MyExtension.class)
public class TestCourseOnMainPage {
    private static final String BASE_URL = System.getProperty("webdriver.base.url", "https://otus.ru");

    @Driver
    private WebDriver driver;
    private MainPage mainPage;

    @BeforeEach
    public void before() {
        driver = new WebDriverFactory().getDriver();
        mainPage = new MainPage(driver);
        mainPage.open(BASE_URL);
    }

    @AfterEach
    public void after() {
        mainPage.close();
    }

    @Test
    @DisplayName("Выбираем курс по наименованию")
    public void findCourse() {
        BannersCourses bannersCourses = new BannersCourses(driver).selectCourseByTitle("Специализация Python");
        String url = bannersCourses.getAttribute("href").replace("promo", "lessons");
        CoursePage coursePagePage = bannersCourses.click();

        System.out.printf("Открыта страница курса -> %s%n", coursePagePage.getTitle());
        Allure.getLifecycle().startStep(UUID.randomUUID().toString(), (new StepResult()).setName("Cтраница курса: " + coursePagePage.getUrl()).setStatus(Status.PASSED));

        Assertions.assertTrue(coursePagePage.getUrl().contains(url), "Страница курса выбрана не верно");
    }

    @Test
    @DisplayName("Выбирает баннер с максимальной датой начала курса")
    public void findCourseWithMaxDateBegin() {
        BannersCourses bannersCourses = new BannersCourses(driver).selectCourseWithMaxDateBegin();
        String titleCourse = bannersCourses.getTitleCurrentBanner();
        String dateBeginCourse = bannersCourses.getDateBeginCurrentBanner();
//        bannersCourses.click();

        System.out.printf("Мах курс '%s', начало -> %s%n", titleCourse, dateBeginCourse);
        Allure.getLifecycle().startStep(UUID.randomUUID().toString(), (new StepResult()).setName("Мах курс: " + titleCourse).setStatus(Status.PASSED));
        Allure.getLifecycle().startStep(UUID.randomUUID().toString(), (new StepResult()).setName("Начало: " + dateBeginCourse).setStatus(Status.PASSED));
    }

    @Test
    @DisplayName("Выбирает баннер с минимальной датой начала курса")
    public void findCourseWithMinDateBegin() {

        BannersCourses bannersCourses = new BannersCourses(driver).selectCourseWithMinDateBegin();
        String titleCourse = bannersCourses.getTitleCurrentBanner();
        String dateBeginCourse = bannersCourses.getDateBeginCurrentBanner();
        bannersCourses.click();

        System.out.printf("Мin курс '%s', начало -> %s%n", titleCourse, dateBeginCourse);
        Allure.getLifecycle().startStep(UUID.randomUUID().toString(), (new StepResult()).setName("Мин курс: " + titleCourse).setStatus(Status.PASSED));
        Allure.getLifecycle().startStep(UUID.randomUUID().toString(), (new StepResult()).setName("Начало: " + dateBeginCourse).setStatus(Status.PASSED));
    }
}
