package ru.jft.addressbook.tests;

import org.testng.annotations.Test;
import ru.jft.addressbook.model.ContactData;

public class ContactModificationTests extends TestBase {

  @Test
  public void testContactModification() {
    if (! app.getContactHelper().isThereAContact()) {
      app.getContactHelper().createContact(new ContactData("Андрей", "Сулимов", "Злынка", "89001234567", "test@mail.ru", "Test1"), true);
    }
    app.getContactHelper().initContactModification();
    app.getContactHelper().fillContactForm(new ContactData("Андрей2", "Сулимов2", "Злынка2", "89001234562", "test2@mail.ru", null), false);
    app.getContactHelper().submitContactModification();
    app.getNavigationHelper().returnToHomePage();
  }
}