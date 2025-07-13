package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.UserAuthEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserDao {
  UserAuthEntity create(UserAuthEntity user);
  Optional<UserAuthEntity> findCategoryById(UUID id);
  Optional<UserAuthEntity> findCategoryByUsername(String username);
  List<UserAuthEntity> findAllCategoryByUsername(String username);
  void deleteCategory(UserAuthEntity user);
}
