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

/** Gerencia par�metros do arquivo de configura��o.
 * @author Abreu Lopes
 * @since 3.0
 * @version $Id$
 */
public class Configurador {

    protected static final Logger logger = LoggerFactory.getLogger(Configurador.class);

    /** Arquivo de configura��o. */
    private static final String ADMIN = "sermil-admin.properties";

    /** Classe interna com inst�ncia do Singleton. (padr�o Bill Pugh)
     * @author Abreu Lopes
     */
    private static class SingletonHolder {
        /** Inst�ncia do singleton. */
        public static final Configurador INSTANCE = new Configurador();
    }

    /** Construtor protegido do singleton. (Classe n�o pode ser instanciada)*/
    private Configurador() {
        super();
    }

    /** Retorna a int�ncia do singleton.
     * @return Singleton.
     */
    public static Configurador getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /** Obt�m a refer�ncia para o arquivo de configura��es do sistema.
     * @return Arquivo de configura��es
     * @throws SermilException erro no processaamento
     */
    public FileConfiguration getFile() throws SermilException {
       try {
           final FileConfiguration config = new PropertiesConfiguration(ADMIN);
           config.setReloadingStrategy(new FileChangedReloadingStrategy());
           return config;
       } catch (ConfigurationException e) {
           logger.error("Erro lendo arquivo de configura��es do sistema.", e);
           throw new SermilException(e);
       }
   }
    
    

    /**
     * Sobrecarregando este metodo para poder trabalhar com outros Bundles
     * alem do sermil-admin.properties
     * 
     * @author Anselmo S Ribeiro
     * @param fileName
     * @return
     * @throws SermilException
     */
    public FileConfiguration getFile(String fileName) throws SermilException {
        try {
            final FileConfiguration config = new PropertiesConfiguration(fileName);
            config.setReloadingStrategy(new FileChangedReloadingStrategy());
            return config;
        } catch (ConfigurationException e) {
            logger.error("Erro lendo arquivo de configura��es do sistema.", e);
            throw new SermilException(e);
        }
    }
    
    

    /** Obt�m o valor da propriedade de configura��o.
     * @param cfg propriedade de configura��o
     * @return Valor da propriedade
     * @throws SermilException erro no processamento
     */
    public String getConfiguracao(final String cfg) throws SermilException {
       return this.getFile().getString(cfg);
   }
    
    
    

    /**
     * Sobrecarregando este metodo para poder escolher outros
     * bundles alem do sermil-admin.properties
     * 
     * @author Anselmo S Ribeiro
     * @param bundleName
     * @param cfg
     * @return
     * @throws SermilException
     */
    public String getConfiguracao(final String bundleName, final String key)  {
        try {
         return this.getFile(bundleName).getString(key);
      } catch (SermilException e) {
         logger.error("Nao foi possivel achar o arquivo "+ bundleName + " com a key: " + key );
         return key;
      }
    }
    

    /**
     * Metodo para trazer os valores especificos de sermil.properties
     * @author Anselmo S Ribeiro
     * @param key
     * @return
     */
    public static String getText(String key){
       return getInstance().getConfiguracao("sermil.properties", key);
    }
    

    /** Lista de propriedades de configura��o armazenada em arquivo.
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
            return "Configura��es salvas.";
        } catch (ConfigurationException e) {
            throw new SermilException(e);
        }
    }

}
