package ru.jft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.jft.addressbook.model.ContactData;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactPhoneTests extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {
        // проверка существования хотя бы одного контакта: если список пустой, то контакт нужно создать
        if (app.contact().all().size() == 0) {
            app.contact().create(new ContactData()
                    .withFirstname("Андрей")
                    .withLastname("Сулимов")
                    .withAddress("Злынка")
                    .withHomePhone("89001234567")
                    .withMobilePhone("+7(900)1234567")
                    .withWorkPhone("8-900-123-45-67")
                    .withEmail("test@mail.ru")
                    .withEmail2("test2@mail.ru")
                    .withEmail3("test3@mail.ru")
                    .withGroup("Test2"), true);
        }
    }

    @Test
    public void testContactPhones() {
        app.goTo().homePage();
        ContactData contact = app.contact().all().iterator().next(); // загружаем множество контактов и выбираем рандомный контакт
        ContactData contactInfoFromEditForm = app.contact().infoFromEditForm(contact); // загружаем информацию о контакте из формы редактирования

        // сравниваем номера телефонов на главной странице и на странице редактирования контакта
        assertThat(contact.getAllPhones(), equalTo(mergePhones(contactInfoFromEditForm)));
    }

    private String mergePhones(ContactData contact) {
        /* формируем коллекцию (Arrays.asList) из телефонов, которые будем склеивать,
        из этого списка отсеиваем элементы равные null, а остальные будем склеивать.
        Для этого превращаем список в поток (stream()) и отфильтровываем получившийся поток от пустых строк,
        иначе они будут мешать при склеивании.
        Для этого используется функция filter, в которую в качестве параметра передается анонимная функция,
        принимающая на вход строку и возвращающая элементы неравные пустой строке.
        Затем необходимо применить функцию очистки cleaned, для чего необходимо использовать функцию map,
        которая предназначена для применения к элементам потока какой-то функции и возврата потока,
        состоящего из результатов этой функции.
        В результате получаем новый поток, в котором удалены все пустые строки, которые и нужно склеить вместе.
        Для этого используется функция collect, принимающая в качестве параметра коллектор joining,
        который умеет склеивать все элементы потока в одну большую строку.
        В качестве параметра коллектор принимает разделить ("\n") - строку, вставляющуюся между склеиваемыми фрагментами.
        Результатом выполнения функции collect является строка, и именно ее нужно вернуть в качестве результата. */

        return Arrays.asList(contact.getHomePhone(), contact.getMobilePhone(), contact.getWorkPhone())
                .stream().filter((s) -> !s.equals(""))
                .map(ContactPhoneTests::cleaned)
                .collect(Collectors.joining("\n"));
    }

    // функция для приведения номера телефона к очищенному виду
    public static String cleaned(String phone) {
        return phone.replaceAll("\\s", "").replaceAll("[-()]", "");
    }
}