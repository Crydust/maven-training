package com.mycompany.example13.boilerplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class Configuration {
    private final String baseURL;
    private final String browserName;
    private final int maxBrowserInstances;

    private Configuration(String baseURL, String browserName, int maxBrowserInstances) {
        this.baseURL = baseURL;
        this.browserName = browserName;
        this.maxBrowserInstances = maxBrowserInstances;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public String getBrowserName() {
        return browserName;
    }

    public int getMaxBrowserInstances() {
        return maxBrowserInstances;
    }

    public static Configuration readConfiguration() {
        try {
            final Properties properties = new Properties();
            try (InputStream in = Configuration.class.getResourceAsStream("/example13.properties")) {
                properties.load(in);
            }

            final String baseURL = properties.getProperty("baseURL");
            if (baseURL == null || baseURL.isEmpty()) {
                throw new IllegalArgumentException("Invalid baseURL " + baseURL);
            }

            final String browserName = properties.getProperty("browserName", "htmlunit");
            if (browserName == null || !Arrays.asList("chrome", "edge", "firefox", "htmlunit", "ie", "opera", "safari").contains(browserName)) {
                throw new IllegalArgumentException("Invalid browserName " + browserName);
            }

            final int maxBrowserInstances;
            if ("ie".equals(browserName)) {
                maxBrowserInstances = 1;
            } else {
                maxBrowserInstances = Integer.parseInt(properties.getProperty("maxBrowserInstances", "1"));
            }
            if (maxBrowserInstances < 1) {
                throw new IllegalArgumentException("Invalid maxBrowserInstances " + maxBrowserInstances);
            }

            return new Configuration(baseURL, browserName, maxBrowserInstances);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
