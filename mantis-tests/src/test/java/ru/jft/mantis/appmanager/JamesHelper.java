package ru.jft.mantis.appmanager;

import org.apache.commons.net.telnet.TelnetClient;
import ru.jft.mantis.model.MailMessage;

import javax.mail.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JamesHelper {

  private ApplicationManager app;

  private TelnetClient telnet;
  private InputStream in;
  private PrintStream out;

  private Session mailSession;
  private Store store;
  private String mailserver;

  public JamesHelper(ApplicationManager app) {
    this.app = app;
    telnet = new TelnetClient(); // создаем TelnetClient
    mailSession = Session.getDefaultInstance(System.getProperties()); // создаем почтовую сессию
  }

  // метод для проверки существования пользователя (в тестах не используется)
  public boolean doesUserExist(String name) {
    initTelnetSession();
    write("verify " + name);
    String result = readUntil("exist");
    closeTelnetSession();
    return result.trim().equals("User " + name + " exist");
  }

  // метод для создания нового пользователя на почтовом сервере
  public void createUser(String name, String passwd) {
    initTelnetSession(); // устанавливаем соединение по протоколу Telnet
    write("adduser " + name + " " + passwd); // пишем команду adduser + имя + пароль
    String result = readUntil("User " + name + " added"); // ждем пока на консоли не появится текст о добавлении пользователя
    closeTelnetSession(); // разрываем соединение
  }

  // метод для удаления нового пользователя на почтовом сервере (в тестах не используется)
  public void deleteUser(String name) {
    initTelnetSession();
    write("deluser " + name);
    String result = readUntil("User " + name + " deleted");
    closeTelnetSession();
  }

  // метод для установки соединения с почтовым сервером
  private void initTelnetSession() {
    // получаем свойства из конфигурационного файла
    mailserver = app.getProperty("mailserver.host");
    int port = Integer.parseInt(app.getProperty("mailserver.port"));
    String login = app.getProperty("mailserver.adminlogin");
    String password = app.getProperty("mailserver.adminpassword");

    try {
      telnet.connect(mailserver, port); // устанавливаем соединение с почтовым сервером
      in = telnet.getInputStream(); // входной поток - для чтения данных, которые отправляет нам TelnetClient
      out = new PrintStream(telnet.getOutputStream()); // выходной поток - для отправки команд клиенту

    } catch (Exception e) {
      e.printStackTrace();
    }

    // описываем схему взаимодействия с клиентом:
    readUntil("Login id:"); // дождаться пока на консоли появится текст "Login id:"
    write(""); // написать туда что-то
    readUntil("Password:"); // дождаться пока на консоли появится текст "Password:"
    write(""); // написать туда что-то

    readUntil("Login id:");
    write(login);
    readUntil("Password:");
    write(password);

    readUntil("Welcome " + login + ". HELP for a list of commands"); // дождаться пока на консоли появится текст
  }

  /* метод для чтения из входного потока (того, что выводит на консоль почтовый сервер)
  сравнение осуществляется посимвольно и сравнивается с заданным шаблоном (pattern):
  как только прочитан фрагмент, соответствующий шаблону, - ожидание завершается */
  private String readUntil(String pattern) {
    try {
      char lastChar = pattern.charAt(pattern.length() - 1);
      StringBuffer sb = new StringBuffer();
      char ch = (char) in.read();
      while (true) {
        System.out.print(ch);
        sb.append(ch);
        if (ch == lastChar) {
          if (sb.toString().endsWith(pattern)) {
            return sb.toString();
          }
        }
        ch = (char) in.read();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  // метод для отправки команд клиенту
  private void write(String value) {
    try {
      out.println(value);
      out.flush();
      System.out.println(value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // метод для разрыва соединения с почтовым сервером (отправка команды "quit")
  public void closeTelnetSession() {
    write("quit");
  }

  /* метод для удаления всех писем, полученных пользователем (в тестах не используется) -
  полезно для многократного использования одного почтового ящика.
  Каждое письмо помечается специальным флагом DELETED и при закрытии папки
  все помеченные письма будут удалены */
  public void drainMail(String username, String password) throws MessagingException {
    Folder inbox = openInbox(username, password);
    for (javax.mail.Message message : inbox.getMessages()) {
      message.setFlag(Flags.Flag.DELETED, true);
    }
    closeFolder(inbox);
  }

  // метод для закрытия почтового ящика
  private void closeFolder(Folder folder) throws MessagingException {
    /* закрываем папку (true говорит о том, что нужно удалить все письма помеченные к удалению
    (см. метод drainMail). Нам это не нужно, но какой-то параметр передать обязательно нужно и передаем true) */
    folder.close(true);
    store.close(); // разрываем соединение
  }

  // метод для открытия почтового ящика
  private Folder openInbox(String username, String password) throws MessagingException {
    // берем ранее созданную почтовую сессию и сообщаем, что хотим использовать протокол "pop3"
    store = mailSession.getStore("pop3");
    // устанавливаем соединение по этому протоколу с указанием адреса почтового сервера, а также имени и пароля
    store.connect(mailserver, username, password);
    // получаем доступ к папке с именем "INBOX" (стандартное название, по протоколу "pop3" можно получить доступ только к ней
    Folder folder = store.getDefaultFolder().getFolder("INBOX");
    // открываем папку на чтение и на запись (но достаточно только чтения, т.к. в тестах удалять письма не планируем)
    folder.open(folder.READ_WRITE);
    // возвращаем открытую папку для использования в методе getAllMail (чтобы в конце ее закрыть)
    return folder;
  }

  // метод для ожидания письма (нужен, т.к. письма приходят не мгновенно)
  public List<MailMessage> waitForMail(
          String username,
          String password,
          long timeout /*время ожидания*/)
          throws MessagingException, IOException {
    long now = System.currentTimeMillis(); // запоминаем момент начала ожидания

    /* В цикле while проверяем, что текущее время не превышает момент старта + таймаут.
    Если время истекло - выходим из цикла и выбрасываем исключение.
    Внутри (в цикле if) проверяем: если есть хотя бы одно письмо, то возвращаем список писем.
    Если же писем нет, то ждем в течение времени, указанного в Thread.sleep(),
    и снова заходим в цикл while.
    И так до тех пор пока либо не появится письмо, либо не окончится время ожидания */

    while (System.currentTimeMillis() < now + timeout) {
      List<MailMessage> allMail = getAllMail(username, password);
      if (allMail.size() > 0) {
        return allMail;
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    throw new Error("No mail :("); // если время истекло, то выбрасывается исключение
  }

  /* метод для извлечения писем из почтового ящика и преобразования их в модельный вид (описанный в MailMessage)
  (это нужно т.к. разные почтовые сервера могут использовать разный формат представления писем):
  берем список писем, превращаем в поток, применяем ко всем письмам функцию toModelMail,
  чтобы привести их к нужному нам модельному виду (описанному в MailMessage), и
  собираем получившиеся объекты в новый список */
  public List<MailMessage> getAllMail(String username, String password) throws MessagingException {
    Folder inbox = openInbox(username, password); // открываем почтовый ящик
    List<MailMessage> messages = Arrays.asList(inbox.getMessages()).stream().map((m) -> toModelMail(m)).collect(Collectors.toList());
    closeFolder(inbox); // закрываем почтовый ящик
    return messages;
  }

  // метод для преобразования реальных писем в модельные (соответствующие MailMessage)
  public static MailMessage toModelMail(javax.mail.Message m) {
    try {
      // 1. берем список адресов получателей и оставляем первого из них (т.к. известно, что получатель всего один)
      // 2. преобразуем письмо (т.к. оно содержит только текст) в строку и отправляем в объект MailMessage
      return new MailMessage(m.getAllRecipients()[0].toString(), (String) m.getContent());
    } catch (MessagingException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}