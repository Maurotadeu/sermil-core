package br.mil.eb.sermil.core.servicos;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import br.mil.eb.sermil.core.dao.InfoLocalDao;

/** Database Metadata.
 * @author Abreu Lopes
 * @since 5.1
 * @version $Id: AdminMetaDataServico.java 144 2015-01-29 15:04:25Z abreulopes $
 */
@Named("adminMetaDataServico")
public class AdminMetaDataServico {

  protected static final Logger logger = LoggerFactory.getLogger(AdminMetaDataServico.class);

  @Inject
  private InfoLocalDao dao;

  public AdminMetaDataServico() {
    logger.debug("AdminMetaDataServico iniciado");
  }

  @PreAuthorize("hasAnyRole('adm','dsm')")
  @Transactional
  public DatabaseMetaData getMetaData() throws SQLException {
    final DatabaseMetaData metadata = this.dao.getConnection().getMetaData();
    logger.debug("Database Product Name: {}", metadata.getDatabaseProductName());
    logger.debug("Database Product Version: {}", metadata.getDatabaseProductVersion());
    logger.debug("Logged User: {}", metadata.getUserName());
    logger.debug("JDBC Driver: {}", metadata.getDriverName());
    logger.debug("Driver Version: {}", metadata.getDriverVersion());
    return metadata;
  }
  
  @PreAuthorize("hasAnyRole('adm')")
  public ArrayList<String> getTablesMetadata() throws SQLException {
    final DatabaseMetaData metadata = this.dao.getConnection().getMetaData();
    String table[] = {"TABLE"};
    ResultSet rs = null;
    ArrayList<String> tables = null;
    rs = metadata.getTables(null, "SSM00", null, table);
    tables = new ArrayList<String>();
    while (rs.next()) {
      logger.debug(rs.getString("TABLE_NAME"));
      tables.add(rs.getString("TABLE_NAME"));
    }
    return tables;
  }

  @PreAuthorize("hasAnyRole('adm')")
  public void getColumnsMetadata(ArrayList<String> tables) throws SQLException {
    final DatabaseMetaData metadata = this.dao.getConnection().getMetaData();
    ResultSet rs = null;
    for (String actualTable : tables) {
      rs = metadata.getColumns(null, "SSM00", actualTable, null);
      logger.info(actualTable.toUpperCase());
      while (rs.next()) {
        logger.info("{} {} {}" ,rs.getString("COLUMN_NAME"), rs.getString("TYPE_NAME"), rs.getString("COLUMN_SIZE"));
      }
    }
  }
  
}
