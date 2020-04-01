package ru.jft.mantis.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.jft.mantis.model.MailMessage;
import ru.lanwen.verbalregex.VerbalExpression;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertTrue;

// тестовый класс наследуется от TestBase, чтобы иеть возможность получить ссылку на ApplicationManager
public class RegistrationTests extends TestBase {

  @BeforeMethod
  public void startMailServer() {
    app.mail().start();
  }

  @Test
  public void testRegistration() throws IOException, MessagingException {
    long now = System.currentTimeMillis(); // запоминаем текущее время (в миллисекундах) в качестве уникального идентификатора
    String username = String.format("user%s", now);
    String password = "password";
    String email = String.format("user%s@localhost.localdomain", now);
    app.registration().start(username, email);
    // ждем 2 письма (1 админу, 1 пользователю) в течение 10 секунд и сохраняе их в список (mailMessages)
    List<MailMessage> mailMessages = app.mail().waitForMail(2, 10000);
    // среди всех писем находим то, которое отправлено на email пользователя, и извлекаем из него ссылку
    String confirmationLink = findConfirmationLink(mailMessages, email);
    app.registration().finish(confirmationLink, username, password); // завершаем регистрацию

    // проверяем, что зарегистрированный пользователь может войти в систему
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
    app.mail().stop();
  }
}