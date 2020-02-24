package ru.jft.addressbook.tests;

import org.testng.annotations.Test;
import ru.jft.addressbook.model.ContactData;

public class ContactCreationTests extends TestBase {

  @Test
  public void testContactCreation() throws Exception {
    app.getNavigationHelper().goToContactPage();
    app.getContactHelper().createContact(new ContactData("Андрей", "Сулимов", "Злынка", "89001234567", "test@mail.ru", "Test1"), true);
    app.getNavigationHelper().returnToHomePage();
  }
}