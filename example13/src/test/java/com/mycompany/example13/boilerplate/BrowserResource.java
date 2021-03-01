package com.mycompany.example13.boilerplate;

import static com.mycompany.example13.boilerplate.WebDriverFactory.createDriverPool;

import org.apache.commons.pool2.ObjectPool;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import com.mycompany.example13.model.IndexPage;

public class BrowserResource implements AfterEachCallback {

    private static final Configuration configuration = Configuration.readConfiguration();
    private static final ObjectPool<WebDriver> driverPool = createDriverPool(configuration.getBrowserName(), configuration.getMaxBrowserInstances());
    private WebDriver driver;

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
        switch (configuration.getBrowserName()) {
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
        getDriver().get(configuration.getBaseURL());
        return IndexPage.init(getDriver());
    }

    public String getBaseURL() {
        return configuration.getBaseURL();
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
