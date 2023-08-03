package org.otus.actions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.otus.waiters.Waiter;

import static java.lang.Thread.sleep;

public abstract class Action {
    protected WebDriver driver;

    protected Waiter waiter;

    public Action(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.waiter = new Waiter(driver);
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
