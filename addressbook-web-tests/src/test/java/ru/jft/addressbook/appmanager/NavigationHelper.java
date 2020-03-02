package ru.jft.addressbook.appmanager;

import org.openqa.selenium.By;

public class NavigationHelper extends HelperBase {

  public ApplicationManager app;

  public NavigationHelper(ApplicationManager applicationManager) {
    super(applicationManager.wd);
    this.app = applicationManager;
  }

  public void gotoGroupPage() {
    if (isElementPresent(By.tagName("h1"))
            && wd.findElement(By.tagName("h1")).getText().equals("Groups")
            && isElementPresent(By.name("new"))) {
      return;
    }
    click(By.linkText("groups"));
  }

  public void goToContactPage() {
    click(By.linkText("add new"));
  }

  public void returnToHomePage() {
    if (isElementPresent(By.id("maintable"))) {
      return;
    }
    click(By.linkText("home page"));
  }
}