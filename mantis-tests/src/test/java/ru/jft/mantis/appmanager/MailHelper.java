package ru.jft.mantis.appmanager;

import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;
import ru.jft.mantis.model.MailMessage;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MailHelper {
  private ApplicationManager app;
  private final Wiser wiser;

  public MailHelper(ApplicationManager app) {
    this.app = app;
    wiser = new Wiser(); // создаем почтовый сервер (объект типа Wiser)
  }

  // метод для ожидания письма (нужен, т.к. письма приходят не мгновенно)
  public List<MailMessage> waitForMail(
          int count /*количество писем, которые должны прийти*/,
          long timeout /*время ожидания*/)
          throws MessagingException, IOException {
    long start = System.currentTimeMillis(); // запоминаем текущее время

    /* В цикле while проверяем, что новое текущее время не превышает момент старта + таймаут.
    Внутри (в цикле if) проверяем: если почты пришло достаточно много, то ожидание можно прекращать и
    переходим к преобразованию писем (это нужно т.к. разные почтовые сервера могут использовать разный формат
    представления писем):
    берем список писем, превращаем в поток, применяем ко всем письмам функцию toModelMail,
    чтобы привести их к нужному нам модельному виду (описанному в MailMessage), и
    собираем получившиеся объекты в новый список;
    если же почты слишком мало, то ждем в течение времени, указанного в Thread.sleep(),
    и снова заходим в цикл while.
    И так до тех пор пока либо не придет нужное количество писем, либо не окончится время ожидания */

    while (System.currentTimeMillis() < start + timeout) {
      if (wiser.getMessages().size() >= count) {
        return wiser.getMessages().stream().map((m) -> toModelMail(m)).collect(Collectors.toList());
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    throw new Error("No mail :("); // если в течение указанного времени ожидаемое количество писем не пришли, то выбрасывается исключение
  }

  // метод для преобразования реальных писем в модельные (соответствующие MailMessage)
  public static MailMessage toModelMail(WiserMessage m) {
    try {
      MimeMessage mm = m.getMimeMessage(); // берем реальный объект (письмо в первоначальном формате)
      // 1. берем список адресов получателей и оставляем первого из них (т.к. известно, что получатель всего один)
      // 2. преобразуем письмо (т.к. оно содержит только текст) в строку и отправляем в объект MailMessage
      return new MailMessage(mm.getAllRecipients()[0].toString(), (String) mm.getContent());
    } catch (MessagingException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  // метод для запуска почтового сервера
  public void start() {
    wiser.start();
  }

  // метод для остановки почтового сервера
  public void stop() {
    wiser.stop();
  }
}