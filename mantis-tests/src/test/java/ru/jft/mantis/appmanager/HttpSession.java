package ru.jft.mantis.appmanager;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpSession {

  private CloseableHttpClient httpClient;
  private ApplicationManager app;

  public HttpSession(ApplicationManager app) {
    this.app = app;
    /* создаем новую сессию для работы по протоколу http - объект, который будет отправлять запросы на сервер.
    В созданном объекте устанавливается стратегия перенаправлений (это нужно для автоматического перенаправления) */
    httpClient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build();
  }

  // метод для выполнения авторизации
  public boolean login(String username, String password) throws IOException {
    HttpPost post = new HttpPost(app.getProperty("web.baseUrl") + "/login.php"); // создаем post-запрос
    // формируем набор параметров запроса
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("username", username));
    params.add(new BasicNameValuePair("password", "password"));
    params.add(new BasicNameValuePair("secure_session", "on"));
    params.add(new BasicNameValuePair("return", "index.php"));

    post.setEntity(new UrlEncodedFormEntity(params));  // упаковываем параметры и помещаем в созданный запрос
    CloseableHttpResponse response = httpClient.execute(post); // отправляем запрос и сохраняем ответ (response)
    String body = geTextFrom(response); // получаем текст ответа
    return body.contains(String.format("<span class=\"user-info\">%s</span>", username)); // ищем в ответе строку с именем пользователя
  }

  // вспомогательный метод для получения текста ответа на запрос
  private String geTextFrom(CloseableHttpResponse response) throws IOException {
    try {
      return EntityUtils.toString(response.getEntity());
    } finally {
      response.close();
    }
  }

  // метод для проверки того, какой пользователь авторизовался в системе
  public boolean isLoggedInAs(String username) throws IOException {
    HttpGet get = new HttpGet(app.getProperty("web.baseUrl") + "/index.php"); // выполняем get-запрос информации о главной странице
    CloseableHttpResponse response = httpClient.execute(get); // отправляем запрос и сохраняем ответ (response)
    String body = geTextFrom(response); // получаем текст ответа
    return body.contains(String.format("<span class=\"user-info\">%s</span>", username)); // ищем в ответе строку с именем пользователя
  }
}