package ru.jft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.jft.addressbook.model.GroupData;
import ru.jft.addressbook.model.Groups;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class GroupModificationTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    // проверка существования хотя бы одной группы: если список пустой, то группу нужно создать
    if (app.db().groups().size() == 0) {
      app.goTo().groupPage();
      app.group().create(new GroupData().withName("Test1"));
    }
  }

  @Test
  public void testGroupModification() {
    Groups before = app.db().groups(); // получаем список групп до модификации (из БД)
    GroupData modifiedGroup = before.iterator().next(); // выбираем группу из множества случайным образом
    GroupData group = new GroupData()
            .withId(modifiedGroup.getId()).withName("Test1").withHeader("Test2").withFooter("Test3");
    app.goTo().groupPage();
    app.group().modify(group);
    assertThat(app.group().count(), equalTo(before.size())); // хеширование (проверка того, что количество групп не изменилось)
    Groups after = app.db().groups(); // получаем список групп после модификации (из БД)
    assertThat(after, equalTo(before.without(modifiedGroup).withAdded(group)));
    verifyGroupListInUI(); // сравниваем между собой списки групп из БД и пользовательского интерфейса
  }
}