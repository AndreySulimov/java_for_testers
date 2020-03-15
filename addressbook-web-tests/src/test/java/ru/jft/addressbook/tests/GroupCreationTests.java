package ru.jft.addressbook.tests;

import org.testng.annotations.Test;
import ru.jft.addressbook.model.GroupData;
import ru.jft.addressbook.model.Groups;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class GroupCreationTests extends TestBase {

  @Test
  public void testGroupCreation() throws Exception {
    app.goTo().groupPage();
    Groups before = app.group().all();
    GroupData group = new GroupData().withName("Test2");
    app.group().create(group);
    assertThat(app.group().count(), equalTo(before.size() + 1)); // хеширование
    Groups after = app.group().all();

    /*вычисляем максимальный идентификатор:
    берем коллекцию, содержащую группы с уже известными идентификаторами,
    превращаем ее в поток
    и этот поток объектов типа GroupData превращаем в поток идентификаторов с помощью mapToInt.
    В качестве параметра в mapToInt передаем анонимную функцию, которая последовательно применяется
    ко всем элементам потока и каждый из них последовательно преобразуется в число.
    Затем вызываем метод max() для получения максимального из чисел и преобразуем результат в
    обычное целое число - getAsInt() - это и будет максимальный из идентификаторов*/

    assertThat(after, equalTo(
            before.withAdded(group.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt())))); // сравниваем множества before и after
  }

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