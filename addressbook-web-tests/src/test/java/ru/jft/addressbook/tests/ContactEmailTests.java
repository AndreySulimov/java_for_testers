package ru.jft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.jft.addressbook.model.ContactData;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactEmailTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    // проверка существования хотя бы одного контакта: если список пустой, то контакт нужно создать
    if (app.contact().all().size() == 0) {
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
              .withGroup("Test2"), true);
    }
  }

  @Test
  public void testContactEmail() {
    app.goTo().homePage();
    ContactData contact = app.contact().all().iterator().next(); // загружаем множество контактов и выбираем рандомный контакт
    ContactData contactInfoFromEditForm = app.contact().infoFromEditForm(contact); // загружаем информацию о контакте из формы редактирования

    // сравниваем номера телефонов на главной странице и на странице редактирования контакта
    assertThat(contact.getAllEmail(), equalTo(mergeEmail(contactInfoFromEditForm)));
  }

  private String mergeEmail(ContactData contact) {

    return Arrays.asList(contact.getEmail(), contact.getEmail2(), contact.getEmail3())
            .stream().filter((s) -> !s.equals(""))
            .collect(Collectors.joining("\n"));
  }
}
