package ru.jft.addressbook;

import org.testng.annotations.*;

public class GroupCreationTests extends TestBase {

  @Test
  public void testGroupCreation() throws Exception {
    gotoGroupPage();
    initGroupCreation();
    fillGroupForm(new GroupData("Test1", "Test1", "Test1"));
    submitGroupCreation();
    returnToGroupPage();
  }
}
