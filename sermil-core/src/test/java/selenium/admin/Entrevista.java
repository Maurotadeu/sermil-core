package selenium.admin;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.openqa.selenium.By;

import selenium.SermilTest;

/**
 * SelTestCase. Usuario Teste Admin CPF 66666666666 Senha !@#$1234QWERqwer email
 * testeadmin@dsm.eb.mil.br
 * 
 * @author dsmanselmo
 */
public class Entrevista extends SermilTest{

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


}
