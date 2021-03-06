package ru.jft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.jft.addressbook.model.ContactData;
import ru.jft.addressbook.model.Contacts;
import ru.jft.addressbook.model.GroupData;
import ru.jft.addressbook.model.Groups;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactDeletionTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditionsContacts() {
    // проверка существования хотя бы одного контакта: если список пустой, то контакт нужно создать
    if (app.db().contacts().size() == 0) {
      Groups groups = app.db().groups(); // получаем список групп (из БД)
      app.contact().create(new ContactData()
              .withFirstname("Андрей")
              .withLastname("Сулимов")
              .withAddress("Злынка")
              .withHomePhone("89001234567")
              .withEmail("test@mail.ru")
              .inGroup(groups.iterator().next()), true);
    }
  }

  @BeforeMethod
  public void ensurePreconditionsGroups() {
    // проверка существования хотя бы одной группы: если список пустой, то группу нужно создать
    if (app.db().groups().size() == 0) {
      app.goTo().groupPage();
      app.group().create(new GroupData().withName("Test1"));
    }
  }

  @Test
  public void testContactDeletion() throws Exception {
    Contacts before = app.db().contacts(); // сохраняем список контактов (из БД) до удаления
    ContactData deletedContact = before.iterator().next(); // выбираем удаляемый контакт (рандомно)
    app.contact().delete(deletedContact); // удаляем выбранный контакт
    assertThat(app.contact().count(), equalTo(before.size() - 1)); // сравниваем размеры списков
    Contacts after = app.db().contacts(); // сохраняем список контактов (из БД) после удаления
    assertThat(after, equalTo(before.without(deletedContact))); // проверяем, что контакт удалился
    verifyContactListInUI(); // сравниваем между собой списки контактов из БД и пользовательского интерфейса
  }
}