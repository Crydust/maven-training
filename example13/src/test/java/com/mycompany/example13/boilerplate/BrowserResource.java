package com.mycompany.example13.boilerplate;

import java.util.Properties;

import org.apache.commons.pool2.ObjectPool;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import com.mycompany.example13.model.IndexPage;

public class BrowserResource implements BeforeAllCallback, AfterEachCallback {

    private static String baseURL;
    private static ObjectPool<WebDriver> driverPool = null;
    private WebDriver driver;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (driverPool == null) {
            final Properties config = BrowserResourceHelper.readConfiguration();
            baseURL = config.getProperty("baseURL");
            final String browserName = config.getProperty("browserName");
            driverPool = BrowserResourceHelper.createDriverPool(browserName);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        if (driver != null) {
            driverPool.returnObject(driver);
            driver = null;
        }
    }

    private WebDriver getDriver() {
        if (driver == null) {
            try {
                driver = driverPool.borrowObject();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return driver;
    }

    public IndexPage openIndexPage() {
        getDriver().get(baseURL);
        return IndexPage.init(getDriver());
    }

    public String getBaseURL() {
        return baseURL;
    }

    public String getSessionCookie() {
        Cookie cookie = getDriver().manage().getCookieNamed("JSESSIONID");
        return String.format("JSESSIONID=%s; Path=%s%s%s",
                cookie.getValue(),
                cookie.getPath(),
                cookie.isSecure() ? "; Secure" : "",
                cookie.isHttpOnly() ? "; HttpOnly" : "");
    }
}
