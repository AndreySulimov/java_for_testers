package ru.jft.mantis.tests;

import biz.futureware.mantis.rpc.soap.client.IssueData;
import biz.futureware.mantis.rpc.soap.client.MantisConnectPortType;
import org.openqa.selenium.remote.BrowserType;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import ru.jft.mantis.appmanager.ApplicationManager;

import javax.xml.rpc.ServiceException;
import java.io.File;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class TestBase {

  protected static final ApplicationManager app
          = new ApplicationManager(System.getProperty("browser", BrowserType.FIREFOX));

  @BeforeSuite(alwaysRun = true)
  public void setUp() throws Exception {
    app.init();
    app.ftp().upload(new File("src/test/resources/config_inc.php"), "config_inc.php", "config_inc.php.bak");
  }

  @AfterSuite(alwaysRun = true)
  public void tearDown() throws Exception {
    app.ftp().restore("config_inc.php.bak", "config_inc.php");
    app.stop();
  }

  // метод для проверки решения по дефекту: решен ("fixed") или нет
  public boolean isIssueOpen(int issueId) throws MalformedURLException, ServiceException, RemoteException {
    MantisConnectPortType mc = app.soap().getMantisConnect(); // открываем соединение с баг-трекером
    // запрашиваем информацию о дефекте с заданным id (issueId) и сохраняем в объект типа IssueData
    IssueData issue = mc.mc_issue_get("administrator", "root", BigInteger.valueOf(issueId));
    // если решение по дефекту равно "fixed", возвращаем true, а иначе - false
    // в UI нужное решение имеет имя "решена", но в терминологии используемого API - "fixed"
    if (issue.getResolution().getName().equals("fixed")) {
      return true;
    } else {
      return false;
    }
  }

  // метод для пропуска теста, если не исправлен баг, который блокирует выполнение этого теста
  public void skipIfNotFixed(int issueId) throws MalformedURLException, ServiceException, RemoteException {
    /* если метод isIssueOpen вернул false (решение по дефекту не равно "fixed"),
    то скипнуть тест и выбросить исключение с текстом */
    if (!isIssueOpen(issueId)) {
      throw new SkipException("Пропущено из-за бага " + issueId);
    }
  }
}