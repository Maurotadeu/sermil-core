package selenium.admin;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;

import selenium.AbstractTest;

public class AdminAbstractTest extends AbstractTest {

   @Before
   public void setUp() throws Exception {
   }

   @After
   public void tearDown() throws Exception {
      driver.quit();
      String verificationErrorString = verificationErrors.toString();
      if (!"".equals(verificationErrorString)) {
         fail(verificationErrorString);
      }
   }

}
