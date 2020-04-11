package ru.jft.rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jayway.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Set;

import static org.testng.Assert.assertEquals;

public class RestAssuredTests extends TestBase {

  @BeforeClass
  public void init() {
    RestAssured.authentication = RestAssured.basic("288f44776e7bec4bf44fdfeb1e646490", "");
  }

  @Test
  public void testCreateIssue() throws IOException {
    skipIfNotFixed(2806);
    Set<Issue> oldIssues = getIssues(); // получаем множество объектов типа Issue (список багов до добавления нового)
    // формируем новый объект типа Issue (баг)
    Issue newIssue = new Issue().withSubject("Test issue").withDescription("New test issue");
    int issueId = createIssue(newIssue); // выполняем на основе сформированного объекта метод createIssue
    Set<Issue> newIssues = getIssues(); // получаем множество объектов типа Issue (список багов после добавления нового)
    oldIssues.add(newIssue.withId(issueId)); // добавляем новый баг в первоначальный список
    // выводим на экран информацию о созданном баге
    System.out.println("Created issue with Subject: "
            + "'" + newIssue.getSubject() + "'"
            + " and with Description: " + "'" + newIssue.getDescription() + "'"
            + " and with Id: " + "'" + newIssue.getId() + "'");
    assertEquals(newIssues, oldIssues); // сравниваем списки
  }

  // метод для получения списка багов посредством отправки GET-запроса
  private Set<Issue> getIssues() throws IOException {
    // получаем список всех багрепортов в формате json (отправляем GET-запрос)
    // ответ как раз и содержит нужный нам список в формате json
    String json = RestAssured.get("https://bugify.stqa.ru/api/issues.json?limit=500").asString();
    // анализируем (парсим) ответ и берем из него часть, соответствующую ключу "issues"
    JsonElement parsed = new JsonParser().parse(json);
    JsonElement issues = parsed.getAsJsonObject().get("issues");
    // преобразуем полученный список в множество объектов типа Issue
    return new Gson().fromJson(issues, new TypeToken<Set<Issue>>() {}.getType()); // Set<Issue>.class);
  }

  // метод для создания багрепорта
  private int createIssue(Issue newIssue) throws IOException {
    // отправляем POST-запрос на создание багрепорта, в котором передаем subject и description.
    // в ответе получаем, кроме прочего, идентификатор созданного багрепорта (issue_id)
    String json = RestAssured.given()
            .parameter("subject", newIssue.getSubject())
            .parameter("description", newIssue.getDescription())
            .post("https://bugify.stqa.ru/api/issues.json").asString();
    // анализируем (парсим) ответ, берем из него часть, соответствующую ключу "issue_id",
    // и представляем его как целое число
    JsonElement parsed = new JsonParser().parse(json);
    return parsed.getAsJsonObject().get("issue_id").getAsInt();
  }
}