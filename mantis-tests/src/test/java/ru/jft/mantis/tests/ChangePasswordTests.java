package ru.jft.mantis.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.jft.mantis.model.MailMessage;
import ru.jft.mantis.model.UserData;
import ru.lanwen.verbalregex.VerbalExpression;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class ChangePasswordTests extends TestBase {

  @BeforeMethod
  public void startMailServer() {
    app.mail().start(); // запускаем встроенный почтовый сервер
  }

  @Test
  public void testChangePassword() throws IOException, MessagingException {
    // переходим на страницу управления пользователями
    app.goTo().userManagePage();
    // получаем список всех пользователей, находим среди них заданного (по имени)
    UserData user = app.user().all().iterator().next().withName("user1585857495790");
    app.user().selectByName(user.getName()); // переходим в карточку заданного пользователя
    app.user().resetPassword(); // и сбрасываем его пароль

    // задаем значения имени, пароля и email пользователя
    String username = user.getName();
    String password = "password";
    String email = user.getEmail();

    // ждем 1 письмо пользователю в течение 10 секунд и сохраняем в список (mailMessages)
    List<MailMessage> mailMessages = app.mail().waitForMail(1, 10000);
    // среди всех писем находим то, которое отправлено на email пользователя, и извлекаем из него ссылку
    String confirmationLink = findConfirmationLink(mailMessages, email);
    app.user().changePassword(confirmationLink, password); // завершаем смену пароля

    // проверяем, что пользователь может войти в систему c новым паролем
    assertTrue(app.newSession().login(username, password));
  }

  private String findConfirmationLink(List<MailMessage> mailMessages, String email) {

    /* находим среди всех писем то, которое отправлено на нужный email,
    среди них берем первое и сохраняем в объект типа MailMessage */
    MailMessage mailMessage = mailMessages.stream().filter((m) -> m.to.equals(email)).findFirst().get();

    /* из текста полученного сообщения нужно извлечь ссылку - для этого используем регулярные выражения.
    Для упрощения работы с ними используем библиотеку verbalregex:
    строим регулярное выражение, ищем текст "http://", после которого должно идти
    один или больше непробельных символов, и собираем в кучу */
    VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();

    // применяем полученное регулярное выражение к тексту письма и возвращаем получившееся значение
    return regex.getText(mailMessage.text);
  }

  @AfterMethod(alwaysRun = true)
  public void stopMailServer() {
    app.mail().stop(); // останавливаем встроенный почтовый сервер
  }
}