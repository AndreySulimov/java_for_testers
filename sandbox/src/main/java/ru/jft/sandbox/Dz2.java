package ru.jft.sandbox;

public class Dz2 {

  public static void main(String[] args) {

    Point p1 = new Point (1, 1);
    Point p2 = new Point (5, 5);
    System.out.println("Расстояние между точками с координатами "
            + "(" + p1.x1 + ", " + p1.y1 + ") " + "и " + "(" + p2.x2 + ", " + p2.y2 + ") "
            + "= " + distance(p1, p2));
  }

  public static double distance (Point p1, Point p2) {
    return Math.sqrt(Math.pow((p2.x2 - p1.x1), 2) + Math.pow((p2.y2 - p1.y1), 2));
  }
}
