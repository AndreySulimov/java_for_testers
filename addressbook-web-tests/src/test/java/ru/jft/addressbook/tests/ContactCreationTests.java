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
            .withFirstname("Андрей").withLastname("Сулимов").withAddress("Злынка").withTelephone("89001234567").withEmail("test@mail.ru").withGroup("Test2");
    app.contact().create(contact, true); // создаем контакт с указанными параметрами
    app.goTo().returnToHomePage(); // возвращаемся на главную страницу
    assertThat(app.contact().count(), equalTo(before.size() + 1)); // сравниваем размеры списков
    Contacts after = app.contact().all(); // сохраняем список контактов после создания нового
    assertThat(after, equalTo(
            before.withAdded(contact.withId(after.stream().mapToInt((c) -> c.getId()).max().getAsInt())))); // проверяем, что контакт создался
  }

  @Test
  public void testBadContactCreation() throws Exception { // негативный тест - недопустимый сивол в firstname
    Contacts before = app.contact().all();
    app.goTo().contactPage();
    ContactData contact = new ContactData()
            .withFirstname("Андрей'").withLastname("Сулимов").withAddress("Злынка").withTelephone("89001234567").withEmail("test@mail.ru").withGroup("Test2");
    app.contact().create(contact, true);
    app.goTo().returnToHomePage();
    assertThat(app.contact().count(), equalTo(before.size()));
    Contacts after = app.contact().all();
    assertThat(after, equalTo(before)); // проверяем, что контакт не создался
  }
}