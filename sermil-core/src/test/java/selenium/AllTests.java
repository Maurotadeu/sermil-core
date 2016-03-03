package selenium;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import selenium.admin.AdminTestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ AdminTestSuite.class })
public class AllTests {

}
