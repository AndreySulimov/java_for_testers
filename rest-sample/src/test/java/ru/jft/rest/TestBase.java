package ru.jft.rest;

import com.google.gson.*;
import com.jayway.restassured.RestAssured;
import org.testng.SkipException;

public class TestBase {

  // метод для проверки состояния дефекта: закрыт ("Closed") или нет
  public boolean isIssueOpen(int issueId) {
    // получаем информацию о багрепорте с заданным id в формате json (отправляем GET-запрос)
    // ответ как раз и содержит нужную нам информацию в формате json
    String json = RestAssured.get(String.format("https://bugify.stqa.ru/api/issues/%s.json", issueId)).asString();

    /* парсим ответ, берем из него часть, соответствующую ключу "issues" (блок полей, содержащий нужное нам поле с состоянием),
       в этом блоке полей, представленном как массив, берем первый элемент (он там один единственный),
       после чего в этом элементе берем часть, соответствующую ключу "state_name", - искомое состояние бага
       и представляем его как строку */
    JsonElement parsed = new JsonParser().parse(json).getAsJsonObject().get("issues").getAsJsonArray().get(0);
    String state = parsed.getAsJsonObject().get("state_name").getAsString();

    System.out.println("Issue with id " + issueId + " is " + state);

    // если состояние дефекта равно "Closed", возвращаем true, а иначе - false
    if (state.equals("Closed")) {
      return true;
    } else {
      return false;
    }
  }

  // метод для пропуска теста, если не исправлен баг, который блокирует выполнение этого теста
  public void skipIfNotFixed(int issueId) {
    /* если метод isIssueOpen вернул false (состояние дефекта не равно "Closed"),
    то скипнуть тест и выбросить исключение с текстом */
    if (!isIssueOpen(issueId)) {
      throw new SkipException("Пропущено из-за бага " + issueId);
    }
  }
}