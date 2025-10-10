package com.mycompany.example13.model;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

public class HelloServletPage {

    private final WebDriver driver;

    @FindBy(id = "nickname")
    public WebElement nickname;

    @FindBy(id = "nicknameOutput")
    public WebElement nicknameOutput;

    public HelloServletPage(WebDriver driver) {
        this.driver = driver;
    }

    public static HelloServletPage init(WebDriver driver) {
        final HelloServletPage page = PageFactory.initElements(driver, HelloServletPage.class);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(visibilityOf(page.nickname));
        return page;
    }

    public boolean isCurrentPage() {
        return driver.getTitle().equals("Servlet HelloServlet");
    }

    public HelloServletPage sayHello(String name) {
        nickname.clear();
        nickname.sendKeys(name);
        nickname.submit();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(visibilityOf(nicknameOutput));
        return this;
    }

    public String getNicknameOutput() {
        return nicknameOutput.getText();
    }
}
