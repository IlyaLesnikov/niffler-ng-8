package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient {

  private static final Config CFG = Config.getInstance();

  private final OkHttpClient client = new OkHttpClient.Builder().build();
  private final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(CFG.spendUrl())
      .client(client)
      .addConverterFactory(JacksonConverterFactory.create())
      .build();

  private final SpendApi spendApi = retrofit.create(SpendApi.class);


  public SpendJson addSpend(SpendJson spend) {
    final Response<SpendJson> response;
    try {
      response = spendApi.addSpend(spend)
              .execute();
    } catch (IOException exception) {
      throw new AssertionError("Не удалось выполнить запрос на эндпоинт internal/spends/add");
    }
    assertEquals(response.code(), 201);
    return response.body();
  }

  public SpendJson getSpend(String id) {
    final Response<SpendJson> response;
    try {
      response = spendApi.getSpend(id)
              .execute();
    } catch (IOException exception) {
      throw new AssertionError("Не удалось выполнить запрос на эндпоинт internal/spends/add");
    }
    assertEquals(response.code(), 200);
    return response.body();
  }

  public SpendJson getSpends() {
    final Response<SpendJson> response;
    try {
      response = spendApi.getSpends()
              .execute();
    } catch (IOException exception) {
      throw new AssertionError("Не удалось выполнить запрос на эндпоинт internal/spends/add");
    }
    assertEquals(response.code(), 200);
    return response.body();
  }

  public SpendJson editSpend(SpendJson spend) {
    final Response<SpendJson> response;
    try {
      response = spendApi.editSpend(spend)
              .execute();
    } catch (IOException exception) {
      var message = String.format("Не удалось выполнить запрос на эндпоинт internal/spends/%s", spend.id().toString());
      throw new AssertionError(message);
    }
    assertEquals(response.code(), 200);
    return response.body();
  }

  public SpendJson deleteSpend(String ids) {
    final Response<SpendJson> response;
    try {
      response = spendApi.deleteSpend(ids)
              .execute();
    } catch (IOException exception) {
      throw new AssertionError("Не удалось выполнить запрос на эндпоинт internal/spends/remove");
    }
    assertEquals(response.code(), 200);
    return response.body();
  }

  public CategoryJson addCategory(CategoryJson category) {
    final Response<CategoryJson> response;
    try {
      response = spendApi.addCategory(category)
              .execute();
    } catch (IOException exception) {
      throw new AssertionError("Не удалось выполнить запрос на эндпоинт internal/categories/add");
    }
    assertEquals(response.code(), 201);
    return response.body();
  }

  public CategoryJson getCategories() {
    final Response<CategoryJson> response;
    try {
      response = spendApi.getCategories()
              .execute();
    } catch (IOException exception) {
      throw new AssertionError("Не удалось выполнить запрос на эндпоинт internal/categories/all");
    }
    assertEquals(response.code(), 200);
    return response.body();
  }

  public CategoryJson updateCategory(CategoryJson category) {
    final Response<CategoryJson> response;
    try {
      response = spendApi.updateCategory(category)
              .execute();
    } catch (IOException exception) {
      throw new AssertionError("Не удалось выполнить запрос на эндпоинт internal/categories/update");
    }
    assertEquals(response.code(), 200);
    return response.body();
  }
}
