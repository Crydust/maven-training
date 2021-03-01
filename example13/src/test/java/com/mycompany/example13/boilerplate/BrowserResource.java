package com.mycompany.example13.boilerplate;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.pool2.ObjectPool;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import com.mycompany.example13.model.IndexPage;

public class BrowserResource implements AfterEachCallback {

    private static final String baseURL;
    private static final String browserName;
    private static final int maxBrowserInstances;
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
        browserName = config.getProperty("browserName", "htmlunit");
        if ("ie".equals(browserName)) {
            maxBrowserInstances = 1;
        } else {
            maxBrowserInstances = Integer.parseInt(config.getProperty("maxBrowserInstances", "1"));
        }
        driverPool = BrowserResourceHelper.createDriverPool(browserName, maxBrowserInstances);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        if (driver != null) {
            driverPool.returnObject(driver);
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

    public boolean getDriverOnlySupportsCharactersInTheBMP() {
        switch (browserName) {
            case "chrome": /* falls through */
            case "edge":
                return true;
            case "firefox": /* falls through */
            case "htmlunit": /* falls through */
            case "ie": /* falls through */
            case "opera": /* falls through */
            case "safari": /* falls through */
            default:
                return false;
        }
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
