package ru.jft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.jft.addressbook.model.ContactData;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactAddressTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    // проверка существования хотя бы одного контакта: если список пустой, то контакт нужно создать
    if (app.db().contacts().size() == 0) {
      app.contact().create(new ContactData()
              .withFirstname("Андрей")
              .withLastname("Сулимов")
              .withAddress("Злынка")
              .withHomePhone("89001234567")
              .withMobilePhone("+7(900)1234567")
              .withWorkPhone("8-900-123-45-67")
              .withEmail("test@mail.ru")
              .withEmail2("test2@mail.ru")
              .withEmail3("test3@mail.ru")
              .withGroup("test0"), true);
    }
  }

  @Test
  public void testContactAddress() {
    app.goTo().homePage();
    ContactData contact = app.db().contacts().iterator().next(); // загружаем множество контактов (из БД) и выбираем рандомный контакт
    ContactData contactInfoFromEditForm = app.contact().infoFromEditForm(contact); // загружаем информацию о контакте из формы редактирования

    // сравниваем адреса на главной странице и на странице редактирования контакта
    assertThat(contact.getAddress(), equalTo(contactInfoFromEditForm.getAddress()));
  }
}