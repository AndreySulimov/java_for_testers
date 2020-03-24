package ru.jft.addressbook.tests;

import org.openqa.selenium.remote.BrowserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import ru.jft.addressbook.appmanager.ApplicationManager;
import ru.jft.addressbook.model.ContactData;
import ru.jft.addressbook.model.Contacts;
import ru.jft.addressbook.model.GroupData;
import ru.jft.addressbook.model.Groups;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestBase {

  Logger logger = LoggerFactory.getLogger(TestBase.class); // протоколирование (логгирование)

  protected static final ApplicationManager app
          = new ApplicationManager(System.getProperty("browser", BrowserType.FIREFOX));

  @BeforeSuite(alwaysRun = true)
  public void setUp() throws Exception {
    app.init();
  }

  @AfterSuite(alwaysRun = true)
  public void tearDown() throws Exception {
    app.logout();
    app.stop();
  }

  @BeforeMethod
  public void logTestStart(Method m, Object[] p) {
    logger.info("Start test " + m.getName() + " with parameters " + Arrays.asList(p));
  }

  @AfterMethod(alwaysRun = true)
  public void logTestStop(Method m) {
    logger.info("Stop test " + m.getName());
  }

  public void verifyGroupListInUI() {

    /* если в конфигурации запуска установлено специальное системное свойство (verifyUI),
    и оно имеет значение true (-DverifyUI=true), то тогда выполняется
    сравнение между собой списков групп и контактов из БД и пользовательского интерфейса
    (в тестах это методы verifyGroupListInUI() и verifyContactListInUI(),
    иначе сравнение не осуществляется. */

    if (Boolean.getBoolean("verifyUI")) {
      Groups dbGroups = app.db().groups(); // загружаем список групп из БД
      Groups uiGroups = app.group().all(); // загружаем список групп из пользовательского интерфейса

    /* т.к. из UI загружается информация только об именах групп,
    а из БД загружается полная информация, включая header и footer,
    перед сравнением необходимо упростить группы, загружаемые из БД.
    Для этого нужно убрать из этих групп всё кроме id и name следующим образом:
    преобразуем список групп в поток (stream()) и применяем ко всем элементам функцию map(),
    которая приниает на вход группу, а на выходе отдает новый объект типа GroupData,
    с такими же id и name, как у преобразуемой группы (которая была на входе), а header и footer - не указаны.
    После того, как ко всем элементам применена функция map(), нужно собрать всё при помощи коллектора.
    После всех этих преобразований можно сравнивать оба списка между собой */

      assertThat(uiGroups, equalTo(dbGroups.stream()
              .map((g) -> new GroupData().withId(g.getId()).withName(g.getName()))
              .collect(Collectors.toSet())));
    }
  }

  public void verifyContactListInUI() {

    /* если в конфигурации запуска установлено специальное системное свойство (verifyUI),
    и оно имеет значение true (-DverifyUI=true), то тогда выполняется
    сравнение между собой списков групп и контактов из БД и пользовательского интерфейса
    (в тестах это методы verifyGroupListInUI() и verifyContactListInUI(),
    иначе сравнение не осуществляется. */

    if (Boolean.getBoolean("verifyUI")) {
      Contacts dbContacts = app.db().contacts(); // загружаем список контактов из БД
      Contacts uiContacts = app.contact().all(); // загружаем список контактов из пользовательского интерфейса

    /* т.к. из UI загружается информация только о firstname и lastname контактов,
    а из БД загружается полная информация, включая address, email и homePhone,
    перед сравнением необходимо упростить контакты, загружаемые из БД.
    Для этого нужно убрать из этих контактов всё кроме id, firstname и lastname следующим образом:
    преобразуем список контактов в поток (stream()) и применяем ко всем элементам функцию map(),
    которая приниает на вход контакт, а на выходе отдает новый объект типа ContactData,
    с такими же id, firstname и lastname, как у преобразуемого контакта (который был на входе),
    а address, email и homePhone - не указаны.
    После того, как ко всем элементам применена функция map(), нужно собрать всё при помощи коллектора.
    После всех этих преобразований можно сравнивать оба списка между собой */

      assertThat(uiContacts.stream().map((uic) -> new ContactData()
                      .withId(uic.getId()).withFirstname(uic.getFirstname()).withLastname(uic.getLastname())
                      .withEmail(uic.getAllEmail()).withHomePhone(uic.getAllPhones()))
                      .collect(Collectors.toSet()),
              equalTo(dbContacts.stream().map((dbc) -> new ContactData()
                      .withId(dbc.getId()).withFirstname(dbc.getFirstname()).withLastname(dbc.getLastname()))
                      .collect(Collectors.toSet())));
    }
  }
}