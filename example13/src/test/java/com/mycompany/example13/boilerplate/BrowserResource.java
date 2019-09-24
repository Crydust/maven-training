package com.mycompany.example13.boilerplate;

import com.mycompany.example13.model.IndexPage;
import org.apache.commons.pool2.ObjectPool;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.Properties;

public class BrowserResource implements BeforeEachCallback, AfterEachCallback {

    private static final String baseURL;
    private static final ObjectPool<WebDriver> driverPool;
    private WebDriver driver;

    static {
        final Properties config;
        try {
            config = BrowserResourceHelper.readConfiguration();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        baseURL = config.getProperty("baseURL");
        final String browserName = config.getProperty("browserName", "htmlunit");
        final int maxBrowserInstances = Integer.parseInt(config.getProperty("maxBrowserInstances", "1"));
        driverPool = BrowserResourceHelper.createDriverPool(browserName, maxBrowserInstances);
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        driver = driverPool.borrowObject();
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        driverPool.returnObject(driver);
    }

    private WebDriver getDriver() {
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
