package ru.jft.addressbook.tests;

import org.testng.annotations.*;
import ru.jft.addressbook.model.GroupData;

public class GroupCreationTests extends TestBase {

  @Test
  public void testGroupCreation() throws Exception {
    app.getNavigationHelper().gotoGroupPage();
    app.getGroupHelper().createGroup(new GroupData("Test1", "Test1", "Test1"));
  }
}