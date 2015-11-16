package br.mil.eb.sermil.core.utils;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationMap;
import org.apache.commons.configuration.FileConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.mil.eb.sermil.core.exceptions.SermilException;

/** Carrega arquivo de configuração.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.2.5
 */
public class Configurador {

    protected static final Logger logger = LoggerFactory.getLogger(Configurador.class);

    /** Arquivo de configuração. */
    private static final String ADMIN = "sermil-admin.properties";

    /** Classe interna com instância do Singleton. (padrão Bill Pugh)
     * @author Abreu Lopes
     */
    private static class SingletonHolder {
        /** Instância do singleton. */
        public static final Configurador INSTANCE = new Configurador();
    }

    /** Construtor protegido do singleton. (Classe não pode ser instanciada)*/
    private Configurador() {
        super();
    }

    /** Retorna a intância do singleton.
     * @return Singleton.
     */
    public static Configurador getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /** Obtém a referência para o arquivo de configurações do sistema.
     * @return Arquivo de configurações
     * @throws SermilException erro no processaamento
     */
    public FileConfiguration getFile() throws SermilException {
        try {
            final FileConfiguration config = new PropertiesConfiguration(ADMIN);
            config.setReloadingStrategy(new FileChangedReloadingStrategy());
            return config;
        } catch (ConfigurationException e) {
            logger.error("Erro lendo arquivo de configurações do sistema.", e);
            throw new SermilException(e);
        }
    }

    /** Obtém o valor da propriedade de configuração.
     * @param cfg propriedade de configuração
     * @return Valor da propriedade
     * @throws SermilException erro no processamento
     */
    public String getConfiguracao(final String cfg) throws SermilException {
        return this.getFile().getString(cfg);
    }

    /** Lista de propriedades de configuração armazenada em arquivo.
     * @return Lista de propriedades
     * @throws SermilException erro no processamento
     */
    public Map<String,Object> listar() throws SermilException {
        try {
            final Map<String,Object> cfg = new TreeMap<String,Object>();
            final ConfigurationMap aux = new ConfigurationMap(new PropertiesConfiguration(ADMIN));
            for (Object chave: aux.keySet()) {
                cfg.put((String) chave, aux.get(chave));
            }
            return cfg;
        } catch (ConfigurationException e) {
            throw new SermilException(e);
        }
    }

    /** Salva as propriedades em arquivo.
     * @param cfg mapa com as propriedades
     * @return Mensagem de processamento
     * @throws SermilException erro no processamento
     */
    public String salvar(final Map<String,Object> cfg) throws SermilException {
        try {
            final PropertiesConfiguration aux = new PropertiesConfiguration(ADMIN);
            final ConfigurationMap mapCfg = new ConfigurationMap(new PropertiesConfiguration(ADMIN));
            for (Object chave: mapCfg.keySet()) {
                aux.setProperty((String) chave, cfg.get((String) chave));
            }
            aux.save();
            return "Configurações salvas.";
        } catch (ConfigurationException e) {
            throw new SermilException(e);
        }
    }

}
