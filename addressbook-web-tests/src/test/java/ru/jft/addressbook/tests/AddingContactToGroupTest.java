package ru.jft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.jft.addressbook.model.ContactData;
import ru.jft.addressbook.model.Contacts;
import ru.jft.addressbook.model.GroupData;
import ru.jft.addressbook.model.Groups;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AddingContactToGroupTest extends TestBase {

  @BeforeMethod
  public void ensurePreconditionsContacts() {
    // проверка существования хотя бы одного контакта: если список пустой, то контакт нужно создать
    if (app.db().contacts().size() == 0) {
      app.contact().create(new ContactData()
              .withFirstname("Андрей")
              .withLastname("Сулимов")
              .withAddress("Злынка")
              .withHomePhone("89001234567")
              .withEmail("test@mail.ru"), true);
    }
  }

  @Test
  public void testAddingContactToGroup() {
    Groups allGroups = app.db().groups(); // получаем список всех групп (из БД)
    Contacts before = app.db().contacts(); // получаем список контактов (из БД)
    ContactData contact = before.iterator().next(); // выбираем контакт (рандомно)
    int id = contact.getId(); // сохраняем id выбранного контакта
    Groups groupsIncludeContactBefore = contact.getGroups(); // получаем список групп, в которые входит выбранный контакт
    System.out.println("Группы, в которые добавлен выбранный контакт до добавления" + groupsIncludeContactBefore);
    allGroups.removeAll(groupsIncludeContactBefore); // удаляем из списка всех групп те, в которые контакт уже добавлен
    System.out.println("Группы, доступные для добавления контакта" + allGroups);

    /* проверяем существование групп, в которые еще не добавлен выбранный контакт:
    если таких групп нет (контакт добавлен во все существующие группы), то создается новая группа */

    if (allGroups.size() == 0) {
      app.goTo().groupPage();
      app.group().create(new GroupData().withName("Test1"));
      app.goTo().homePage();
    }

    Groups actualAllGroups = app.db().groups(); // получаем актуальный список всех групп (из БД)
    actualAllGroups.removeAll(groupsIncludeContactBefore); // удаляем из актуального списка групп те, в которые контакт уже добавлен
    System.out.println("Группы, доступные для добавления контакта" + actualAllGroups);

    app.contact().selectContactById(contact.getId()); // устанавливаем ЧБ у выбранного контакта
    app.contact().addToGroup(actualAllGroups.iterator().next().getId()); // выбираем рандомную группу из списка доступных для добавления и добавляем в нее контакт
    Contacts after = app.db().contacts(); // получаем список контактов (из БД)
    ContactData actualContact = after.iterator().next().withId(id); // сохраняем информацию о "нашем" контакте (находим его по id)
    Groups groupsIncludeContactAfter = actualContact.withId(id).getGroups(); // получаем список групп, в которые входит контакт
    System.out.println("Группы, в которые добавлен выбранный контакт после добавления" + groupsIncludeContactAfter);

    /* сравниваем размер списка групп, в которых состоял контакт до добавления в новую группу,
    с размером списка групп, в которых контакт состоит после добавления в новую группу */

    assertThat(groupsIncludeContactAfter.size(), equalTo(groupsIncludeContactBefore.size() + 1));

    /* сравниваем содержимое списка групп, в которых состоит контакт после добавления в новую группу,
    с содержимым списка групп, в которых контакт состоял до добавления в новую группу,
    с добавлением в этот список новой группы */

    assertThat(groupsIncludeContactAfter, equalTo(groupsIncludeContactBefore.withAdded(actualAllGroups.iterator().next())));
  }
}