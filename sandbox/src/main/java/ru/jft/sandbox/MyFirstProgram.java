package ru.jft.sandbox;

public class MyFirstProgram {

  public static void main(String[] args) {
    hello("world"); // вызываем функцию hello и передаем в нее в качестве параметра текст
    hello("user");
    hello("Andrey");

    Square s = new Square(5); // создаем объект s типа Square со значением параметра l = 5
    System.out.println("Площадь квадрата со стороной " + s.l + " = " + s.area()); // s.l - обращаеся к атрибуту l объекта s

    Rectangle r = new Rectangle(4, 6); // создаем объект r типа Rectangle
    System.out.println("Площадь прямоугольника со сторонами " + r.a + " и " + r.b + " = " + r.area());
  }

  // функция, выводящая на экран текст
  // hello - имя функции, String - тип аргумента функции, somebody - аргумент функции
  public static void hello(String somebody) {
    System.out.println("Hello, " + somebody + "!");
  }
}