package ru.jft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.jft.addressbook.model.ContactData;
import ru.jft.addressbook.model.Contacts;

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
    type(By.name("home"), contactData.getHomePhone());
    type(By.name("mobile"), contactData.getMobilePhone());
    type(By.name("work"), contactData.getWorkPhone());
    type(By.name("email"), contactData.getEmail());
    type(By.name("email2"), contactData.getEmail2());
    type(By.name("email3"), contactData.getEmail3());

    if (creation) {
      new Select(wd.findElement(By.name("new_group"))).selectByVisibleText(contactData.getGroup());
    } else {
      Assert.assertFalse(isElementPresent(By.name("new_group")));
    }
  }

  public void selectContactById(int id) {
    wd.findElement(By.cssSelector("input[value='" + id + "']")).click();
  }

  public void deleteSelectedContact() {
    click(By.xpath("//input[@value='Delete']"));
    confirmDeletion();
  }

  public ContactData infoFromEditForm(ContactData contact) {
    initContactModificationById(contact.getId()); // выбираем контакт по идентификатору
    String firstname = wd.findElement(By.name("firstname")).getAttribute("value");
    String lastname = wd.findElement(By.name("lastname")).getAttribute("value");
    String address = wd.findElement(By.name("address")).getAttribute("value");
    String home = wd.findElement(By.name("home")).getAttribute("value");
    String mobile = wd.findElement(By.name("mobile")).getAttribute("value");
    String work = wd.findElement(By.name("work")).getAttribute("value");
    String email = wd.findElement(By.name("email")).getAttribute("value");
    String email2 = wd.findElement(By.name("email2")).getAttribute("value");
    String email3 = wd.findElement(By.name("email3")).getAttribute("value");

    wd.navigate().back();
    return new ContactData()
            .withId(contact.getId())
            .withFirstname(firstname)
            .withLastname(lastname)
            .withAddress(address)
            .withHomePhone(home)
            .withMobilePhone(mobile)
            .withWorkPhone(work)
            .withEmail(email)
            .withEmail2(email2)
            .withEmail3(email3);
  }

  private void initContactModificationById(int id) {
    wd.findElement(By.cssSelector("a[href='edit.php?id=" + id + "']")).click();

  }

  public void submitContactModification() {
    click(By.name("update"));
  }

  public void create(ContactData contact, boolean creation) {
    app.goTo().contactPage();
    fillContactForm(contact, creation);
    submitContactCreation();
    contactCache = null;
    app.goTo().homePage();
  }

  public void delete(ContactData сontact) {
    selectContactById(сontact.getId());
    deleteSelectedContact();
    contactCache = null;
    app.goTo().homePage();
  }

  public void modify(ContactData contact) {
    initContactModificationById(contact.getId());
    fillContactForm(contact, false);
    submitContactModification();
    contactCache = null;
    app.goTo().homePage();
  }

  public boolean isThereAContact() {
    return isElementPresent(By.name("selected[]"));
  }

  public int count() {
    return wd.findElements(By.name("selected[]")).size();
  }

  private Contacts contactCache = null; // поле для кэша

  public Contacts all() {
    /* если кэш не пустой (не равен null),
    то возвращается копия кэша */
    if (contactCache != null) {
      return new Contacts(contactCache);
    }
    contactCache = new Contacts();
    // находим элементы, соответствующие строкам таблицы
    List<WebElement> elements = wd.findElements(By.xpath("//tr[@name='entry']"));
    // проходимся циклом по строкам таблицы
    for (WebElement element : elements) {
      // внутри каждой строки находим ячейки
      List<WebElement> cells = element.findElements(By.tagName("td"));

      String firstname = cells.get(2).getText(); // извлекаем firstname из третьей ячейки
      String lastname = cells.get(1).getText(); // извлекаем lastname из второй ячейки
      String address = cells.get(3).getText(); // извлекаем address из четвертой ячейки
      String allPhones = cells.get(5).getText(); // извлекаем все телефоны (одновременно) из шестой ячейки
      String allEmail = cells.get(4).getText(); // извлекаем все email (одновременно) из пятой ячейки
      // извлекаем id из первой ячейки
      // находим один элемент (value) внутри другого (input)
      // преобразуем строку в число (Integer.parseInt)
      int id = Integer.parseInt(cells.get(0).findElement(By.tagName("input")).getAttribute("value"));

      // создаем объект и передаем в конструктор в качестве параметров извлеченные выше значения
      // добавляем созданный объект в список
      contactCache.add(new ContactData()
              .withId(id)
              .withFirstname(firstname)
              .withLastname(lastname)
              .withAddress(address)
              .withAllPhones(allPhones)
              .withAllEmail(allEmail));
    }
    return new Contacts(contactCache); // возвращаем копию кэша
  }
}