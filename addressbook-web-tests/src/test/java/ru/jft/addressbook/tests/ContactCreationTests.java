package ru.jft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.jft.addressbook.model.ContactData;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class ContactCreationTests extends TestBase {

  @Test
  public void testContactCreation() throws Exception {
    List<ContactData> before = app.getContactHelper().getContactList();
    app.getNavigationHelper().goToContactPage();
    // создаем переменную и передаем в конструктор значения
    ContactData contact = new ContactData("Андрей3", "Сулимов3", "Злынка2", "89001234567", "test2@mail.ru", "Test1");
    app.getContactHelper().createContact(contact, true);
    app.getNavigationHelper().returnToHomePage();
    List<ContactData> after = app.getContactHelper().getContactList();
    Assert.assertEquals(after.size(), before.size() + 1);

    // находим максимальный элемент с помощью цикла
    /*
    int max = 0; // устанавливаем максиальное значение
    // сравниваем найденный максимум с индикатором каждого элемента
    for (ContactData c : after) {
      if (c.getId() > max) { // если идентификатор оказался больше, то меняем значение max
        max = c.getId();
      }
    }
    */

    /* сравниваем объекты с помощью анонимной (лямбда) функции
    - превращаем список в поток (after.stream())
    - с помощью лямбда-функции находим максимальный элемент
    - устанавливаем в качестве идентификатора нового контакта найденный максимум */
    // contact.setId(after.stream().max((o1, o2) -> Integer.compare(o1.getId(), o2.getId())).get().getId());

    before.add(contact); // добавляем в список контакт, созданный в тестируемом приложении
    // сравниваем элементы по Id с помощью компаратора
    Comparator<? super ContactData> byId = (c1, c2) -> Integer.compare(c1.getId(), c2.getId());
    // сортируем оба списка по Id
    before.sort(byId);
    after.sort(byId);
    Assert.assertEquals(before, after); // сравниваем упорядоченные списки
    // Assert.assertEquals(new HashSet<>(before), new HashSet<>(after)); // преобразуем списки в множества и сравниваем два получившихся множества
  }
}