package br.mil.eb.sermil.core.servicos;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.dao.JsmDao;
import br.mil.eb.sermil.core.exceptions.CriterioException;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.core.security.CriptoSermil;
import br.mil.eb.sermil.core.utils.Configurador;
import br.mil.eb.sermil.modelo.Jsm;

/** Geração do arquivo de carga de alistados para entrada no SASM.
 * @author Abreu Lopes, Gardino
 * @since 3.4
 * @version 5.2.3
 */
@Named("arquivoAlistadosServico")
public class ArquivoAlistadosServico {

	protected static final Logger logger = LoggerFactory.getLogger(ArquivoAlistadosServico.class);

	private static final String SQL =
      "SELECT TO_CHAR(ra,'FM000000000000')||RPAD(nome,70)||RPAD(pai,70)||RPAD(mae,70)||TO_CHAR(nascimento_data,'DDMMYYYY')||" +
      "LPAD(municipio_nascimento_codigo,8,'0')||LPAD(pais_nascimento_codigo,4,'0')||" +
      "estado_civil||sexo||RPAD(escolaridade,2)||'X1010'||DECODE(dispensa,1,1,2,2,3,3,4,4,5,5,15,6,0)||zona_residencial||" +
      "LPAD(municipio_residencia_codigo,8,'0')||LPAD(pais_residencia_codigo,4,'0')||" +
      "RPAD(NVL(endereco,' '),70)||RPAD(NVL(bairro,' '),70)||LPAD(NVL(cep,' '),8,'0')||SUBSTR(m.ddd,1,2)||" +
      "RPAD(SUBSTR(NVL(telefone,' '),1,8),8)||RPAD(NVL(cpf,' '),11)||RPAD(SUBSTR(NVL(rg,' '),1,12),12)||RPAD(NVL(email,' '),70)||TO_CHAR(e.data,'DDMMYYYY')||deseja_servir|| " +
      "TO_CHAR(d.emissao_data,'DDMMYYYY')||d.tipo||RPAD(NVL(d.numero,' '),32)||RPAD(NVL(d.livro,' '),10)||RPAD(NVL(d.folha,' '),10)||RPAD(NVL(d.cartorio,' '),30)||LPAD(municipio_codigo,8,'0') " +
      "FROM cidadao c JOIN cid_evento e ON c.ra = e.cidadao_ra JOIN municipio m ON c.municipio_residencia_codigo = m.codigo LEFT JOIN cid_doc_apres d ON c.ra = d.cidadao_ra WHERE c.csm_codigo = ? AND c.jsm_codigo = ? " +
      "AND e.codigo = 1 AND e.data BETWEEN TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(?,'DD/MM/YYYY')";

	@Inject
	private JsmDao jsmDao;

	public ArquivoAlistadosServico() {
		logger.debug("ArquivoAlistadosServico iniciado");
	}

	public Path gerarArquivo(final Jsm jsm, final Date dataInicial, final Date dataFinal) throws SermilException {
		final Path tempDir = Paths.get(Configurador.getInstance().getConfiguracao("temp.dir"));
		List<Object[]> lista = null;
		try {
			if (dataInicial == null && dataFinal == null) {
				throw new CriterioException("Informe a data inicial e final de alistamento.");
			} else {
				lista = this.jsmDao.findBySQL(SQL, jsm.getPk().getCsmCodigo(), jsm.getPk().getCodigo(), new SimpleDateFormat("dd/MM/yyyy").format(dataInicial), new SimpleDateFormat("dd/MM/yyyy").format(dataFinal));
			}
			if (lista == null || lista.size() == 0) {
				throw new SermilException("Não há alistados no período informado.");
			}
			final Jsm j = this.jsmDao.findById(jsm.getPk());
			final StringBuilder arqId = new StringBuilder("1")                     // Tipo
					.append(String.format("%02d", j.getCsm().getRm().getCodigo())) // RM
					.append(String.format("%02d", j.getPk().getCsmCodigo()))       // CSM
					.append(String.format("%03d", j.getPk().getCodigo()))          // JSM
					.append("000");                                                // NR ARQ
			
			final Path arquivoTexto = Paths.get(tempDir.toString(), new StringBuilder(arqId).append(".dat").toString());
			logger.info("Arquivo MóduloJSM: {}", arquivoTexto.toString());
			try (BufferedWriter bw = Files.newBufferedWriter(arquivoTexto, Charset.forName("UTF-8"))) {
				final StringBuilder cab = new StringBuilder(arqId)
						.append(new SimpleDateFormat("ddMMyyyy").format(new Date())) // DATA ATUAL
						.append("000")                                                 // NUMERO GR
						.append(String.format("%07d", lista.size()) + "00004");        // VERSAO
                logger.info("cab: {}", cab);
                bw.write(cab.toString());
                bw.newLine();
				for (Object item : lista) {
					logger.info("info: {}", item);
                    bw.write((String) item);
                    bw.newLine();
				}
				bw.flush();
			} catch (IOException e) {
				throw e;
			}
			final Path arquivoCripto = Paths.get(tempDir.toString(), arquivoTexto.getFileName().toString().replace(".dat", ".cta"));
			CriptoSermil.executar(arquivoTexto.toFile(), arquivoCripto.toFile(), 2007);
			Files.delete(arquivoTexto);
			return arquivoCripto;
		} catch (Exception e) {
			throw new SermilException(e.getMessage());
		}
	}

}

