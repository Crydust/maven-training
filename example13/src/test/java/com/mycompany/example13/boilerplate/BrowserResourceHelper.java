package com.mycompany.example13.boilerplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
        public void passivateObject(PooledObject<WebDriver> p) {
            final WebDriver driver = p.getObject();
            if (driver != null) {
                driver.get("about:blank");
                driver.manage().deleteAllCookies();
            }
        }

        @Override
        public void destroyObject(PooledObject<WebDriver> pooledObject) {
            final WebDriver driver = pooledObject.getObject();
            if (driver != null) {
                driver.quit();
            }
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
