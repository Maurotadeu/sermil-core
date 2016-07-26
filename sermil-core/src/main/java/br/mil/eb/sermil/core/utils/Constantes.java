package br.mil.eb.sermil.core.utils;

import java.util.Locale;

/** Constantes simbólicas do sistema.
 * @author Abreu Lopes
 * @since 3.0
 * @version 5.4.5
 */
public final class Constantes {

    /** Localidade Geográfica. */
    public static final Locale LOCAL = new Locale("pt", "BR");

    /** Conta SMTP. */
    public static final String SUPORTE_CONTA_SMTP = "suporte.conta.smtp";

    /** Conta de E-mail. */
    public static final String SUPORTE_CONTA_EMAIL = "suporte.conta.email";

    /** Diretório temporário. */
    public static final String TEMP_DIR = "temp.dir";

    /** Expressão regular para validar e-mail. */
    public static final String EMAIL_REGEXP = "^([a-zA-Z0-9_\\.\\-\\+])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";

    /** Expressão regular para validar JPEG. */
    public static final String JPG_REGEXP = "([^\\s]+(\\.(?i)(jpg))$)";

    /** Arquivo de Log. */
    public static final String LOG_FILE = "logFile";

    /** Expressão regular para validar senha: mínimo 6 e máximo 30 caracteres, ao menos um dígito, ao menos um caracter minúsculo, ao menos um maiúsculo e um caracter especial. */
    public static final String SENHA_REGEXP = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!&*]).{6,30})";

    /** RA Emergencial Máximo. */
    public static final String RA_EMERGENCIAL_MAXIMO = "ra.emergencial.maximo";
    
    /** Construtor protegido. */
    private Constantes() {
        // Classe utilitária
    }

}
