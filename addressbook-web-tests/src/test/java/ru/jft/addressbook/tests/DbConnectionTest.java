package ru.jft.addressbook.tests;

import org.testng.annotations.Test;
import ru.jft.addressbook.model.GroupData;
import ru.jft.addressbook.model.Groups;

import java.sql.*;

public class DbConnectionTest {

  @Test
  public void testDbConnection() {
    Connection conn = null;
    try {
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/addressbook?user=root&password=&serverTimezone=UTC");
      Statement st = conn.createStatement();
      // создаем объект ResultSet, который в каждый момент времени указывает на одну строчку таблицы с результатами sql-запроса
      ResultSet rs = st.executeQuery("select group_id, group_name, group_header, group_footer from group_list");
      // создаем объект типа Groups и добавляем в него созданные объекты типа GroupData
      Groups groups = new Groups();
      // пока есть элементы в множестве результатов sql-запроса, можно выполнять с ними какие-то действия
      while (rs.next()) {
        groups.add(new GroupData()
                .withId(rs.getInt("group_id"))
                .withName(rs.getString("group_name"))
                .withHeader(rs.getString("group_header"))
                .withFooter(rs.getString("group_footer")));
      }
      // закрываем соединение с БД для освобождения ресурсов
      rs.close();
      st.close();
      conn.close();

      System.out.println(groups);

    } catch (SQLException ex) {

      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
    }
  }
}
