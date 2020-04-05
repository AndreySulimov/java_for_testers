package ru.jft.mantis.model;

import com.google.common.collect.ForwardingSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Users extends ForwardingSet<UserData> {

  private Set<UserData> delegate;

  public Users(Users users) { // конструктор, строящий копию существующего объекта
    /* берем множество из существующего объекта, который передан в качестве параметра,
    строим новое множество, которое содержит те же самые элементы,
    и присваиваем это новое множество в качестве атрибута в новый создаваемый этим конструктором объект*/
    this.delegate = new HashSet<UserData>(users.delegate);

  }

  public Users() {
    this.delegate = new HashSet<UserData>();
  }

  public Users(Collection<UserData> users) {
    this.delegate = new HashSet<UserData>(users); // строим множество объектов типа UserData (HashSet) из коллекции
  }

  @Override
  protected Set<UserData> delegate() {
    return delegate;
  }
}