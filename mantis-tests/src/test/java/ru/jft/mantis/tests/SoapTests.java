package ru.jft.mantis.tests;

import org.testng.annotations.Test;
import ru.jft.mantis.model.Issue;
import ru.jft.mantis.model.Project;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Set;

import static org.testng.Assert.assertEquals;

public class SoapTests extends TestBase {

  @Test // тест для получения списка проектов
  public void testGetProjects() throws MalformedURLException, ServiceException, RemoteException {
    skipIfNotFixed(4);

    Set<Project> projects = app.soap().getProjects(); // получаем множество проектов
    System.out.println(projects.size());
    for (Project project : projects) {
      System.out.println(project.getName());
    }
  }

  @Test // тест для создания багрепорта
  public void testCreateIssue() throws MalformedURLException, ServiceException, RemoteException {
    // пропустить тест, если решение по багу с Id = 1 не равно "fixed"
    // в UI Id состоит из 7 цифр, например, 0000001, но в терминологии используемого API нулей нет
    skipIfNotFixed(2);

    Set<Project> projects = app.soap().getProjects();
    Issue issue = new Issue()
            .withSummary("Test issue")
            .withDescription("Test issue description")
            .withProject(projects.iterator().next()); // выбираем рандомный проект
    Issue created = app.soap().addIssue(issue);
    assertEquals(issue.getSummary(), created.getSummary());
  }
}