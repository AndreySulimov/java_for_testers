package ru.jft.sandbox;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SquareTests {

  @Test // аннотация
  // создаем метод testArea, который не имеет возвращаемого значения
  public void testArea() {
    Square s = new Square(5); // создаем новый квадрат со стороной 5
    Assert.assertEquals(s.area(), 25.0); // сравниваем вычисленную и ожидаему площадь
  }
}
