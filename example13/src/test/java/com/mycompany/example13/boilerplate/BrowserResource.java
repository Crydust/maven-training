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
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.mycompany.example13.model.IndexPage;

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
            case "chrome": driver = new ChromeDriver(); break;
            case "edge": driver = new EdgeDriver(); break;
            case "firefox": driver = new FirefoxDriver(); break;
            case "htmlunit": driver = new HtmlUnitDriver(); break;
            case "ie": driver = new InternetExplorerDriver(); break;
            case "opera": driver = new OperaDriver(); break;
            case "safari": driver = new SafariDriver(); break;
            default: throw new RuntimeException("unknown browserName");
        }
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
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
                cookie.isSecure()? "; Secure" : "",
                cookie.isHttpOnly() ? "; HttpOnly" : "");
    }
}
