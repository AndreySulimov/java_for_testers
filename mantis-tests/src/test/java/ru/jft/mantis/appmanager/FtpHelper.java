package ru.jft.mantis.appmanager;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FtpHelper {

  private final ApplicationManager app;
  private FTPClient ftp;

  public FtpHelper(ApplicationManager app) {
    this.app = app;
    ftp = new FTPClient(); // объект, устаналивающий соединение и передающий файлы
  }

  // метод для загрузки нового файла и временного переименования старого файла
  public void upload(File file /*локальный файл, который нужно загрузить на удаленную машину*/,
                     String target /*имя удаленного файла*/,
                     String backup /*имя резервной копии файла на случай, если удаленный файл уже существует*/)
          throws IOException {
    ftp.connect(app.getProperty("ftp.host")); // устанавливаем соединение с сервером
    ftp.login(app.getProperty("ftp.login"), app.getProperty("ftp.password")); // авторизуемся
    ftp.deleteFile(backup); // удаляем предыдущую резервную копию
    ftp.rename(target, backup); // переименовываем удаленный файл (делаем резервную копию)
    ftp.enterLocalPassiveMode(); // включаем "пассивный режим передачи данных" - техническая манипуляция
    ftp.storeFile(target, new FileInputStream(file)); // передаем файл с локальной машины на удаленную и сохраняем его в удаленном файле target
    ftp.disconnect(); // разрываем соединение
  }

  // метод для восстановления старого файла
  public void restore(String backup, String target) throws IOException {
    ftp.connect(app.getProperty("ftp.host"));
    ftp.login(app.getProperty("ftp.login"), app.getProperty("ftp.password"));
    ftp.deleteFile(target); // удаляем загруженный файл
    ftp.rename(backup, target); // восстанавливаем оригинальный файл из резервной копии
    ftp.disconnect();
  }
}