package br.mil.eb.sermil.core.testes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import br.mil.eb.sermil.core.dao.CepDao;
import br.mil.eb.sermil.core.dao.DaoFactory;

@Configuration
@EnableTransactionManagement
public class ConfigTest {

	public ConfigTest() {
		System.out.println("Entrou em ConfigTeste");
	}

	@Bean
	public CepDao cepDao() {
		return DaoFactory.getInstance(DaoFactory.JPA).getCepDao();
	}

}
