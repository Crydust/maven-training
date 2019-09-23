package com.mycompany.example13.boilerplate;

import java.io.InputStream;
import java.util.Properties;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.mycompany.example13.model.IndexPage;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BrowserResource implements BeforeAllCallback, AfterAllCallback {

    private String baseURL;
    private String browserName;

    private WebDriver driver;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        final Properties config = new Properties();
        try (InputStream in = BrowserResource.class.getResourceAsStream("/example13.properties")) {
            config.load(in);
            baseURL = config.getProperty("baseURL");
            browserName = config.getProperty("browserName");
        }
        switch (browserName) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "htmlunit":
                driver = new HtmlUnitDriver();
                break;
            case "ie":
                WebDriverManager.iedriver().setup();
                driver = new InternetExplorerDriver();
                break;
            case "opera":
                WebDriverManager.operadriver().setup();
                driver = new OperaDriver();
                break;
            case "safari":
                driver = new SafariDriver();
                break;
            default:
                throw new RuntimeException("unknown browserName");
        }
    }

    @Override
    public void afterAll(ExtensionContext context) {
        if (driver != null) {
            driver.quit();
        }
    }

    public IndexPage openIndexPage() {
        driver.get(baseURL);
        return IndexPage.init(driver);
    }

    public String getBaseURL() {
        return baseURL;
    }

    public String getSessionCookie() {
        Cookie cookie = driver.manage().getCookieNamed("JSESSIONID");
        return String.format("JSESSIONID=%s; Path=%s%s%s",
                cookie.getValue(),
                cookie.getPath(),
                cookie.isSecure() ? "; Secure" : "",
                cookie.isHttpOnly() ? "; HttpOnly" : "");
    }
}
