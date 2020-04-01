package ru.jft.mantis.model;

public class MailMessage {

  public String to; // кому пришло письмо
  public String text; // текст письма

  public MailMessage(String to, String text) {
    this.to = to;
    this.text = text;
  }
}