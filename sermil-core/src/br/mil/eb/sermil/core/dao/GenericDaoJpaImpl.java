package br.mil.eb.sermil.core.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

import br.mil.eb.sermil.core.exceptions.SermilException;

/** Implementa��o JPA (EclipseLink) de GenericDao.
 * @author Abreu Lopes
 * @version $Id$
 * @since 4.0
 * @param <T> Tipo da entidade
 * @param <ID> Tipo da chave prim�ria
 */
public class GenericDaoJpaImpl<T, ID extends Serializable> implements GenericDao<T, ID> {

  /** Classe da entidade manipulada pelo DAO. */
  private final Class<T> classe;

  /** Entity Manager injetado por IoC. */
  private EntityManager entityManager;

  /** Define a classe da entidade com base no tipo gen�rico. */
  @SuppressWarnings("unchecked")
  public GenericDaoJpaImpl() {
    this.classe = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }

  /** Define a classe da entidade com base no par�metro informado.
   * @param classe Classe da entidade
   */
  public GenericDaoJpaImpl(final Class<T> classe) {
    this.classe = classe;
  }

  /** Configura o Entity Manager injetado por IoC.
   * @param entityManager Um Entity Manager apropriado
   */
  @PersistenceContext
  public void setEntityManager(final EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  /** Retorna o Entity Maager da classe.
   * @return Entity Manager
   */
  @Override
  public EntityManager getEntityManager() {
    return this.entityManager;
  }

  /** Retorna a classe de entidade do modelo que o DAO manipula.
   * @see br.mil.eb.sermil.core.dao.GenericDao#getEntityClass()
   */
  @Override
  public Class<T> getEntityClass() {
    return this.classe;
  }

  /** Retorna uma conex�o JDBC.
   * @see br.mil.eb.sermil.core.dao.GenericDao#getConnection()
   */
  @Override
  public Connection getConnection() {
    return this.getEntityManager().unwrap(Connection.class);
  }

  /** Conta todas as entidades dispon�veis.
   * @return Total de entidades
   * @see br.mil.eb.sermil.core.dao.GenericDao#countAll()
   */
  @Override
  public int countAll() {
    final List<T> lista = this.findAll();
    return lista != null ? lista.size() : 0;
  }

  /** Conta todas as entidades com base no exemplo informado.
   * @return Total de entidades
   * @see br.mil.eb.sermil.core.dao.GenericDao#countByExample(java.lang.Object)
   */
  @Override
  public int countByExample(final T exemplo) {
    final List<T> lista = this.findByExample(exemplo);
    return lista != null ? lista.size() : 0;
  }

  /** Remove a entidade.
   * @param entity Entidade
   * @see br.mil.eb.sermil.core.dao.GenericDao#delete(java.lang.Object)
   */
  @Override
  public void delete(T entity) throws SermilException {
      try {
          this.getEntityManager().remove(this.getEntityManager().contains(entity) ? entity : this.getEntityManager().merge(entity));
      } catch(PersistenceException e) {
          throw new SermilException(e);
      }
  }

  /** Executa um comando (UPDATE, DELETE ou Procedure).
   * @param namedQuery Comando
   * @param params Par�metros posicionais
   */
  @Override
  public int execute(final String namedQuery, Object... params) {
    final Query query = this.getEntityManager().createNamedQuery(namedQuery);
    for (int i = 0; i < params.length; i++) {
      query.setParameter(i + 1, params[i]);
    }
    return query.executeUpdate();
  }

  /** Busca todas as entidades.
   * @see br.mil.eb.sermil.core.dao.GenericDao#findAll()
   * @return Lista de entidades
   */
  @Override
  public List<T> findAll() {
    return findByCriteria();
  }

  /** Busca entidades com base no exemplo informado.
   * @return Lista de entidades
   * @see br.mil.eb.sermil.core.dao.GenericDao#findByExample(java.lang.Object)
   */
  @Override
  public List<T> findByExample(final T exemplo) {
    final CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
    final CriteriaQuery<T> query = cb.createQuery(this.classe);
    return this.getEntityManager().createQuery(query).getResultList();
  }

  /** Busca pelo identificador �nico da entidade (PK).
   * @return Entidade encontrada
   * @see br.mil.eb.sermil.core.dao.GenericDao#findById(java.io.Serializable)
   */
  @Override
  public T findById(final ID id) {
    return this.getEntityManager().find(this.classe, id);
  }

  /** Busca pela Named Query e pelos par�metros informados retornando uma lista de entidades.
   * @return Lista de entidades
   * @see br.mil.eb.sermil.core.dao.GenericDao#findByNamedQuery(java.lang.String, java.lang.Object[])
   */
  @Override
  public List<T> findByNamedQuery(final String namedQuery, Object... params) {
    final TypedQuery<T> query = this.getEntityManager().createNamedQuery(namedQuery, this.classe);
    for (int i = 0; i < params.length; i++) {
      query.setParameter(i + 1, params[i]);
    }
    final List<T> resultado = query.getResultList();
    return resultado;
  }

  /** Busca pela Named Query e par�metros informados retornando uma lista de array de objetos.
   * return Lista de array de objetos
   * @see br.mil.eb.sermil.core.dao.GenericDao#findByNamedQueryArray(java.lang.String, java.lang.Object[])
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> findByNamedQueryArray(final String namedQuery, Object... params) {
    final Query query = this.getEntityManager().createNamedQuery(namedQuery);
    for (int i = 0; i < params.length; i++) {
      query.setParameter(i + 1, params[i]);
    }
    final List<Object[]> resultado = query.getResultList();
    return resultado;
  }

  /** Busca pela Named Query e par�metros informados no Map retornando uma lista de entidades.
   * @param namedQuery Nome da consulta JPQL
   * @param params Par�metros nomeados
   * @return Lista de entidades
   * @see br.mil.eb.sermil.core.dao.GenericDao#findByNamedQueryAndNamedParams(java.lang.String, java.util.Map)
   */
  @Override
  public List<T> findByNamedQueryAndNamedParams(final String namedQuery, final Map<String, ? extends Object> params) {
    final TypedQuery<T> query = this.getEntityManager().createNamedQuery(namedQuery, this.classe);
    for (final Map.Entry<String, ? extends Object> param : params.entrySet()) {
      query.setParameter(param.getKey(), param.getValue());
    }
    final List<T> result = query.getResultList();
    return result;
  }

  /** Consulta SQL nativa, com par�metros passados por posi��o.
   * @param sql Consulta SQL nativa
   * @param params Par�metros posicionais (?1, ?2, ...)
   * @return Lista de objetos
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<Object[]> findBySQL(final String sql, Object... params) {
    final Query query = this.getEntityManager().createNativeQuery(sql);
    for (int i = 0; i < params.length; i++) {
      query.setParameter(i + 1, params[i]);
    }
    final List<Object[]> resultado = query.getResultList();
    return resultado;
  }

  /** Salva a entidade (update ou insert).
   * @return Entidade salva
   * @see br.mil.eb.sermil.core.dao.GenericDao#save(java.lang.Object)
   */
  @Override
  public T save(T entidade) throws SermilException {
      try {
          return this.getEntityManager().merge(entidade);
      } catch (PersistenceException e) {
          throw new SermilException(e);
      }
  }

  /** Busca pelos crit�rios informados. (m�todo auxiliar)
   * @param criterio Crit�rio(s) de busca
   * @return Lista de entidades
   */
  public List<T> findByCriteria(final Predicate... criterio) {
    final CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
    final CriteriaQuery<T> query = cb.createQuery(this.classe);
    query.where(criterio);
    return this.getEntityManager().createQuery(query).getResultList();
  }

}
