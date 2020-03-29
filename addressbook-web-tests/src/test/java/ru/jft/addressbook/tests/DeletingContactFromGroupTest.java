package ru.jft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.jft.addressbook.model.ContactData;
import ru.jft.addressbook.model.Contacts;
import ru.jft.addressbook.model.GroupData;
import ru.jft.addressbook.model.Groups;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class DeletingContactFromGroupTest extends TestBase {

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
    // проверка существования хотя бы одной группы: если список пустой, то группу нужно создать
    if (app.db().groups().size() == 0) {
      app.goTo().groupPage();
      app.group().create(new GroupData().withName("Test1"));
      app.goTo().homePage();
    }
  }

  @Test
  public void testDeletingContactFromGroup() {
    Contacts before = app.db().contacts(); // получаем список контактов (из БД)
    ContactData contact = before.iterator().next(); // выбираем контакт (рандомно)
    int id = contact.getId(); // сохраняем id выбранного контакта
    Groups groupsIncludeContactBefore = contact.getGroups(); // получаем список групп, в которые входит выбранный контакт
    System.out.println("Группы, в которые добавлен выбранный контакт до удаления" + groupsIncludeContactBefore);

    /* проверяем существование групп, в которые добавлен выбранный контакт:
    если таких групп нет (контакт не добавлен ни в одну из существующих групп), то контакт добавляется в группу */

    if (groupsIncludeContactBefore.size() == 0) {
      Groups allGroups = app.db().groups(); // получаем список всех групп (из БД)
      app.contact().selectContactById(contact.getId()); // устанавливаем ЧБ у выбранного контакта
      app.contact().addToGroup(allGroups.iterator().next().getId()); // выбираем рандомную группу из списка доступных для добавления и добавляем в нее контакт
      app.goTo().homePage();
    }

    Contacts actual = app.db().contacts(); // получаем список контактов (из БД) (нужно, если контакт был добавлен в новую группу)
    ContactData actualContact = actual.iterator().next().withId(id); // сохраняем информацию о "нашем" контакте (находим его по id)
    Groups actualGroupsIncludeContactBefore = actualContact.withId(id).getGroups(); // получаем актуальный список групп, в которые входит выбранный контакт
    System.out.println("Группы, в которые добавлен выбранный контакт до удаления" + actualGroupsIncludeContactBefore);

    app.contact().selectGroupToDelete(actualGroupsIncludeContactBefore.iterator().next().getId()); // выбираем рандомную группу из списка доступных для удаления
    app.contact().selectContactById(contact.getId()); // устанавливаем ЧБ у выбранного контакта
    app.contact().deleteFromGroup(); // и удаляем контакт из выбранной группы
    app.goTo().homePage();
    Contacts after = app.db().contacts(); // получаем список контактов (из БД)
    ContactData mostActualContact = after.iterator().next().withId(id); // сохраняем информацию о "нашем" контакте (находим его по id)
    Groups groupsIncludeContactAfter = mostActualContact.withId(id).getGroups(); // получаем список групп, в которые входит контакт
    System.out.println("Группы, в которые добавлен выбранный контакт после удаления" + groupsIncludeContactAfter);

    /* сравниваем размер списка групп, в которых состоял контакт до удаления из группы,
    с размером списка групп, в которых контакт состоит после удаления из группы */

    assertThat(groupsIncludeContactAfter.size(), equalTo(actualGroupsIncludeContactBefore.size() - 1));

    /* сравниваем содержимое списка групп, в которых состоит контакт после удаление из группы,
    с содержимым списка групп, в которых контакт состоял до удаления из группы,
    с исключением из этого списка той группы, из которой контакт был удален */

    Groups removedGroup = app.db().groups(); // получаем список всех групп (из БД)
    removedGroup.removeAll(groupsIncludeContactAfter); // удаляем из списка всех групп те, в которых контакт состоит после удаления из группы
    System.out.println("Группа, из которой удален контакт" + removedGroup);
    assertThat(groupsIncludeContactAfter, equalTo(actualGroupsIncludeContactBefore.without(removedGroup.iterator().next())));
  }
}