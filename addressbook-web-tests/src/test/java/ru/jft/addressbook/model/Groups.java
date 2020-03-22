package ru.jft.addressbook.model;

import com.google.common.collect.ForwardingSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Groups extends ForwardingSet<GroupData> {

  private Set<GroupData> delegate;

  public Groups(Groups groups) { // конструктор, строющий копию существующего объекта
    /* берем множество из существующего объекта, который передан в качестве параметра,
    строим новое множество, которое содержит те же самые элементы,
     и присваиваем это новое множество в качестве атрибута в новый создаваемый этим конструктором объект*/
    this.delegate = new HashSet<GroupData>(groups.delegate);

  }

  public Groups() {
    this.delegate = new HashSet<GroupData>();
  }

  public Groups(Collection<GroupData> groups) {
    this.delegate = new HashSet<GroupData>(groups); // строим множество объектов типа GroupData (HashSet) из коллекции
  }

  @Override
  protected Set<GroupData> delegate() {
    return delegate;
  }

  public Groups withAdded(GroupData group) {
    Groups groups = new Groups(this); // создаем копию
    groups.add(group); // в эту копию добавляем объект, который передан в качестве параметра
    return groups; //возвращаем копию с добавленной группой
  }

  public Groups without(GroupData group) {
    Groups groups = new Groups(this); // создаем копию
    groups.remove(group); // из этой копии удаляем объект, который передан в качестве параметра
    return groups; //возвращаем копию с удаленной группой
  }
}