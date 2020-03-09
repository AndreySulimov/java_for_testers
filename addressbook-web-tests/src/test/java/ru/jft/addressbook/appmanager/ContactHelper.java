package ru.jft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.jft.addressbook.model.ContactData;

import java.util.ArrayList;
import java.util.List;

public class ContactHelper extends HelperBase {

  public ApplicationManager app;

  public ContactHelper(ApplicationManager applicationManager) {
    super(applicationManager.wd);
    this.app = applicationManager;
  }

  public void submitContactCreation() {
    wd.findElement(By.xpath("//div[@id='content']/form/input[21]")).click();
  }

  public void fillContactForm(ContactData contactData, boolean creation) {
    type(By.name("firstname"), contactData.getFirstname());
    type(By.name("lastname"), contactData.getLastname());
    type(By.name("address"), contactData.getAddress());
    type(By.name("mobile"), contactData.getTelephone());
    type(By.name("email"), contactData.getEmail());

    if (creation) {
      new Select(wd.findElement(By.name("new_group"))).selectByVisibleText(contactData.getGroup());
    } else {
      Assert.assertFalse(isElementPresent(By.name("new_group")));
    }
  }

  public void selectContact(int index) {
    wd.findElements(By.name("selected[]")).get(index).click();
  }

  public void deleteSelectedContact() {
    click(By.xpath("//input[@value='Delete']"));
    confirmDeletion();
  }

  public void initContactModification(int index) {
    wd.findElements(By.xpath("//img[@alt='Edit']")).get(index).click();
  }

  public void submitContactModification() {
    click(By.name("update"));
  }

  public void createContact(ContactData contact, boolean creation) {
    app.getNavigationHelper().goToContactPage();
    fillContactForm(contact, creation);
    submitContactCreation();
    app.getNavigationHelper().returnToHomePage();
  }

  public boolean isThereAContact() {
    return isElementPresent(By.name("selected[]"));
  }

  public int getContactCount() {
    return wd.findElements(By.name("selected[]")).size();
  }

  public List<ContactData> getContactList() {
    List<ContactData> contacts = new ArrayList<ContactData>();
    // находим элементы, соответствующие строкам таблицы
    List<WebElement> elements = wd.findElements(By.xpath("//tr[@name='entry']"));
    // проходимся циклом по строкам таблицы
    for (WebElement element : elements) {
      // внутри каждой строки находим ячейки
      List<WebElement> cells = element.findElements(By.tagName("td"));

      String firstname = cells.get(2).getText(); // извлекаем firstname из третьей ячейки
      String lastname = cells.get(1).getText(); // извлекаем lastname из второй ячейки
      // извлекаем id из первой ячейки
      // находим один элемент (value) внутри другого (input)
      // преобразуем строку в число (Integer.parseInt)
      int id = Integer.parseInt(cells.get(0).findElement(By.tagName("input")).getAttribute("value"));

      // создаем объект и передаем в конструктор в качестве параметров извлеченные выше значения
      ContactData contact = new ContactData(id, firstname, lastname, null, null, null, null);
      contacts.add(contact); // добавляем созданный объект в список
    }
    return contacts;
  }
}