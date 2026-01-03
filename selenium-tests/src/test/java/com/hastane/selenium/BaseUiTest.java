package com.hastane.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;

public class BaseUiTest {

    protected WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = new SafariDriver();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }
}
