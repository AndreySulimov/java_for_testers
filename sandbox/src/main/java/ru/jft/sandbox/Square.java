package ru.jft.sandbox;

public class Square { // имя класса

  public double l; // атрибут класса - длина стороны, имеет тип double

  // создаем конструктор
  public Square(double l) {
    this.l = l; // значение атрибута создаваемого объекта = значению параметра, переданного в конструктор
  }

  // создаем метод, в котором вычисляется площадь квадрата
  public double area() {
    return this.l * this.l;
  }
}
