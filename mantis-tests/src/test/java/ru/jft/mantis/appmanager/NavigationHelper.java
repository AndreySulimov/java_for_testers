package ru.jft.mantis.appmanager;

import org.openqa.selenium.By;

public class NavigationHelper extends HelperBase {

  public NavigationHelper(ApplicationManager app) {
    super(app); // вызываем конструктор базового класса, в который передаем ссылку на ApplicationManager
  }

  // метод для перехода на страницу управления пользователями
  public void userManagePage() {
    wd.findElement(By.linkText("Управление")).click();
    wd.findElement(By.linkText("Управление пользователями")).click();
  }
}