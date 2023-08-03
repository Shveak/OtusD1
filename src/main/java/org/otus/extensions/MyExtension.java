package org.otus.extensions;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.otus.annotations.Driver;
import org.otus.driver.WebDriverFactory;
import org.otus.listeners.Mouse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MyExtension implements BeforeEachCallback, AfterEachCallback {

    private WebDriver driver;

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        if (driver != null) {
//            driver.close();
//            driver.quit();
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        driver = new WebDriverFactory().getDriver();
        getAnnotatedFields(Driver.class, context).forEach(x -> {
            if (x.getType().getName().equals(WebDriver.class.getName())) {
                x.setAccessible(true);
                try {
                    x.set(context.getTestInstance().get(), driver);
                } catch (IllegalAccessException e) {
                    throw new Error(String.format("Не возможно установить значение WebDriver в поле %s", x), e);
                }
            }
        });
    }

    private List<Field> getAnnotatedFields(Class<? extends Annotation> annotation, ExtensionContext context) {
        List<Field> list = new ArrayList<>();
        Class<?> clazz = context.getTestClass().get();
        do {
            list.addAll(Arrays.stream(clazz.getDeclaredFields()).filter(x -> x.isAnnotationPresent(annotation)).collect(Collectors.toList()));
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        return list;
    }
}
