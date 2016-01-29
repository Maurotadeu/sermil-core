package sel;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * SelTestCase. Usuario Teste Admin CPF 66666666666 Senha !@#$1234QWERqwer email
 * testeadmin@dsm.eb.mil.br
 * 
 * @author dsmanselmo
 */
public class SelTestCase {
   private WebDriver driver;
   private String baseUrl;
   private boolean acceptNextAlert = true;
   private StringBuffer verificationErrors = new StringBuffer();

   @Before
   public void setUp() throws Exception {
      driver = new FirefoxDriver();
      baseUrl = "http://localhost:8080";
      driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
      loginAdmin();
   }

   private void loginAdmin() {
      driver.get(baseUrl + "/portal/login.jsp");
      driver.findElement(By.name("j_password")).clear();
      driver.findElement(By.name("j_password")).sendKeys("!@#$1234QWERqwer");
      driver.findElement(By.id("j_username")).clear();
      driver.findElement(By.id("j_username")).sendKeys("66666666666");
      driver.findElement(By.xpath("//button[@type='submit']")).click();
   }

   private void logoffAdmin() {
      try {
         driver.get(baseUrl + "/portal/j_spring_security_logout");
      } catch (Exception e) {
         fail(e.getMessage());
      }
   }

   private void vaiParaInicio() {
      driver.get(baseUrl + "/portal/inicio.action");
   }

   @Test
   public void entrevistaJaRealizadaTest() throws Exception {
      driver.get(baseUrl + "/portal/entrevista!inicio.action");
      driver.findElement(By.id("entrevista!entrada_ra")).clear();
      driver.findElement(By.id("entrevista!entrada_ra")).sendKeys("040582159345");
      driver.findElement(By.cssSelector("button.btn.btn-success")).click();
      driver.findElement(By.cssSelector("div.modal-footer > button.btn.btn-default")).click();
      assertTrue(StringUtils.contains(driver.findElement(By.cssSelector("div.alert.alert-info")).getText(), "A entrevista com este cidadão já foi realizada. Para manter a atual entrevista clique em Cancelar. Para alterar clique em Continuar."));
      assertFalse(StringUtils.contains(driver.findElement(By.cssSelector("div.alert.alert-info")).getText(), "A entrevista com este cidadão não foi realizada. Para manter a atual entrevista clique em Cancelar. Para alterar clique em Continuar."));
      vaiParaInicio();
      Thread.sleep(2000);
   }

   @After
   public void tearDown()  {
      logoffAdmin();
      driver.quit();
      String verificationErrorString = verificationErrors.toString();
      if (!"".equals(verificationErrorString)) {
         fail(verificationErrorString);
      }
   }

   private boolean isElementPresent(By by) {
      try {
         driver.findElement(by);
         return true;
      } catch (NoSuchElementException e) {
         return false;
      }
   }

   private boolean isAlertPresent() {
      try {
         driver.switchTo().alert();
         return true;
      } catch (NoAlertPresentException e) {
         return false;
      }
   }

   private String closeAlertAndGetItsText() {
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
