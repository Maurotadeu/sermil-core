package br.mil.eb.sermil.core.testes.selenium;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import br.mil.eb.sermil.core.testes.selenium.admin.AdminTestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ AdminTestSuite.class })
public class AllTests {

}
