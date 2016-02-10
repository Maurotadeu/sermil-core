package selenium.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;

public class Cidadao extends AdminAbstractTest {

   @Test
   public void pesquisarCidadao() throws Exception {
      driver.get(baseUrl + "/portal/jsp/index.jsp");
      driver.findElement(By.linkText("Cidadão")).click();
      driver.findElement(By.cssSelector("li.dropdown.open > ul.dropdown-menu > li > a")).click();
      assertTrue(isElementPresent(By.id("ra")));
      assertTrue(isElementPresent(By.id("cpf")));
      assertTrue(isElementPresent(By.id("idt")));
      assertTrue(isElementPresent(By.id("btPesquisar")));
      assertEquals("Pesquisar", driver.findElement(By.id("btPesquisar")).getText());
      assertTrue(isElementPresent(By.id("nome")));
      assertTrue(isElementPresent(By.id("mae")));
      assertTrue(isElementPresent(By.id("cidadao.nascimentoData")));
   }

   @Test
   public void pesquisarCidaddaoPorCpf() throws Exception {
      driver.get(baseUrl + "/portal/cidadao/pesquisar.action");
      driver.findElement(By.id("cpf")).clear();
      driver.findElement(By.id("cpf")).sendKeys("03354498161");
      driver.findElement(By.id("btPesquisar")).click();
      assertEquals("Pesquisa de Cidadão", driver.findElement(By.cssSelector("div.panel-heading > b")).getText());
      assertTrue(isElementPresent(By.cssSelector("div.panel-heading")));
   }

}
