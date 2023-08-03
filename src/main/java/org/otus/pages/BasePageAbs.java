package org.otus.pages;

import org.openqa.selenium.WebDriver;
import org.otus.actions.Action;

public abstract class BasePageAbs extends Action {

    public BasePageAbs(WebDriver driver) {
        super(driver);
    }

    public void open(String url) {
        driver.get(url);
    }

    public void close() {
        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }
}
