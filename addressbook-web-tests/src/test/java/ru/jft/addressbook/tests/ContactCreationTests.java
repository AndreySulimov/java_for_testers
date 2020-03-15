package ru.jft.addressbook.tests;

import org.testng.annotations.Test;
import ru.jft.addressbook.model.ContactData;
import ru.jft.addressbook.model.Contacts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactCreationTests extends TestBase {

  @Test
  public void testContactCreation() throws Exception {
    Contacts before = app.contact().all(); // сохраняем список контактов до создания нового
    app.goTo().contactPage(); // переходим на страницу добавления контактов
    // создаем локальную переменную contact и передаем в конструктор значения
    ContactData contact = new ContactData()
            .withFirstname("Андрей3").withLastname("Сулимов3").withAddress("Злынка2").withTelephone("89001234567").withEmail("test2@mail.ru").withGroup("Test2");
    app.contact().create(contact, true); // создаем контакт с указанными параметрами
    app.goTo().returnToHomePage(); // возвращаемся на главную страницу
    Contacts after = app.contact().all(); // сохраняем список контактов после создания нового
    assertThat(after.size(), equalTo(before.size() + 1)); // сравниваем размеры списков
    assertThat(after, equalTo(
            before.withAdded(contact.withId(after.stream().mapToInt((c) -> c.getId()).max().getAsInt())))); // проверяем, что контакт создался
  }
}