/*						
	private String trataCidDocApres(final Cidadao cid) {
		String resp = "";
		if (!cid.getCidDocApresColletion().isEmpty()) {
			List<CidDocApres> docs = cid.getCidDocApresColletion();
			resp = this.sdf.format(docs.get(0).getEmissaoData())
					+ (docs.get(0).getTipo() == null ? (String.format("%-1.1s", "")) : (String.format("%-1.1s", docs.get(0).getTipo())))
					+ (docs.get(0).getPk().getNumero() == null ? (String.format("%-32.32s", "")) : (String.format("%-32.32s", docs.get(0).getPk().getNumero())))
					+ (docs.get(0).getLivro() == null ? (String.format("%-10.10s", "")) : (String.format("%-10.10s", docs.get(0).getLivro())))
					+ (docs.get(0).getFolha() == null ? (String.format("%-10.10s", "")) : (String.format("%-10.10s", docs.get(0).getFolha())))
					+ (docs.get(0).getCartorio() == null ? (String.format("%-30.30s", "")) : (String.format("%-30.30s", docs.get(0).getCartorio())))
					+ (docs.get(0).getMunicipioCodigo() == null ? (String.format("%08d")) : (String.format("%08d", docs.get(0).getMunicipioCodigo())));
		}
		return resp;
	}

				
				bw.write(("1" + // CODIGO DA TABELA
						String.format("%02d", lista.get(0).getJsm().getCsm().getRm().getCodigo()) + // RM
						String.format("%02d", lista.get(0).getJsm().getPk().getCsmCodigo()) + // CSM
						String.format("%03d", lista.get(0).getJsm().getPk().getCodigo()) + // JSM
						"000" + // NR
						this.sdf.format(new Date()) + // DATA ATUAL
						"000" + // NUMERO GR
						String.format("%07d", lista.size()) + "00004" + EOF));

bw.write( String.format("%012d", item.getRa())
		+ String.format("%-70.70s", item.getNome())
		+ String.format("%-70.70s", item.getPai()))
		+ String.format("%-70.70s", item.getMae())
		+ this.sdf.format(item.getNascimentoData())
		+ String.format("%08d", item.getMunicipioNascimento().getCodigo())
		+ String.format("%04d", item.getPaisNascimento().getCodigo())
		+ String.format("%-1.1s", item.getEstadoCivil())
		+ String.format("%-1.1s", item.getSexo())
		+ String.format("%-2.2s", item.getEscolaridade())
		+ String.format("%-5.5s", "X1010")
		// Tabela CBO só no SERMIL, SASM utiliza tabela antiga, cod X1010 acima fica como default
		// (item.getOcupacao().getCodigo() == null ?
		// (String.format("%-5.5s","")):(String.format("%-5.5s",item.getOcupacao().getCodigo())))+
		+ String.format("%-1.1s", item.getDispensa())
		+ String.format("%-1.1s", item.getZonaResidencial())
		+ String.format("%08d", item.getMunicipioResidencia().getCodigo())
		+ String.format("%04d", item.getPaisResidencia().getCodigo())
		+ (item.getEndereco() == null ? (String.format("%-70.70s", "")) : (String.format("%-70.70s", item.getEndereco())))
		+ (item.getBairro() == null ? (String.format("%-70.70s", "")) : String.format("%-70.70s", item.getBairro()))
		+ (item.getCep() == null ? (String.format("%-8.8s", "")) : (String.format("%-8.8s", item.getCep())))
		+ (item.getMunicipioResidencia().getDdd() == null ? (String.format("%-2.2s", "")) : (String.format("%-2.2s", item.getMunicipioResidencia().getDdd())))
		+ (item.getTelefone() == null ? (String.format("%-8.8s", " ")) : (String.format("%-8.8s", item.getTelefone())))
		+ (item.getCpf() == null ? (String.format("%-11.11s", " ")) : (String.format("%-11.11s", item.getCpf())))
		+ (item.getRg() == null ? (String.format("%-12.12s", " ")) : (String.format("%-12.12s", item.getRg())))
		+ (item.getEmail() == null ? (String.format("%-70.70s", " ")) : (String.format("%-70.70s", item.getEmail()))) + this.sdf.format(item.getAtualizacaoData())
		+ (item.getDesejaServir() == null ? (String.format("%-1.1s", "")) : (String.format("%-1.1s", item.getDesejaServir()))) + this.trataCidDocApres(item) + EOF);
*/

