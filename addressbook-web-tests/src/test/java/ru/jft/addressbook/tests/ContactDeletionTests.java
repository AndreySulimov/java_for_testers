package ru.jft.addressbook.tests;

import org.testng.annotations.Test;
import ru.jft.addressbook.model.ContactData;

public class ContactDeletionTests extends TestBase {

  @Test
  public void testContactDeletion() throws Exception {
    if (! app.getContactHelper().isThereAContact()) {
      app.getContactHelper().createContact(new ContactData("Андрей", "Сулимов", "Злынка", "89001234567", "test@mail.ru", "Test1"), true);
    }
    app.getContactHelper().selectContact();
    app.getContactHelper().deleteSelectedContact();
    app.logout();
  }
}