package ru.jft.addressbook.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.jft.addressbook.model.GroupData;
import ru.jft.addressbook.model.Groups;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class GroupCreationTests extends TestBase {

  @DataProvider // провайдер тестовых данных
  public Iterator<Object[]> validGroups() throws IOException {
    // создаем и заполняем список массивов, каждый массив содержит набор данных для одного запуска тестового метода
    List<Object[]> list = new ArrayList<Object[]>();
    BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/groups.csv")));
    String line = reader.readLine();
    while (line != null) {
      String[] split = line.split(";");
      list.add(new Object[] {new GroupData().withName(split[0]).withHeader(split[1]).withFooter(split[2])});
      line = reader.readLine();
    }
    return list.iterator(); // возвращаем итератор списка массивов
  }

  @Test(dataProvider = "validGroups")
  public void testGroupCreation(GroupData group) throws Exception {
    app.goTo().groupPage();
    Groups before = app.group().all();
    app.group().create(group);
    assertThat(app.group().count(), equalTo(before.size() + 1)); // хеширование
    Groups after = app.group().all();
    assertThat(after, equalTo(
            before.withAdded(group.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt())))); // сравниваем множества before и after
  }

    /*вычисляем максимальный идентификатор:
    берем коллекцию, содержащую группы с уже известными идентификаторами,
    превращаем ее в поток
    и этот поток объектов типа GroupData превращаем в поток идентификаторов с помощью mapToInt.
    В качестве параметра в mapToInt передаем анонимную функцию, которая последовательно применяется
    ко всем элементам потока и каждый из них последовательно преобразуется в число.
    Затем вызываем метод max() для получения максимального из чисел и преобразуем результат в
    обычное целое число - getAsInt() - это и будет максимальный из идентификаторов*/


  @Test
  public void testBadGroupCreation() throws Exception { // негативный тест
    app.goTo().groupPage();
    Groups before = app.group().all();
    GroupData group = new GroupData().withName("Test2'"); // недопустимый сивол в названии группы
    app.group().create(group);
    assertThat(app.group().count(), equalTo(before.size()));
    Groups after = app.group().all();
    assertThat(after, equalTo(before));
  }
}