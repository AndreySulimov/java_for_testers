package ru.jft.sandbox;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PointTests {

  @Test
  public void testDistance() {
    Point p1 = new Point(2, 2);
    Point p2 = new Point(2, 4);
    Assert.assertEquals(p1.distance(p2),2); // основной позитивный тест

    Point p3 = new Point(0, 0);
    Point p4 = new Point(0, 0);
    Assert.assertEquals(p3.distance(p4),0); // тест с нулем

    Point p5 = new Point(-2, -2);
    Point p6 = new Point(-2, -4);
    Assert.assertEquals(p5.distance(p6),2); // отрицательные значения

    Point p7 = new Point(100, 0);
    Point p8 = new Point(1, 0);
    Assert.assertEquals(p7.distance(p8),99); // большое число

    Point p9 = new Point(100, 0);
    Point p10 = new Point(1, 0);
    Assert.assertEquals(p10.distance(p9),99); // проверка на перестановку точек
  }
}
