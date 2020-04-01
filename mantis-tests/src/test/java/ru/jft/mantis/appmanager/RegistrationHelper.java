package ru.jft.mantis.appmanager;

import org.openqa.selenium.By;

public class RegistrationHelper extends HelperBase {

  public RegistrationHelper(ApplicationManager app) {
    super(app); // вызываем конструктор базового класса, в который передаем ссылку на ApplicationManager
  }

  // метод для начала регистрации
  public void start(String username, String email) {
    wd.get(app.getProperty("web.baseUrl") + "signup_page.php");
    type(By.name("username"), username);
    type(By.name("email"), email);
    click(By.cssSelector("input[value='Зарегистрироваться']"));

  }

  // метод для завершения регистрации
  public void finish(String confirmationLink, String username, String password) {
    wd.get(confirmationLink); // проходим по ссылке
    type(By.name("realname"), username);
    type(By.name("password"), password); // вводим пароль
    type(By.name("password_confirm"), password); // вводим пароль повторно (для подтвержения)
    click(By.cssSelector("span.bigger-110"));
  }
}