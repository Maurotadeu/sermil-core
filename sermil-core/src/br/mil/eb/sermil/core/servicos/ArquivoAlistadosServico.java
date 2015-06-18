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

import br.mil.eb.sermil.core.dao.CidadaoDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.core.utils.Configurador;
import br.mil.eb.sermil.core.security.CriptoSermil;
import br.mil.eb.sermil.modelo.CidDocApres;
import br.mil.eb.sermil.modelo.Cidadao;
import br.mil.eb.sermil.modelo.Jsm;

/**
 * Processamento da carga de alistados para o SASM.
 *
 * @author Abreu Lopes, Gardino
 * @since 3.4
 * @version $Id: ArquivoAlistadosServico.java 2520 2014-08-21 17:12:11Z anselmo $
 */
@Named("arquivoAlistadosServico")
public class ArquivoAlistadosServico {

	protected static final Logger logger = LoggerFactory.getLogger(ArquivoAlistadosServico.class);

	public static final String EOF = ("\r\n");

	private final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

	@Inject
	private CidadaoDao cidadaoDao;

	public ArquivoAlistadosServico() {
		logger.debug("ArquivoAlistadosServico iniciado");
	}

	public Path gerarArquivo(final Jsm jsm, final Date dataInicial, final Date dataFinal) throws SermilException {
		final Path tempDir = Paths.get(Configurador.getInstance().getConfiguracao("temp.dir"));
		List<Cidadao> lista = null;
		try {
			if (dataInicial == null && dataFinal == null) {
				lista = this.cidadaoDao.findByNamedQuery("Cidadao.listarPorCsmJsm", jsm.getPk().getCsmCodigo(), jsm.getPk().getCodigo());
			} else {
				lista = this.cidadaoDao.findByNamedQuery("Cidadao.listarPorCsmJsmPeriodo", jsm.getPk().getCsmCodigo(), jsm.getPk().getCodigo(), dataInicial, dataFinal);
			}
			if (lista == null || lista.size() == 0) {
				throw new SermilException("Não há alistados no período solicitado.");
			}
			final Path arquivoTexto = Paths.get(
					tempDir.toString(),
					new StringBuilder("1").append(String.format("%02d", jsm.getCsm().getRm().getCodigo())).append(String.format("%02d", jsm.getPk().getCsmCodigo()))
							.append(String.format("%03d", jsm.getPk().getCodigo())).append("000.dat").toString());
			logger.debug("Arquivo MóduloJSM: {}", arquivoTexto.toString());
			try (BufferedWriter bw = Files.newBufferedWriter(arquivoTexto, Charset.forName("UTF-8"))) {
				// 00 CABEÇALHO
				bw.write(("1" + // CODIGO DA TABELA
						String.format("%02d", lista.get(0).getJsm().getCsm().getRm().getCodigo()) + // RM
						String.format("%02d", lista.get(0).getJsm().getPk().getCsmCodigo()) + // CSM
						String.format("%03d", lista.get(0).getJsm().getPk().getCodigo()) + // JSM
						"000" + // NR
						this.sdf.format(new Date()) + // DATA ATUAL
						"000" + // NUMERO GR
						String.format("%07d", lista.size()) + "00004" + EOF));
				for (Cidadao item : lista) {
					// TODO: manter enquanto SASM 3.4.0 não estiver implantado
					if (item.getEscolaridade() > 10) {
						item.setEscolaridade((byte) (item.getEscolaridade() - 1));
					}
					bw.write((String.format("%012d", item.getRa())
							+ String.format("%-70.70s", item.getNome())
							+ (item.getPai() == null ? (String.format("%-70.70s", "NAO DECLARADO")) : (String.format("%-70.70s", item.getPai())))
							+ String.format("%-70.70s", item.getMae())
							+ this.sdf.format(item.getNascimentoData())
							+ (item.getMunicipioNascimento().getCodigo() == null ? (String.format("%08d")) : (String.format("%08d", item.getMunicipioNascimento().getCodigo())))
							+ String.format("%04d", item.getPaisNascimento().getCodigo())
							+ String.format("%-1.1s", item.getEstadoCivil())
							+ String.format("%-1.1s", item.getSexo())
							+ (item.getEscolaridade() == null ? (String.format("%-2.2s", "")) : (String.format("%-2.2s", item.getEscolaridade())))
							+ String.format("%-5.5s", "X1010")
							// Tabela CBO só no SERMIL, SASM utiliza tabela antiga, cod X1010 acima fica como default
							// (item.getOcupacao().getCodigo() == null ?
							// (String.format("%-5.5s","")):(String.format("%-5.5s",item.getOcupacao().getCodigo())))+
							+ (item.getDispensa() == null ? (String.format("%01d", "")) : (String.format("%-1.1s", item.getDispensa())))
							+ // Tipo Alistamento
							String.format("%-1.1s", item.getZonaResidencial())
							+ (item.getMunicipioResidencia().getCodigo() == null ? (String.format("%08d")) : (String.format("%08d", item.getMunicipioResidencia().getCodigo())))
							+ String.format("%04d", item.getPaisResidencia().getCodigo())
							+ (item.getEndereco() == null ? (String.format("%-70.70s", "")) : (String.format("%-70.70s", item.getEndereco())))
							+ (item.getBairro() == null ? (String.format("%-70.70s", "")) : String.format("%-70.70s", item.getBairro()))
							+ (item.getCep() == null ? (String.format("%-8.8s", "")) : (String.format("%-8.8s", item.getCep())))
							+ (item.getMunicipioResidencia().getDdd() == null ? (String.format("%-2.2s", "")) : (String.format("%-2.2s", item.getMunicipioResidencia().getDdd())))
							+ (item.getTelefone() == null ? (String.format("%-8.8s", " ")) : (String.format("%-8.8s", item.getTelefone())))
							+ (item.getCpf() == null ? (String.format("%-11.11s", " ")) : (String.format("%-11.11s", item.getCpf())))
							+ (item.getRg() == null ? (String.format("%-12.12s", " ")) : (String.format("%-12.12s", item.getRg())))
							+ (item.getEmail() == null ? (String.format("%-70.70s", " ")) : (String.format("%-70.70s", item.getEmail()))) + this.sdf.format(item.getAtualizacaoData())
							+ (item.getDesejaServir() == null ? (String.format("%-1.1s", "")) : (String.format("%-1.1s", item.getDesejaServir()))) + this.trataCidDocApres(item) + EOF));
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

	private String trataCidDocApres(final Cidadao cid) {
		String resp = "";
		if (!cid.getCidDocApresColletion().isEmpty()) {
			List<CidDocApres> docs = cid.getCidDocApresColletion();
			resp = this.sdf.format(docs.get(0).getEmissaoData()) + (docs.get(0).getTipo() == null ? (String.format("%-1.1s", "")) : (String.format("%-1.1s", docs.get(0).getTipo())))
					+ (docs.get(0).getPk().getNumero() == null ? (String.format("%-32.32s", "")) : (String.format("%-32.32s", docs.get(0).getPk().getNumero())))
					+ (docs.get(0).getLivro() == null ? (String.format("%-10.10s", "")) : (String.format("%-10.10s", docs.get(0).getLivro())))
					+ (docs.get(0).getFolha() == null ? (String.format("%-10.10s", "")) : (String.format("%-10.10s", docs.get(0).getFolha())))
					+ (docs.get(0).getCartorio() == null ? (String.format("%-30.30s", "")) : (String.format("%-30.30s", docs.get(0).getCartorio())))
					+ (docs.get(0).getMunicipioCodigo() == null ? (String.format("%08d")) : (String.format("%08d", docs.get(0).getMunicipioCodigo())));
		}
		return resp;
	}

}
