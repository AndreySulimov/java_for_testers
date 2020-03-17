package ru.jft.addressbook.generators;

import ru.jft.addressbook.model.GroupData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class GroupDataGenerator {

  public static void main(String[] args) throws IOException {

    int count = Integer.parseInt(args[0]); // количество групп
    File file = new File(args[1]); // путь к файлу

    List<GroupData> groups = generateGroups(count);
    save(groups, file);
  }

  private static void save(List<GroupData> groups, File file) throws IOException { // сохранение списка в файл
    System.out.println(new File(".").getAbsolutePath());
    // открываем файл на запись
    Writer writer = new FileWriter(file);
    for (GroupData group : groups) { // проходимся в цикле по всем группам из списка groups
      writer.write(String.format("%s; %s; %s\n", group.getName(), group.getHeader(), group.getFooter())); // и каждую  из групп записываем в файл

    }
    writer.close(); // закрываем файл
  }

  private static List<GroupData> generateGroups(int count) {
    List<GroupData> groups = new ArrayList<GroupData>(); // создаем новый список объектов типа GroupData
    // заполняем список заданным количеством групп
    for (int i = 0; i < count; i++) {
      groups.add(new GroupData()
              .withName(String.format("test %s", i))
              .withHeader(String.format("header %s", i))
              .withFooter(String.format("footer %s", i)));
    }
    return groups; // возвращаем заполненный список
  }
}
