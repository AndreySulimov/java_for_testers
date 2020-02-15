package ru.jft.sandbox;

public class Dz2 {

  public static void main(String[] args) {

    Point p1 = new Point(3, 2);
    Point p2 = new Point(7, 8);

    System.out.println("Расстояние между точками с координатами "
            + "(" + p1.x + ", " + p1.y + ") " + "и " + "(" + p2.x + ", " + p2.y + ") "
            + "= " + p1.distance(p2));

  }
}