package guru.qa.niffler.data;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class Databases {
    private Databases() {}

    private static final ConcurrentHashMap<String, DataSource> datasources = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, Map<String, Connection>> threadConnections = new ConcurrentHashMap<>();

    public static <T> T transaction(Function<Connection, T> function, String jdbcUrl) {
      Connection connection = null;
      try {
        connection = connection(jdbcUrl);
        connection.setAutoCommit(false);
        T result = function.apply(connection);
        connection.commit();
        connection.setAutoCommit(true);
        return result;
      } catch (Exception e) {
          try {
            if (connection != null) {
              connection.rollback();
              connection.setAutoCommit(true);
            }
            throw e;
          } catch (SQLException ex) {
            throw new RuntimeException(e);
          }
      }
    }

  public static void transaction(Consumer<Connection> function, String jdbcUrl) {
    Connection connection = null;
    try {
      connection = connection(jdbcUrl);
      connection.setAutoCommit(false);
      function.accept(connection);
      connection.commit();
      connection.setAutoCommit(true);
    } catch (Exception e) {
      try {
        if (connection != null) {
          connection.rollback();
          connection.setAutoCommit(true);
        }
        throw e;
      } catch (SQLException ex) {
        throw new RuntimeException(e);
      }
    }
  }

    public static Connection connection(String jdbcUrl) {
        return threadConnections.computeIfAbsent(
            Thread.currentThread().threadId(),
            key -> {
              try {
                return new HashMap<>(
                    Map.of(jdbcUrl, datasource(jdbcUrl).getConnection())
                );
              } catch (SQLException e) {
                throw new RuntimeException(e);
              }
            }
        ).computeIfAbsent(
            jdbcUrl,
            key -> {
              try {
                return datasource(jdbcUrl).getConnection();
              } catch (SQLException e) {
                throw new RuntimeException(e);
              }
            }
        );
    }

    private static DataSource datasource(String jdbcUrl) {
        return datasources.computeIfAbsent(
            jdbcUrl,
            key -> {
              PGSimpleDataSource pgDataSource = new PGSimpleDataSource();
              pgDataSource.setUser("postgres");
              pgDataSource.setPassword("secret");
              pgDataSource.setURL(key);
              return pgDataSource;
            }
        );
    }

    public static void closeConnections() {
      for (Map<String, Connection> connections: threadConnections.values()) {
        for (Connection connection: connections.values()) {
          try {
            if (connection != null && !connection.isClosed()) {
              connection.close();
            }
          } catch (SQLException e) {
            //NOP
          }
        }
      }
    }
}
