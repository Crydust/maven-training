package com.mycompany.example13.boilerplate;

import static org.openqa.selenium.support.ui.ExpectedConditions.urlToBe;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

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

class BrowserResourceHelper {

    static Properties readConfiguration() throws IOException {
        final Properties config = new Properties();
        try (InputStream in = BrowserResource.class.getResourceAsStream("/example13.properties")) {
            config.load(in);
        }
        return config;
    }

    static ObjectPool<WebDriver> createDriverPool(String browserName) {
        final ObjectPool<WebDriver> pool = new GenericObjectPool<>(new WebDriverFactory(browserName), createConfig());
        Runtime.getRuntime().addShutdownHook(new Thread(pool::close, "CloseDriverPool-thread"));
        return pool;
    }

    private static GenericObjectPoolConfig<WebDriver> createConfig() {
        final GenericObjectPoolConfig<WebDriver> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxIdle(1);
        poolConfig.setMaxTotal(1);
        poolConfig.setMinIdle(0);
        return poolConfig;
    }

    private static class WebDriverFactory extends BasePooledObjectFactory<WebDriver> {
        private final String browserName;

        private WebDriverFactory(String browserName) {
            this.browserName = browserName;
        }

        @Override
        public WebDriver create() {
            return createWebDriver(browserName);
        }

        @Override
        public PooledObject<WebDriver> wrap(WebDriver obj) {
            return new DefaultPooledObject<>(obj);
        }

        @Override
        public void activateObject(PooledObject<WebDriver> p) {
            final WebDriver driver = p.getObject();

            String originalHandle = driver.getWindowHandle();
            final Set<String> windowHandles = driver.getWindowHandles();
            if (windowHandles.size() > 1) {
                for (String handle : windowHandles) {
                    if (!handle.equals(originalHandle)) {
                        driver.switchTo().window(handle);
                        driver.close();
                    }
                }
                driver.switchTo().window(originalHandle);
            }

            driver.get("about:blank");
            new WebDriverWait(driver, 10).until(urlToBe("about:blank"));

            driver.manage().deleteAllCookies();
        }

        @Override
        public void destroyObject(PooledObject<WebDriver> pooledObject) {
            pooledObject.getObject().quit();
        }
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