// SASM 3.3.0 já contempla 9º Ano
//if (item.getEscolaridade() > 10) {
//	item.setEscolaridade((byte) (item.getEscolaridade() - 1));
//}
/*
StringBuilder info = new StringBuilder(String.format("%012d", item.getRa()))
		.append(String.format("%-70.70s", item.getNome()))
		.append(String.format("%-70.70s", item.getPai()))
		.append(String.format("%-70.70s", item.getMae()))
		.append(this.sdf.format(item.getNascimentoData()))
		.append(String.format("%08d", item.getMunicipioNascimento().getCodigo()))
		.append(String.format("%04d", item.getPaisNascimento().getCodigo()))
		.append(String.format("%-1.1s", item.getEstadoCivil()))
		.append(String.format("%-1.1s", item.getSexo()))
		.append(String.format("%-2.2s", item.getEscolaridade()))
		.append(String.format("%-5.5s", "X1010"))
		.append(String.format("%-1.1s", item.getDispensa()))
		.append(String.format("%-1.1s", item.getZonaResidencial()))
		.append(String.format("%08d", item.getMunicipioResidencia().getCodigo()))
		.append(String.format("%04d", item.getPaisResidencia().getCodigo()))
		.append((item.getEndereco() == null ? (String.format("%-70.70s", "")) : (String.format("%-70.70s", item.getEndereco()))))
		.append((item.getBairro() == null ? (String.format("%-70.70s", "")) : String.format("%-70.70s", item.getBairro())))
		.append((item.getCep() == null ? (String.format("%-8.8s", "")) : (String.format("%-8.8s", item.getCep()))))
		.append((item.getMunicipioResidencia().getDdd() == null ? (String.format("%-2.2s", "")) : (String.format("%-2.2s", item.getMunicipioResidencia().getDdd()))))
		.append((item.getTelefone() == null ? (String.format("%-8.8s", " ")) : (String.format("%-8.8s", item.getTelefone()))))
		.append((item.getCpf() == null ? (String.format("%-11.11s", " ")) : (String.format("%-11.11s", item.getCpf()))))
		.append((item.getRg() == null ? (String.format("%-12.12s", " ")) : (String.format("%-12.12s", item.getRg()))))
		.append((item.getEmail() == null ? (String.format("%-70.70s", " ")) : (String.format("%-70.70s", item.getEmail()))) + this.sdf.format(item.getAtualizacaoData()))
		.append((item.getDesejaServir() == null ? (String.format("%-1.1s", "")) : (String.format("%-1.1s", item.getDesejaServir()))) + this.trataCidDocApres(item) + EOF);
	*/	
