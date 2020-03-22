package ru.jft.addressbook.model;

import com.google.gson.annotations.Expose;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.File;
import java.util.Objects;

@Entity
@Table(name = "addressbook")
public class ContactData {
  @Id
  @Column(name = "id")
  // добавляем атрибут класса
  private int id = Integer.MAX_VALUE; // в id присваем Integer.MAX_VALUE, чтобы созданный контакт при сравнении оказался самым последним

  @Expose // использовать поле при сериализации в json
  @Column(name = "firstname")
  private String firstname;

  @Expose
  @Column(name = "lastname")
  private String lastname;

  @Expose
  @Column(name = "address")
  @Type(type = "text")
  private String address;

  @Expose
  @Column(name = "email")
  @Type(type = "text")
  private String email;

  @Transient // не извлекать из БД
  // @Column(name = "email2")
  // @Type(type = "text")
  private String email2;

  @Transient
  // @Column(name = "email3")
  // @Type(type = "text")
  private String email3;

  @Transient
  private String allEmail;

  @Expose
  @Transient
  private String group;

  @Expose
  @Column(name = "home")
  @Type(type = "text")
  private String homePhone;

  @Transient
  //@Column(name = "mobile")
  //@Type(type = "text")
  private String mobilePhone;

  @Transient
  //@Column(name = "work")
  //@Type(type = "text")
  private String workPhone;

  @Transient
  private String allPhones;

  @Transient
  // @Column(name = "photo")
  // @Type(type = "text")
  private File photo;

  public ContactData withId(int id) {
    this.id = id;
    return this;
  }

  public ContactData withFirstname(String firstname) {
    this.firstname = firstname;
    return this;
  }

  public ContactData withLastname(String lastname) {
    this.lastname = lastname;
    return this;
  }

  public ContactData withAddress(String address) {
    this.address = address;
    return this;
  }

  public ContactData withEmail(String email) {
    this.email = email;
    return this;
  }

  public ContactData withEmail2(String email2) {
    this.email2 = email2;
    return this;
  }

  public ContactData withEmail3(String email3) {
    this.email3 = email3;
    return this;
  }

  public ContactData withAllEmail(String allEmail) {
    this.allEmail = allEmail;
    return this;
  }

  public ContactData withGroup(String group) {
    this.group = group;
    return this;
  }

  public ContactData withHomePhone(String home) {
    this.homePhone = home;
    return this;
  }

  public ContactData withMobilePhone(String mobile) {
    this.mobilePhone = mobile;
    return this;
  }

  public ContactData withWorkPhone(String work) {
    this.workPhone = work;
    return this;
  }

  public ContactData withAllPhones(String allPhones) {
    this.allPhones = allPhones;
    return this;
  }

  public ContactData withPhoto(File photo) {
    this.photo = photo;
    return this;
  }

  public int getId() {
    return id;
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

  public String getEmail() {
    return email;
  }

  public String getEmail2() {
    return email2;
  }

  public String getEmail3() {
    return email3;
  }

  public String getAllEmail() {
    return allEmail;
  }

  public String getGroup() {
    return group;
  }

  public String getHomePhone() {
    return homePhone;
  }

  public String getMobilePhone() {
    return mobilePhone;
  }

  public String getWorkPhone() {
    return workPhone;
  }

  public String getAllPhones() {
    return allPhones;
  }

  public File getPhoto() {
    return photo;
  }

  // метод преобразования в строку
  @Override
  public String toString() {
    return "ContactData{" +
            "id=" + id +
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
    return id == that.id &&
            Objects.equals(firstname, that.firstname) &&
            Objects.equals(lastname, that.lastname) &&
            Objects.equals(address, that.address) &&
            Objects.equals(email, that.email) &&
            Objects.equals(homePhone, that.homePhone);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstname, lastname, address, email, homePhone);
  }
}