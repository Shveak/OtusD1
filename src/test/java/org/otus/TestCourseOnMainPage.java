package org.otus;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.otus.annotations.Driver;
import org.otus.components.BannersCourses;
import org.otus.extensions.MyExtension;
import org.otus.pages.CoursePage;
import org.otus.pages.MainPage;

import java.util.UUID;

@ExtendWith(MyExtension.class)
public class TestCourseOnMainPage {
    private static final String BASE_URL = System.getProperty("webdriver.base.url", "https://otus.ru");

    @Driver
    private WebDriver driver;

    @BeforeEach
    public void before() {
        new MainPage(driver).open(BASE_URL);
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
        bannersCourses.click();

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
