package ru.jft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.jft.addressbook.model.GroupData;
import ru.jft.addressbook.model.Groups;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class GroupDeletionTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    // проверка существования хотя бы одной группы: если список пустой, то группу нужно создать
    if (app.db().groups().size() == 0) {
      app.goTo().groupPage();
      app.group().create(new GroupData().withName("Test1"));
    }
  }

  @Test
  public void testGroupDeletion() throws Exception {
    Groups before = app.db().groups(); // получаем список групп до удаления (из БД)
    GroupData deletedGroup = before.iterator().next(); // выбираем группу из множества случайным образом
    app.goTo().groupPage();
    app.group().delete(deletedGroup);
    assertThat(app.group().count(), equalTo(before.size() - 1)); // хеширование (проверка того, что количество групп не изменилось)
    Groups after = app.db().groups(); // получаем список групп после удаления (из БД)
    assertThat(after, equalTo(before.without(deletedGroup)));
  }
}