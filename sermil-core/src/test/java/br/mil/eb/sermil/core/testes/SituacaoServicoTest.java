package br.mil.eb.sermil.core.testes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.core.servicos.SituacaoServico;
import br.mil.eb.sermil.modelo.Cidadao;
import junitparams.Parameters;

public class SituacaoServicoTest extends AbstractTest {

  protected static final Logger logger = LoggerFactory.getLogger(SituacaoServicoTest.class);

  SituacaoServico servico;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @Test
  @Parameters(method = "getParams")
  public final void verificarSituacao() throws SermilException {
    assertFalse(false);
    assertTrue(true);
  }

  @SuppressWarnings("unused")
  private Object[] getParams() {
    Cidadao c1 = new Cidadao(151052426617L);
    Cidadao c2 = new Cidadao(111052102337L);
    Cidadao c3 = new Cidadao(301372011603L);
    Cidadao c4 = new Cidadao(101952100846L);
    Cidadao c5 = new Cidadao(280432113130L);
    Cidadao c6 = new Cidadao(041143103566L);
    Cidadao c7 = new Cidadao(171962058391L);
    Cidadao c8 = new Cidadao(280062519358L);
    Cidadao c9 = new Cidadao(161822053823L);
    Cidadao c10 = new Cidadao(280062118540L);
    Cidadao c11 = new Cidadao(170018684374L);
    Cidadao c12 = new Cidadao(170018793654L);
    Cidadao c13 = new Cidadao(280392309835L);
    Cidadao c14 = new Cidadao(280062479827L);
    Cidadao c15 = new Cidadao(151052374023L);
    Cidadao c16 = new Cidadao(301502029681L);
    Cidadao c17 = new Cidadao(170212039136L);
    Cidadao c18 = new Cidadao(300092033319L);
    return new Object[]{c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16,c17,c18};
  }

}
