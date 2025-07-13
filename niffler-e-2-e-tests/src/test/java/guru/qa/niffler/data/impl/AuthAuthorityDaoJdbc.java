package guru.qa.niffler.data.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.data.entity.AuthorityEntity;
import guru.qa.niffler.data.entity.UserEntity;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

  private final static Config CFG = Config.getInstance();
  private final Connection connection;

  public AuthAuthorityDaoJdbc() {
    this.connection = Databases.connection(CFG.authJdbcUrl());
  }

  @Override
  public AuthorityEntity create(AuthorityEntity authority) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO authority (user_id, authority) VALUES (?, ?)",
        Statement.RETURN_GENERATED_KEYS)) {
      ps.setObject(1, authority.getUser());
      ps.setString(2, authority.getAuthority().name());
      ps.executeUpdate();
      try (ResultSet rs = ps.getGeneratedKeys()) {
        final UUID generatedKey;
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
          authority.setId(generatedKey);
        }
        return authority;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthorityEntity> findCategoryById(UUID id) {
    try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM authority WHERE id = ?")) {
      ps.setObject(1, id);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          AuthorityEntity authority = new AuthorityEntity();
          authority.setId(rs.getObject("id", UUID.class));
          authority.setUser(rs.getObject("user_id", UserEntity.class));
          authority.setAuthority(rs.getObject("authority", Authority.class));
          return Optional.of(authority);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteCategory(AuthorityEntity authority) {
    try (PreparedStatement ps = connection.prepareStatement("DELETE FROM authority WHERE id = ?")) {
      ps.setObject(1, "id");
      int count = ps.executeUpdate();
      if (count != 1) {
        throw new SQLException("Не удалось удалить сущность с id = %s".formatted(authority.getId()));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
