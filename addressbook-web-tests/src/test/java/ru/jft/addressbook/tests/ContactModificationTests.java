package ru.jft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.jft.addressbook.model.ContactData;
import ru.jft.addressbook.model.Contacts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactModificationTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    // проверка существования хотя бы одного контакта: если список пустой, то контакт нужно создать
    if (app.db().contacts().size() == 0) {
      app.contact().create(new ContactData()
              .withFirstname("Андрей")
              .withLastname("Сулимов")
              .withAddress("Злынка")
              .withHomePhone("89001234567")
              .withEmail("test@mail.ru")
              .withGroup("test0"), true);
    }
  }

  @Test
  public void testContactModification() {
    Contacts before = app.db().contacts(); // сохраняем список контактов (из БД) до модификации
    ContactData modifiedContact = before.iterator().next(); // выбираем модифицируемый контакт (рандомно)
    // создаем объект типа contact и передаем в конструктор значения, которые будут использоваться при модификации
    ContactData contact = new ContactData()
            .withId(modifiedContact.getId()) /*задаем старый идентификатор*/
            .withFirstname("Андрей2")
            .withLastname("Сулимов2")
            .withAddress("Злынка2")
            .withHomePhone("89001234562")
            .withEmail("test2@mail.ru")
            .withGroup(null);
    app.contact().modify(contact); // модифицируем контакт
    assertThat(app.contact().count(), equalTo(before.size())); // проверяем равенство размеров списков
    Contacts after = app.db().contacts(); // сохраняем список контактов (из БД) после модификации
    assertThat(after, equalTo(before.without(modifiedContact).withAdded(contact))); // проверяем, что контакт модифицировался
    verifyContactListInUI(); // сравниваем между собой списки контактов из БД и пользовательского интерфейса
  }
}