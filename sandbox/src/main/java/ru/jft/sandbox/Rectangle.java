package ru.jft.sandbox;

public class Rectangle { // имя класса

  public double a; // одна сторона
  public double b; // вторая сторона

  // создаем конструктор
  public Rectangle(double a, double b) {
    this.a = a; // значения атрибутов создаваемого объекта = значения параметров, переданных в конструктор
    this.b = b;
  }

  // создаем метод, в котором вычисляется площадь прямоугольника
  public double area(){
    return this.a * this.b;
  }
}
