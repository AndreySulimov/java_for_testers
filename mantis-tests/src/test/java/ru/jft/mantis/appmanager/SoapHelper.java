package ru.jft.mantis.appmanager;

import biz.futureware.mantis.rpc.soap.client.*;
import ru.jft.mantis.model.Issue;
import ru.jft.mantis.model.Project;

import javax.xml.rpc.ServiceException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class SoapHelper {

  private ApplicationManager app;

  public SoapHelper(ApplicationManager app) {
    this.app = app;
  }

  // метод для получения списка проектов
  public Set<Project> getProjects() throws RemoteException, MalformedURLException, ServiceException {
    MantisConnectPortType mc = getMantisConnect(); // открываем соединение с баг-трекером
    ProjectData[] projects = mc.mc_projects_get_user_accessible("administrator", "root");

    // преобразуем полученные данные в модельные объекты
    return Arrays.asList(projects).stream()
            .map((p) -> new Project().withId(p.getId().intValue()).withName(p.getName()))
            .collect(Collectors.toSet());
  }

  // метод для установки соединения с баг-трекером
  public MantisConnectPortType getMantisConnect() throws ServiceException, MalformedURLException {
    return new MantisConnectLocator()
            .getMantisConnectPort(new URL(app.getProperty("web.connectUrl")+ "/mantisconnect.php"));
  }

  // метод для добавления багрепорта
  public Issue addIssue(Issue issue) throws MalformedURLException, ServiceException, RemoteException {
    MantisConnectPortType mc = getMantisConnect(); // открываем соединение с баг-трекером
    // запрашиваем список категорий и сохраняем в переменную (массив строк)
    String[] categories = mc.mc_project_get_categories("administrator", "root", BigInteger.valueOf(issue.getProject().getId()));
    IssueData issueData = new IssueData(); // создаем объект типа IssueData (багрепорт)
    // и заполняем созданный объект
    issueData.setSummary(issue.getSummary());
    issueData.setDescription(issue.getDescription());
    issueData.setProject(new ObjectRef(BigInteger.valueOf(issue.getProject().getId()), issue.getProject().getName()));
    issueData.setCategory(categories[0]); // выбираем первую категорию из всех доступных
    // присваиваем идентификатор созданного багрепорта в переменную
    BigInteger issueId = mc.mc_issue_add("administrator", "root", issueData);
    IssueData createdIssueData = mc.mc_issue_get("administrator", "root", issueId);
    // строим и возвращаем модельный объект
    return new Issue().withId(createdIssueData.getId().intValue())
            .withSummary(createdIssueData.getSummary())
            .withDescription(createdIssueData.getDescription())
            .withProject(new Project().withId(createdIssueData.getProject().getId().intValue())
                    .withName(createdIssueData.getProject().getName()));
  }
}