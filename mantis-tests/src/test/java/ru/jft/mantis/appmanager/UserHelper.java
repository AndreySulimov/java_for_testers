package ru.jft.mantis.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.jft.mantis.model.UserData;
import ru.jft.mantis.model.Users;

import java.util.List;

public class UserHelper extends HelperBase {

  public UserHelper(ApplicationManager app) {
    super(app); // вызываем конструктор базового класса, в который передаем ссылку на ApplicationManager
  }

  // метод для выбора пользователя по имени
  public void selectByName(String name) {
    wd.findElement(By.xpath("//a[contains(text(),'" + name + "')]")).click();
  }

  // метод для сброса пароля пользователя
  public void resetPassword() {
    wd.findElement(By.xpath("//input[@value='Сбросить пароль']")).click();
  }

  // метод для завершения смены пароля
  public void changePassword(String confirmationLink, String password) {
    wd.get(confirmationLink); // проходим по ссылке
    type(By.name("password"), password); // вводим пароль
    type(By.name("password_confirm"), password); // вводим пароль повторно (для подтвержения)
    click(By.cssSelector("span.bigger-110"));
  }

  private Users userCache = null; // поле для кэша

  // метод для получения списка всех пользователей
  public Users all() {

    /* если кэш не пустой (не равен null),
    то возвращается копия кэша */
    if (userCache != null) {
      return new Users(userCache);
    }

    userCache = new Users();
    // находим элементы, соответствующие строкам таблицы
    List<WebElement> elements = wd.findElements(By.xpath("//table[@class=\"table table-striped table-bordered table-condensed table-hover\"]/tbody/tr"));
    // проходимся циклом по строкам таблицы
    for (WebElement element : elements) {
      // внутри каждой строки находим ячейки
      List<WebElement> cells = element.findElements(By.tagName("td"));

      String name = cells.get(1).getText(); // извлекаем name из второй ячейки
      String email = cells.get(2).getText(); // извлекаем email из третьей ячейки

      // создаем объект и передаем в конструктор в качестве параметров извлеченные выше значения
      // добавляем созданный объект в список
      userCache.add(new UserData().withName(name).withEmail(email));
    }
    return new Users(userCache); // возвращаем копию кэша
  }
}