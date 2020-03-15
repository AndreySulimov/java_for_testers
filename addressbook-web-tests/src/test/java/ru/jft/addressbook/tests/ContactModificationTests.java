package ru.jft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.jft.addressbook.model.ContactData;
import ru.jft.addressbook.model.Contacts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;

public class ContactModificationTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    // проверка существования хотя бы одного контакта: если список пустой, то контакт нужно создать
    if (app.contact().all().size() == 0) {
      app.contact().create(new ContactData()
              .withFirstname("Андрей3").withLastname("Сулимов3").withAddress("Злынка2").withTelephone("89001234567").withEmail("test2@mail.ru").withGroup("Test2"), true);
    }
  }

  @Test
  public void testContactModification() {
    Contacts before = app.contact().all(); // сохраняем список контактов до модификации
    ContactData modifiedContact = before.iterator().next(); // выбираем модифицируемый контакт (рандомно)
    // создаем объект типа contact и передаем в конструктор значения, которые будут использоваться при модификации
    ContactData contact = new ContactData()
            .withId(modifiedContact.getId()) /*задаем старый идентификатор*/.withFirstname("Андрей2").withLastname("Сулимов2").withAddress("Злынка2").withTelephone("89001234562").withEmail("test2@mail.ru").withGroup(null);
    app.contact().modify(contact); // модифицируем контакт
    Contacts after = app.contact().all(); // сохраняем список контактов после модификации
    assertEquals(after.size(), before.size()); // проверяем равенство размеров списков
    assertThat(after, equalTo(before.without(modifiedContact).withAdded(contact))); // проверяем, что контакт модифицировался
  }
}