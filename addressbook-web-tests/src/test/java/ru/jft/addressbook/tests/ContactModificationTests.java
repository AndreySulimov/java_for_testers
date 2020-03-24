package ru.jft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.jft.addressbook.model.ContactData;
import ru.jft.addressbook.model.Contacts;
import ru.jft.addressbook.model.GroupData;
import ru.jft.addressbook.model.Groups;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactModificationTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditionsContacts() {
    // проверка существования хотя бы одного контакта: если список пустой, то контакт нужно создать
    if (app.db().contacts().size() == 0) {
      Groups groups = app.db().groups();
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
  public void testContactModification() {
    Groups groups = app.db().groups(); // получаем список групп (из БД)
    Contacts before = app.db().contacts(); // сохраняем список контактов (из БД) до модификации
    ContactData modifiedContact = before.iterator().next(); // выбираем модифицируемый контакт (рандомно)
    // создаем объект типа contact и передаем в конструктор значения, которые будут использоваться при модификации
    ContactData contact = new ContactData()
            .withId(modifiedContact.getId()) /*задаем старый идентификатор*/
            .withFirstname("Андрей1")
            .withLastname("Сулимов1")
            .withAddress("Злынка1")
            .withHomePhone("89001234561")
            .withEmail("test1@mail.ru")
            .inGroup(groups.iterator().next()); // помещаем контакт в рандомную группу (из имеющихся)
    app.contact().modify(contact); // модифицируем контакт
    assertThat(app.contact().count(), equalTo(before.size())); // проверяем равенство размеров списков
    Contacts after = app.db().contacts(); // сохраняем список контактов (из БД) после модификации
    assertThat(after, equalTo(before.without(modifiedContact).withAdded(contact))); // проверяем, что контакт модифицировался
    verifyContactListInUI(); // сравниваем между собой списки контактов из БД и пользовательского интерфейса
  }
}