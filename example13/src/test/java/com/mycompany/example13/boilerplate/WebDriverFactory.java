package com.mycompany.example13.boilerplate;

import static org.openqa.selenium.support.ui.ExpectedConditions.urlToBe;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

class WebDriverFactory extends BasePooledObjectFactory<WebDriver> {
    private final String browserName;

    WebDriverFactory(String browserName) {
        this.browserName = browserName;
    }

    @Override
    public WebDriver create() {
        return createWebDriver(browserName);
    }

    @Override
    public PooledObject<WebDriver> wrap(WebDriver driver) {
        return new DefaultPooledObject<>(driver);
    }

    @Override
    public boolean validateObject(PooledObject<WebDriver> p) {
        // not valid if there are popup windows open
        return p.getObject().getWindowHandles().size() == 1;
    }

    @Override
    public void activateObject(PooledObject<WebDriver> p) {
        final WebDriver driver = p.getObject();
        driver.get("about:blank");
        new WebDriverWait(driver, 10).until(urlToBe("about:blank"));
        driver.manage().deleteAllCookies();
    }

    @Override
    public void destroyObject(PooledObject<WebDriver> p) {
        final WebDriver driver = p.getObject();
        if (driver != null) {
            driver.quit();
        }
    }

    static ObjectPool<WebDriver> createDriverPool(String browserName, int maxBrowserInstances) {
        final ObjectPool<WebDriver> pool = new GenericObjectPool<>(new WebDriverFactory(browserName), createGenericObjectPoolConfig(maxBrowserInstances));
        Runtime.getRuntime().addShutdownHook(new Thread(pool::close, "CloseDriverPool-thread"));
        return pool;
    }

    private static GenericObjectPoolConfig<WebDriver> createGenericObjectPoolConfig(int maxBrowserInstances) {
        final GenericObjectPoolConfig<WebDriver> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setJmxEnabled(false);
        poolConfig.setMaxIdle(maxBrowserInstances);
        poolConfig.setMaxTotal(maxBrowserInstances);
        poolConfig.setMinIdle(0);
        poolConfig.setTestOnReturn(true);
        return poolConfig;
    }

    private static WebDriver createWebDriver(String browserName) {
        switch (browserName) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver();
            case "edge":
                WebDriverManager.edgedriver().setup();
                return new EdgeDriver();
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver();
            case "htmlunit":
                return new HtmlUnitDriver();
            case "ie":
                WebDriverManager.iedriver().setup();
                return new InternetExplorerDriver();
            case "opera":
                WebDriverManager.operadriver().setup();
                return new OperaDriver();
            case "safari":
                return new SafariDriver();
            default:
                throw new RuntimeException("unknown browserName");
        }
    }
}
