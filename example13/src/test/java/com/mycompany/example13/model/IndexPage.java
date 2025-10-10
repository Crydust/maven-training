package com.mycompany.example13.model;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

public class IndexPage {

    private final WebDriver driver;

    @FindBy(linkText = "HelloServlet")
    public WebElement helloServletLink;

    @FindBy(linkText = "TodoJspServlet")
    public WebElement todoJspServletLink;

    public IndexPage(WebDriver driver) {
        this.driver = driver;
    }

    public static IndexPage init(WebDriver driver) {
        final IndexPage page = PageFactory.initElements(driver, IndexPage.class);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(visibilityOf(page.helloServletLink));
        return page;
    }

    public boolean isCurrentPage() {
        return driver.getTitle().equals("Start Page");
    }

    public HelloServletPage clickHelloServletLink() {
        helloServletLink.click();
        return HelloServletPage.init(driver);
    }

    public TodoJspServletPage clickTodoJspServletLink() {
        todoJspServletLink.click();
        return TodoJspServletPage.init(driver);
    }

}
