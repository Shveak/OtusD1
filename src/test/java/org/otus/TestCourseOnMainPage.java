package org.otus;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.otus.annotations.Driver;
import org.otus.components.BannersCourses;
import org.otus.extensions.MyExtension;
import org.otus.pages.CoursePage;
import org.otus.pages.MainPage;

import static java.lang.Thread.sleep;

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
    }

    @Test
    @DisplayName("Выбирает баннер с минимальной датой начала курса")
    public void findCourseWithMinDateBegin() {

        BannersCourses bannersCourses = new BannersCourses(driver).selectCourseWithMinDateBegin();
        String titleCourse = bannersCourses.getTitleCurrentBanner();
        String dateBeginCourse = bannersCourses.getDateBeginCurrentBanner();
        bannersCourses.click();

        System.out.printf("Мin курс '%s', начало -> %s%n", titleCourse, dateBeginCourse);
    }
}
