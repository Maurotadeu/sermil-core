package selenium.admin;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;

import selenium.AbstractTest;

public class AdminAbstractTest extends AbstractTest {

	@Before
	public void setUp() throws Exception {
		this.login();
	}

	public void login() throws Exception {
		driver.get(baseUrl + "/portal/login.jsp");
		driver.findElement(By.name("j_password")).clear();
		driver.findElement(By.name("j_password")).sendKeys("AR$7wQ$W");
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("99999999999");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	public void logout() throws Exception {
		driver.get(baseUrl + "/portal/inicio.action#");
		driver.findElement(By.linkText("Sair")).click();
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
	}

	@After
	public void tearDown() throws Exception {
		this.login();
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

}
