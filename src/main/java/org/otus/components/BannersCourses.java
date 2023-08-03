package org.otus.components;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.otus.pages.CoursePage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class BannersCourses extends BaseComponentAbs {
    private WebElement currentBunner;
    @FindBy(xpath = "//a[contains(@href, 'https://otus.ru/lessons/')]")
    private List<WebElement> listBanner;

    public BannersCourses(WebDriver driver) {
        super(driver);
    }

    public BannersCourses selectCourseByTitle(String title) {
        currentBunner = listBanner.stream()
                .filter(x -> getTitle(x).equals(title))
                .findFirst()
                .orElseThrow(() -> new AssertionError(String.format("Не найден баннер курса %s", title)));
        return this;
    }

    public BannersCourses selectCourseWithMaxDateBegin() {
        currentBunner = listBanner.stream()
                .filter(x -> getDateBegin(x) != null)
                .reduce((p1, p2) -> {
                    LocalDate d1 = getDateBegin(p1);
                    LocalDate d2 = getDateBegin(p2);
                    return d1.isAfter(d2) || d1.isEqual(d2) ? p1 : p2;
                })
                .orElseThrow(() -> new AssertionError("Не удалось вычислить мах значение"));
        return this;
    }

    public BannersCourses selectCourseWithMinDateBegin() {
        currentBunner = listBanner.stream()
                .filter(x -> getDateBegin(x) != null)
                .reduce((p1, p2) -> {
                    LocalDate d1 = getDateBegin(p1);
                    LocalDate d2 = getDateBegin(p2);
                    return d1.isBefore(d2) ? p1 : p2;
                })
                .orElseThrow(() -> new AssertionError("Не удалось вычислить min значение"));
        return this;
    }

    public String getTitleCurrentBanner() {
        return getTitle(getCurrentBanner());
    }

    public String getDateBeginCurrentBanner() {
        return getDateBegin(getCurrentBanner()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public CoursePage click() {
        scrollTo(getCurrentBanner());
        click(getCurrentBanner());
        return new CoursePage(driver);
    }

    public String getAttribute(String str) {
        return getCurrentBanner().getAttribute(str);
    }

    private WebElement getCurrentBanner() {
        return currentBunner == null ? listBanner.get(0) : currentBunner;
    }

    private String getTitle(WebElement webElement) {
        String titleLocator = ".//h5";
        List<WebElement> allElements = webElement.findElements(By.xpath(titleLocator));
        if (allElements != null && !allElements.isEmpty()) {
            return allElements.get(0).getText();
        } else {
            throw new NoSuchElementException("Элемент в баннере не найден " + titleLocator);
        }
    }

    private LocalDate getDateBegin(WebElement webElement) {
        List<String> dateBlockLocatorList = Arrays.asList(".//div[contains(@class, 'new-item-bottom_spec')]/div[2]",
                ".//div[contains(@class, 'start')]", ".//span");
        List<WebElement> allElements = null;
        for (String locator : dateBlockLocatorList) {
            allElements = webElement.findElements(By.xpath(locator));
            if (allElements != null && !allElements.isEmpty()) {
                break;
            }
        }
        if (allElements == null || allElements.isEmpty()) {
            throw new NoSuchElementException("Элемент в баннере не найден " + dateBlockLocatorList);
        }
        boolean one = allElements.size() < 4;
        String strDate = getDate(allElements.get(one ? 1 : 0).getText());
        if (strDate.isEmpty()) {
            return null;
        }
        List<String> pathDate = Arrays.asList(strDate.split(" "));
        return LocalDate.parse(String.format("%1$2s", pathDate.get(0)).replace(' ', '0') + "."
                + String.format("%1$2s", Month.of(pathDate.get(1)).getNumber()).replace(' ', '0')
                + "." + pathDate.get(2), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    private String getDate(String str) {
        Pattern pattern = Pattern.compile("^\\D+$");
        Matcher matcher = pattern.matcher(str);
        str = matcher.find() ? "" : str;
        if (str.isEmpty()) {
            return str;
        }
        pattern = Pattern.compile(" \\d+ месяц");
        matcher = pattern.matcher(str);
        while (matcher.find()) {
            str = str.substring(0, matcher.start());
        }
        pattern = Pattern.compile("^[СВ] ");
        matcher = pattern.matcher(str);
        while (matcher.find()) {
            str = str.substring(2);
        }
        pattern = Pattern.compile("^(\\D+) ([0-9]{4}) года");
        matcher = pattern.matcher(str);
        str = matcher.find() ? "1 " + matcher.group(1) + " " + matcher.group(2) : str + " " + LocalDate.now().getYear();
        return StringUtils.isNumeric(str.substring(0, 1)) ? str : "1 " + str;
    }

    private enum Month {
        ЯНВАРЬ("1", "январ"),
        ФЕВРАЛЬ("2", "феврал"),
        МАРТ("3", "март"),
        АПРЕЛЬ("4", "апрел"),
        МАЙ("5", "мая"),
        ИЮНЬ("6", "июн"),
        ИЮЛЬ("7", "июл"),
        АВГУСТ("8", "август"),
        СЕНТЯБРЬ("9", "сентябр"),
        ОКТЯБРЬ("10", "октябр"),
        НОЯБРЬ("11", "ноябр"),
        ДЕКАБРЬ("12", "декабр");

        final String number;
        final String loName;

        Month(String number, String loName) {
            this.number = number;
            this.loName = loName;
        }

        public String getLoName() {
            return this.loName;
        }

        public String getNumber() {
            return this.number;
        }

        public static Month of(String loName) {
            return Stream.of(Month.values())
                    .filter(x -> loName.contains(x.getLoName()))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError(String.format("Не найден месяц по %s", loName)));
        }
    }
}
