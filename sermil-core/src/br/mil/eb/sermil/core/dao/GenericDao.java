package br.mil.eb.sermil.core.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;

import br.mil.eb.sermil.core.exceptions.SermilException;

/** DAO gen�rico que define as opera��es CRUD b�sicas de uma entidade do modelo.
 * @author Abreu Lopes
 * @version $Id$
 * @since 4.0
 * @param <T> Tipo da entidade
 * @param <ID> Tipo da chave prim�ria
 */
public interface GenericDao<T, ID extends Serializable> {

    /** Retorna a conex�o JDBC.
     * @return java.sql.Connection
     */
    Connection getConnection();

    /** Retorna a conex�o JDBC.
     * @return java.sql.Connection
     */
    EntityManager getEntityManager();

    /** Obter classe da entidade.
     * @return java.lang.class
     */
    Class<T> getEntityClass();

    /** Conta todas as entidades.
     * @return n�mero de entidades
     */
    int countAll();

    /** Conta entidades baseado em um exemplo.
     * @param exemplo crit�rio de pesquisa
     * @return n�mero de entidades
     */
    int countByExample(final T exemplo);

    /** Remove a entidade.
     * @param entidade objeto a ser removido
     */
    void delete(final T entidade) throws SermilException;

    /** Executa um comando SQL (delete ou stored procedure).
     * @param namedQuery Comando SQL 
     * @param params par�metros
     * @return Total de registros afetados pelo comando
     */
    int execute(final String namedQuery, Object... params);

    /** Busca todas as entidades.
     * @return lista de entidades
     */
    List<T> findAll();

    /** Encontra uma entidade pela sua chave prim�ria.
     * @param id chave prim�ria
     * @return entidade
     */
    T findById(final ID id);

    /** Busca entidades atrav�s de um exemplo.
     * @param exemplo exemplo de inst�ncia
     * @return lista de entidades
     */
    List<T> findByExample(final T exemplo);

    /** Busca usando uma Named Query com par�metros passados por posi��o (1, 2, 3, ...)
     *  no Varargs (Object...) da chamada do m�todo.
     *  Use este m�todo somente quando a Query JPQL passar os par�metros por posi��o (?1).
     * @param namedQuery consulta (JPQL)
     * @param params par�metros
     * @return lista de entidades
     */
    List<T> findByNamedQuery(final String namedQuery, Object... params);

    /** Busca usando uma Named Query com par�metros passados por posi��o (1, 2, 3, ...)
     *  no Varargs (Object...) da chamada do m�todo.
     *  Use este m�todo somente quando a Query JPQL passar os par�metros por posi��o (?1).
     *  O retorno do m�todo � uma lista de Object[] devido ao fato da Named Query ser uma
     *  lista de atributos dos objetos que est�o sendo consultados. 
     * @param namedQuery consulta (JPQL)
     * @param params par�metros
     * @return lista de Object[]
     */
    List<Object[]> findByNamedQueryArray(final String namedQuery, Object... params);

    /** Busca usando uma Named Query e os par�metros passado por nome no Map da chamada do m�todo.
     *  Use este m�todo somente quando a Query JPQL passar os par�metros por nome (:nome).
     * @param queryName consulta (JPQL)
     * @param params par�metros
     * @return the lista de entidades
     */
    List<T> findByNamedQueryAndNamedParams(final String queryName, final Map<String, ? extends Object> params);

    /** Executa uma consulta nativa SQL.
     * @param sql Consulta SQL
     * @param params par�metros
     * @return lista de array de objetos
     */
    List<Object[]> findBySQL(final String sql, Object... params);

    /** Executa uma pesquisa por crit�rio.
     * @param criterio crit�rios de pesquisa
     * @return Lista de objetos
     */
    List<T> findByCriteria(final Predicate... criterio);

    /** Salva a entidade (update ou insert).
     * @param entidade objeto a ser salvo
     * @return entidade salva
     * @throws SermilException
     */
    T save(final T entidade) throws SermilException;

}
