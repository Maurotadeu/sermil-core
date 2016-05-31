package br.mil.eb.sermil.core.testes.local;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.exceptions.JsmException;
import br.mil.eb.sermil.modelo.Cs;
import br.mil.eb.sermil.modelo.Jsm;
import br.mil.eb.sermil.tipos.TipoTributacao;
import junitparams.Parameters;

public class JsmTest extends AbstractTest {

   protected static final Logger logger = LoggerFactory.getLogger(JsmTest.class);

   @BeforeClass
   public static void setUpBeforeClass() throws Exception {
   }

   @Before
   public void setUp() throws Exception {
   }

   @Test
   @Parameters(method = "getParams")
   public final void verificarTributacao(final Jsm jsm) throws JsmException {
      if (jsm.getTributacao() == TipoTributacao.MNT.ordinal() || jsm.getTributacao() == TipoTributacao.DESATIVADA.ordinal()) {
         assertFalse(jsm.isTributaria());
      } else {
         assertTrue(jsm.isTributaria());
      }
   }

   @Test
   @Parameters(method = "getParams")
   public final void verificarAtivada(final Jsm jsm) throws JsmException {
      if (jsm.getTributacao() != TipoTributacao.DESATIVADA.ordinal()) {
         assertTrue(jsm.isAtiva());
      } else {
         assertFalse(jsm.isAtiva());
      }
   }
   
   @SuppressWarnings("unused")
   private Object[] getParams() {
      final Jsm j0 = new Jsm(Byte.decode("1"), Short.decode("1"));
      j0.setDelsm(Short.decode("1"));
      j0.setDescricao("JSM não tributária");
      j0.setInfor("S");
      j0.setTributacao(Byte.decode("0"));
      final Jsm j1 = new Jsm(Byte.decode("1"), Short.decode("2"));
      final Cs cs = new Cs();
      cs.setCodigo(1);
      j1.setCs(cs);
      j1.setDelsm(Short.decode("1"));
      j1.setDescricao("JSM tributária OFOR");
      j1.setInfor("S");
      j1.setTributacao(Byte.decode("1"));
      final Jsm j2 = new Jsm(Byte.decode("1"), Short.decode("3"));
      j2.setCs(cs);
      j2.setDelsm(Short.decode("1"));
      j2.setDescricao("JSM tributária OMA/TG");
      j2.setInfor("S");
      j2.setTributacao(Byte.decode("2"));
      final Jsm j3 = new Jsm(Byte.decode("1"), Short.decode("4"));
      j3.setCs(cs);
      j3.setDelsm(Short.decode("1"));
      j3.setDescricao("JSM tributária TG");
      j3.setInfor("S");
      j3.setTributacao(Byte.decode("3"));
      final Jsm j4 = new Jsm(Byte.decode("1"), Short.decode("5"));
      j4.setCs(cs);
      j4.setDelsm(Short.decode("1"));
      j4.setDescricao("JSM tributária OMA");
      j4.setInfor("S");
      j4.setTributacao(Byte.decode("4"));
      final Jsm j5 = new Jsm(Byte.decode("1"), Short.decode("6"));
      j5.setDelsm(Short.decode("1"));
      j5.setDescricao("JSM desativada");
      j5.setInfor("N");
      j5.setTributacao(Byte.decode("5"));
      return new Object[]{j0, j1, j2, j3, j4, j5};
   }
   
}
