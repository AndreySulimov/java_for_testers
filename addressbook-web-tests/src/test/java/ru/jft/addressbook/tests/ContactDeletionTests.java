package ru.jft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.jft.addressbook.model.ContactData;
import ru.jft.addressbook.model.Contacts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;

public class ContactDeletionTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    // проверка существования хотя бы одного контакта: если список пустой, то контакт нужно создать
    if (app.contact().all().size() == 0) {
      app.contact().create(new ContactData()
              .withFirstname("Андрей").withLastname("Сулимов").withAddress("Злынка").withTelephone("89001234567").withEmail("test@mail.ru").withGroup("Test2"), true);
    }
  }

  @Test
  public void testContactDeletion() throws Exception {
    Contacts before = app.contact().all(); // сохраняем список контактов до удаления
    ContactData deletedContact = before.iterator().next(); // выбираем удаляемый контакт (рандомно)
    app.contact().delete(deletedContact); // удаляем выбранный контакт
    assertThat(app.contact().count(), equalTo(before.size() - 1)); // сравниваем размеры списков
    Contacts after = app.contact().all(); // сохраняем список контактов после удаления
    assertThat(after, equalTo(before.without(deletedContact))); // проверяем, что контакт удалился
  }
}