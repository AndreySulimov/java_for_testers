package ru.jft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.*;
import ru.jft.addressbook.model.GroupData;

import java.util.Set;

public class GroupCreationTests extends TestBase {

  @Test
  public void testGroupCreation() throws Exception {
    app.goTo().groupPage();
    Set<GroupData> before = app.group().all();
    GroupData group = new GroupData().withName("Test2");
    app.group().create(group);
    Set<GroupData> after = app.group().all();
    Assert.assertEquals(after.size(), before.size() + 1);

    group.withId(after.stream().max((o1, o2) -> Integer.compare(o1.getId(), o2.getId())).get().getId());
    /*вычисляем максимальный идентификатор:
    берем коллекцию, содержащую группы с уже известными идентификаторами,
    превращаем ее в поток
    и этот поток объектов типа GroupData превращаем в поток идентификаторов с помощью mapToInt.
    В качестве параметра в mapToInt передаем анонимную функцию, которая последовательно применяется
    ко всем элементам потока и каждый из них последовательно преобразуется в число.
    Затем вызываем метод max() для получения максимального из чисел и преобразуем результат в
    обычное целое число - getAsInt() - это и будет максимальный из идентификаторов*/
    group.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt());

    before.add(group);
    Assert.assertEquals(before, after);
  }
}