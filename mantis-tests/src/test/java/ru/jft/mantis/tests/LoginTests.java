package ru.jft.mantis.tests;

import org.testng.annotations.Test;
import ru.jft.mantis.appmanager.HttpSession;

import java.io.IOException;

import static org.testng.Assert.assertTrue;

public class LoginTests extends TestBase {

  @Test
  public void testLogin() throws IOException {
    HttpSession session = app.newSession(); // создаем новую сессию

    // выполняем вход в систему и проверяем, что авторизация успешна (на странице появился нужный текст)
    assertTrue(session.login("administrator", "root"));
    assertTrue(session.isLoggedInAs("administrator"));
  }
}