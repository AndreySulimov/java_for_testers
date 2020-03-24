package ru.jft.addressbook.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.jft.addressbook.model.ContactData;
import ru.jft.addressbook.model.Contacts;
import ru.jft.addressbook.model.GroupData;
import ru.jft.addressbook.model.Groups;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactCreationTests extends TestBase {

  @DataProvider // провайдер тестовых данных
  public Iterator<Object[]> validContactsFromJson() throws IOException { // итератор массивов объектов
    // читаем данные из файла (построчно)
    try (BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/contacts.json")))) {
      String json = "";
      String line = reader.readLine();
      // читаем строки в цикле до тех пор, пока строки не кончатся
      while (line != null) {
        json += line;
        line = reader.readLine();
      }
      Gson gson = new Gson();
      List<ContactData> contacts = gson.fromJson(json, new TypeToken<List<ContactData>>() {
      }.getType()); // List<ContactData>.class
      return contacts.stream().map((c) -> new Object[]{c}).collect(Collectors.toList()).iterator();
    }
  }

  @BeforeMethod
  public void ensurePreconditions() {
    // проверка существования хотя бы одной группы: если список пустой, то группу нужно создать
    if (app.db().groups().size() == 0) {
      app.goTo().groupPage();
      app.group().create(new GroupData().withName("Test1"));
    }
  }

  @Test(dataProvider = "validContactsFromJson")
  public void testContactCreation(ContactData contact) throws Exception {
    Contacts before = app.db().contacts(); // сохраняем список контактов до создания нового (из БД)
    app.goTo().contactPage(); // переходим на страницу добавления контактов
    app.contact().create(contact, true); // создаем контакт с указанными параметрами
    app.goTo().homePage(); // возвращаемся на главную страницу
    assertThat(app.contact().count(), equalTo(before.size() + 1)); // сравниваем размеры списков
    Contacts after = app.db().contacts(); // сохраняем список контактов после создания нового (из БД)
    assertThat(after, equalTo(
            before.withAdded(contact.withId(after.stream().mapToInt((c) -> c.getId()).max().getAsInt())))); // проверяем, что контакт создался
    verifyContactListInUI(); // сравниваем между собой списки контактов из БД и пользовательского интерфейса
  }

  @Test
  public void testContactCreationWithPhoto() throws Exception {
    Groups groups = app.db().groups(); // получаем список групп (из БД)
    Contacts before = app.db().contacts(); // сохраняем список контактов до создания нового (из БД)
    app.goTo().contactPage(); // переходим на страницу добавления контактов
    File photo = new File("src/test/resources/logo.png"); // создаем локальную переменную photo и в качестве параметра передаем путь к файлу
    // создаем локальную переменную contact и передаем в конструктор значения
    ContactData contact = new ContactData()
            .withFirstname("Андрей")
            .withLastname("Сулимов")
            .withAddress("Злынка")
            .withHomePhone("89001234567")
            .withEmail("test@mail.ru")
            .withPhoto(photo)
            .inGroup(groups.iterator().next()); // помещаем контакт в рандомную группу (из имеющихся)
    app.contact().create(contact, true); // создаем контакт с указанными параметрами
    app.goTo().homePage(); // возвращаемся на главную страницу
    assertThat(app.contact().count(), equalTo(before.size() + 1)); // сравниваем размеры списков
    Contacts after = app.db().contacts(); // сохраняем список контактов после создания нового (из БД)
    assertThat(after, equalTo(
            before.withAdded(contact.withId(after.stream().mapToInt((c) -> c.getId()).max().getAsInt())))); // проверяем, что контакт создался
    verifyContactListInUI(); // сравниваем между собой списки контактов из БД и пользовательского интерфейса
  }

  @Test
  public void testBadContactCreation() throws Exception { // негативный тест - недопустимый сивол в firstname
    Contacts before = app.db().contacts();
    app.goTo().contactPage();
    ContactData contact = new ContactData()
            .withFirstname("Андрей'")
            .withLastname("Сулимов")
            .withAddress("Злынка")
            .withHomePhone("89001234567")
            .withEmail("test@mail.ru");
    app.contact().create(contact, true);
    app.goTo().homePage();
    assertThat(app.contact().count(), equalTo(before.size()));
    Contacts after = app.db().contacts();
    assertThat(after, equalTo(before)); // проверяем, что контакт не создался
    verifyContactListInUI(); // сравниваем между собой списки контактов из БД и пользовательского интерфейса
  }

  /*
  @Test
  public void testCurrentDir() {
    File currentDir = new File("."); // создаем объект типа File, точка обозначает текущую директорию
    System.out.println(currentDir.getAbsolutePath()); // выводим на экран абсолютный путь к файлу currentDir
    File photo = new File("src/test/resources/photo.png"); // создаем объект типа File, который соответствует уже существующему файлу
    System.out.println(photo.getAbsolutePath()); // выводим на экран абсолютный путь к файлу photo
    System.out.println(photo.exists()); // проверка существования файла
  }
  */

}