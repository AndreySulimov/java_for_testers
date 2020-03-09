package ru.jft.addressbook.model;

import java.util.Objects;

public class ContactData {
  private int id; // добавляем атрибут класса
  private final String firstname;
  private final String lastname;
  private final String address;
  private final String telephone;
  private final String email;
  private String group;

  // создаем конструктор с параметрами, в которые будут передаваться соответствующие значения
  public ContactData(int id, String firstname, String lastname, String address, String telephone, String email, String group) {
    this.id = id; // присваиваем значение параметра в атрибут (в поле объекта)
    this.firstname = firstname;
    this.lastname = lastname;
    this.address = address;
    this.telephone = telephone;
    this.email = email;
    this.group = group;
  }

  // создаем еще один конструктор, который не принимает идентификатор (id) в качестве параметра
  public ContactData(String firstname, String lastname, String address, String telephone, String email, String group) {
    this.id = Integer.MAX_VALUE; // в этом конструкторе для id присваем Integer.MAX_VALUE, чтобы созданный контакт при сравнении оказался самым последним
    this.firstname = firstname;
    this.lastname = lastname;
    this.address = address;
    this.telephone = telephone;
    this.email = email;
    this.group = group;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public String getAddress() {
    return address;
  }

  public String getTelephone() {
    return telephone;
  }

  public String getEmail() {
    return email;
  }

  public String getGroup() {
    return group;
  }

  // метод преобразования в строку
  @Override
  public String toString() {
    return "ContactData{" +
            "id='" + id + '\'' +
            ", firstname='" + firstname + '\'' +
            ", lastname='" + lastname + '\'' +
            '}';
  }

  // метод для сравнения объектов типа ContactData, сгенерирован средой разработки
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ContactData that = (ContactData) o;
    return Objects.equals(firstname, that.firstname) &&
            Objects.equals(lastname, that.lastname);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstname, lastname);
  }
}