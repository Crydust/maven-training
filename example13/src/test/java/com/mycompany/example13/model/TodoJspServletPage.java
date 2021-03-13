package com.mycompany.example13.model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.ui.Quotes;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class TodoJspServletPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "label")
    public WebElement labelText;

    @FindBy(id = "button_add")
    public WebElement addButton;

    @FindBy(id = "button_save")
    public WebElement saveButton;

    @FindBy(id = "items_empty")
    public WebElement empty;

    @FindBy(id = "items")
    public WebElement itemsTable;

    public TodoJspServletPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 10L);
    }

    public static TodoJspServletPage init(WebDriver driver) {
        final TodoJspServletPage page = PageFactory.initElements(driver, TodoJspServletPage.class);
        page.wait.until(visibilityOf(page.addButton));
        return page;
    }

    public boolean isCurrentPage() {
        return driver.getTitle().equals("Servlet TodoJspServlet");
    }

    public TodoJspServletPage add(String text) {
        labelText.clear();
        labelText.sendKeys(text);
        addButton.click();
        waitUntilVisibilityOfItemLabel(text);
        return this;
    }

    public boolean contains(String text) {
        final String xpath = String.format("//*[@id = 'items']//label[normalize-space(text()) = %s]", Quotes.escape(text));
        return !driver.findElements(By.xpath(xpath)).isEmpty();
    }

    public TodoJspServletPage remove(String text) {
        final String xpath = String.format("//*[@id = 'items']//tr[normalize-space(.//label/text()) = %s]//button[normalize-space(text()) = 'Remove']", Quotes.escape(text));
        final WebElement removeButton = itemsTable.findElement(By.xpath(xpath));
        removeButton.click();
        waitUntilInvisibilityOfItemLabel(text);
        return this;
    }

    public boolean isDone(String text) {
        final WebElement checkbox = itemsTable.findElement(new ByChained(
                By.xpath(String.format("//*[@id = 'items']//tr[normalize-space(.//label/text()) = %s]", Quotes.escape(text))),
                By.cssSelector("input[type='checkbox']")
        ));
        return checkbox.isSelected();
    }

    public TodoJspServletPage setDone(String text, boolean done) {
        final WebElement checkbox = itemsTable.findElement(new ByChained(
                By.xpath(String.format("//*[@id = 'items']//tr[normalize-space(.//label/text()) = %s]", Quotes.escape(text))),
                By.cssSelector("input[type='checkbox']")
        ));
        if (checkbox.isSelected() != done) {
            checkbox.click();
            saveButton.click();
            wait.until(stalenessOf(checkbox));
        }
        return this;
    }

    private void waitUntilVisibilityOfItemLabel(String text) {
        final String labelXpath = String.format("//*[@id = 'items']//label[normalize-space(text()) = %s]", Quotes.escape(text));
        wait.until(visibilityOfElementLocated(By.xpath(labelXpath)));
    }

    private void waitUntilInvisibilityOfItemLabel(String text) {
        final String labelXpath = String.format("//*[@id = 'items']//label[normalize-space(text()) = %s]", Quotes.escape(text));
        wait.until(invisibilityOfElementLocated(By.xpath(labelXpath)));
    }

}
