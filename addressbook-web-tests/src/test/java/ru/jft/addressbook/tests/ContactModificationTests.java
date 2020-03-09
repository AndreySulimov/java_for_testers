package ru.jft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.jft.addressbook.model.ContactData;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class ContactModificationTests extends TestBase {

  @Test
  public void testContactModification() {
    if (!app.getContactHelper().isThereAContact()) {
      app.getContactHelper().createContact(new ContactData("Андрей", "Сулимов", "Злынка", "89001234567", "test@mail.ru", "Test1"), true);
    }
    List<ContactData> before = app.getContactHelper().getContactList();
    app.getContactHelper().initContactModification(before.size() - 1);
    // создаем локальную переменную contact
    ContactData contact = new ContactData(before.get(before.size() - 1).getId() /*задаем старый идентификатор*/, "Андрей2", "Сулимов2", "Злынка2", "89001234562", "test2@mail.ru", null);
    app.getContactHelper().fillContactForm(contact, false);
    app.getContactHelper().submitContactModification();
    app.getNavigationHelper().returnToHomePage();
    List<ContactData> after = app.getContactHelper().getContactList();
    Assert.assertEquals(after.size(), before.size());

    before.remove(before.size() - 1); // модификация списка: удаляем из списка элемент, который удаляется в тестируемом приложении
    before.add(contact); // добавляем вместо удаленного элемента тот, который появляется после модификации
    // сравниваем элементы по Id с помощью компаратора
    Comparator<? super ContactData> byId = (c1, c2) -> Integer.compare(c1.getId(), c2.getId());
    // сортируем оба списка по Id
    before.sort(byId);
    after.sort(byId);
    Assert.assertEquals(before, after); // сравниваем упорядоченные списки
    // Assert.assertEquals(new HashSet<>(before), new HashSet<>(after)); // преобразуем списки в множества и сравниваем два получившихся множества
  }
}