package br.mil.eb.sermil.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {

   private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
   private static final String DB_URL = "jdbc:oracle:thin:@//exa01-scan.eb.mil.br:1521/sermil";
   private static final String USER = "sermilweb";
   private static final String PASS = "krupp210mk11";

   public static Connection getConnection() throws ClassNotFoundException, SQLException {
      Class.forName(JDBC_DRIVER);
      final Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
      return conn;
   }

}
