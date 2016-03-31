package br.mil.eb.sermil.core.testes.selenium.admin;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.By;

/**
 * SelTestCase. Usuario Teste Admin CPF 66666666666 Senha !@#$1234QWERqwer email
 * testeadmin@dsm.eb.mil.br
 * 
 * @author dsmanselmo
 */
public class Entrevista extends AdminAbstractTest {

   @Test
   public void entrevistaJaRealizadaTest() throws Exception {
      driver.get(baseUrl + "/portal/entrevista!inicio.action");
      driver.findElement(By.id("entrevista!entrada_ra")).clear();
      driver.findElement(By.id("entrevista!entrada_ra")).sendKeys("040582159345");
      driver.findElement(By.cssSelector("button.btn.btn-success")).click();
      assertEquals("Entrevista realizada com sucesso.", driver.findElement(By.cssSelector("h4")).getText());
      assertTrue(isElementPresent(By.linkText("Alterar Entrevista")));
   }

}
