
package br.mil.eb.sermil.core.testes.selenium;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

abstract public class AbstractTest {

	protected WebDriver driver;
	protected String baseUrl;
	protected StringBuffer verificationErrors = new StringBuffer();
	protected boolean acceptNextAlert;
	public static final Integer TEMPO_ESPERA_PAGINA_RESPONDER = 30;

	public AbstractTest() {
		driver = new FirefoxDriver();
      //baseUrl = "https://www.sermilweb.eb.mil.br";
      baseUrl = "http://www.sermilweb.eb.mil.br";
		driver.manage().timeouts().implicitlyWait(AbstractTest.TEMPO_ESPERA_PAGINA_RESPONDER, TimeUnit.SECONDS);
	}

	public void vaiParaInicio() {
		driver.get(baseUrl + "/portal/inicio.action");
	}

	public boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	public String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}

}
