package guru.qa.niffler.data.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.UserAuthEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoJdbc implements AuthUserDao {

  private final static Config CFG = Config.getInstance();
  private final Connection connection;

  public AuthUserDaoJdbc() {
    this.connection = Databases.connection(CFG.authJdbcUrl());
  }

  @Override
  public UserAuthEntity create(UserAuthEntity user) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO authority (username, password, enabled, accountNonExpired, accountNonLocked, credentialsNonExpired, authorities) VALUES (?, ?, ?, ?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getPassword());
      ps.setBoolean(3, user.getEnabled());
      ps.setBoolean(4, user.getAccountNonExpired());
      ps.setBoolean(5, user.getAccountNonLocked());
      ps.setBoolean(6, user.getCredentialsNonExpired());
      ps.executeUpdate();
      try (ResultSet rs = ps.getGeneratedKeys()) {
        final UUID generatedKey;
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
          user.setId(generatedKey);
        }
        return user;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UserAuthEntity> findCategoryById(UUID id) {
    try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM authority WHERE id = ?")) {
      ps.setObject(1, id);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          UserAuthEntity user = new UserAuthEntity();
          user.setId(rs.getObject("id", UUID.class));
          user.setUsername(rs.getString("username"));
          user.setPassword(rs.getString("password"));
          user.setEnabled(rs.getBoolean("enabled"));
          user.setAccountNonExpired(rs.getBoolean("account_non_expired"));
          user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
          user.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
          return Optional.of(user);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UserAuthEntity> findCategoryByUsername(String username) {
    try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM authority WHERE username = ?")) {
      ps.setObject(1, username);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          UserAuthEntity user = new UserAuthEntity();
          user.setId(rs.getObject("id", UUID.class));
          user.setUsername(rs.getString("username"));
          user.setPassword(rs.getString("password"));
          user.setEnabled(rs.getBoolean("enabled"));
          user.setAccountNonExpired(rs.getBoolean("account_non_expired"));
          user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
          user.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
          return Optional.of(user);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<UserAuthEntity> findAllCategoryByUsername(String username) {
    try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM authority WHERE username = ?")) {
      ps.setObject(1, username);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        List<UserAuthEntity> users = new ArrayList<>();
        while (rs.next()) {
          UserAuthEntity user = new UserAuthEntity();
          user.setId(rs.getObject("id", UUID.class));
          user.setUsername(rs.getString("username"));
          user.setPassword(rs.getString("password"));
          user.setEnabled(rs.getBoolean("enabled"));
          user.setAccountNonExpired(rs.getBoolean("account_non_expired"));
          user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
          user.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
          users.add(user);
        }
        return users;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteCategory(UserAuthEntity user) {
    try (PreparedStatement ps = connection.prepareStatement("DELETE FROM authority WHERE id = ?")) {
      ps.setObject(1, user.getId());
      int count = ps.executeUpdate();
      if (count != 1) {
        throw new RuntimeException("Не удалось удалить сущность с id = %s".formatted(user.getId()));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
