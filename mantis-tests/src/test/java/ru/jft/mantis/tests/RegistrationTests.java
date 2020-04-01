package ru.jft.mantis.tests;

import org.testng.annotations.Test;

// тестовый класс наследуется от TestBase, чтобы иеть возможность получить ссылку на ApplicationManager
public class RegistrationTests extends TestBase {

  @Test
  public void testRegistration() {
    app.registration().start("user1", "user1@localhost.localdomain");
  }
}