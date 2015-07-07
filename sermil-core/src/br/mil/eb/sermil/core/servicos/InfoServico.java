package br.mil.eb.sermil.core.servicos;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.DstbExclusaoDao;
import br.mil.eb.sermil.core.exceptions.SermilException;
import br.mil.eb.sermil.modelo.DstbExclusao;

/** Processamento serviço.
 * @author Abreu Lopes
 * @since 5.1
 * @version $Id$
 */
@Named("infoServico")
public class InfoServico {

    protected static final Logger logger = LoggerFactory.getLogger(InfoServico.class);

    @Inject
    private DstbExclusaoDao dao;

    public InfoServico() {
        logger.debug("InfoServico iniciado");
    }

    public List<Object[]> listar() {
        final List<Object[]> lista = this.dao.findByNamedQueryArray("Info.listar");
        return lista;
    }

    @Transactional
    public void salvar(Long ra) throws SermilException {
        this.dao.save(new DstbExclusao(ra));
    }

}
