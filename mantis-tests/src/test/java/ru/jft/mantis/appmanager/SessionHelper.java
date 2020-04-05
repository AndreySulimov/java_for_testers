package ru.jft.mantis.appmanager;

import org.openqa.selenium.By;

public class SessionHelper extends HelperBase {

  public SessionHelper(ApplicationManager app) {
    super(app); // вызываем конструктор базового класса, в который передаем ссылку на ApplicationManager
  }

  // метод для авторизации
  public void login(String username, String password) {
    type(By.name("username"), username);
    click(By.xpath("//input[@value='Войти']"));
    type(By.name("password"), password);
    click(By.xpath("//input[@value='Войти']"));
  }
}