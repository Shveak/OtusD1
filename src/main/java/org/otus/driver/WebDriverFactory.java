package org.otus.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Random;

public class WebDriverFactory {
    private final String browser = System.getProperty("browser").toLowerCase(Locale.ROOT);

    public WebDriver getDriver() {
        switch (browser) {
            case "chrome": {
                if (System.getProperty("web.driver").equalsIgnoreCase("LOCAL")) {
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--no-sandbox");
                    options.addArguments("--homepage=about:blank");
                    options.addArguments("--ignore-certificate-errors");
                    options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                    options.setCapability("enableVNC", Boolean.parseBoolean(System.getProperty("enableVNC", "false")));
                    WebDriverManager.chromedriver().setup();
                    return new EventFiringWebDriver(new ChromeDriver(options));
                } else {
                    DesiredCapabilities capabilities = new DesiredCapabilities();
                    capabilities.setCapability("browserName", "chrome");
                    capabilities.setCapability("enableVNC", true);
                    double dd = new Random().nextInt(7) / 2.0;
                    String ver = (dd - (int) dd) == 0 ? "110.0" : "112.0";
                    capabilities.setCapability("browserVersion", ver);
                    try {
                        return new RemoteWebDriver(new URL(System.getProperty("selenoid.url")), capabilities);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            case "firefox": {
                FirefoxOptions options = new FirefoxOptions();
                options.addPreference("browser.startup.page", 1);
                options.addPreference("browser.link.open_newwindow", 3);
                options.addPreference("browser.link.open_newwindow.restriction", 0);
                options.setCapability("marionette", true);
                WebDriverManager.firefoxdriver().setup();
                return new EventFiringWebDriver(new FirefoxDriver(options));
            }
            case "opera": {
                OperaOptions options = new OperaOptions();
                options.addArguments("private");
                options.setCapability(OperaOptions.CAPABILITY, options);
                WebDriverManager.operadriver().setup();
                return new EventFiringWebDriver(new OperaDriver(options));
            }
            default:
                throw new Error(String.format("Отсутствует драйвер для браузера '%s'", browser));
        }
    }
}
