package telrun.shortnik.controllers.api;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import telrun.shortnik.dto.UrlRequest;
import telrun.shortnik.dto.UserRequest;

import java.io.IOException;
import java.util.Map;

@Component
public class GsoupHttpConnector {

    private final String serverUrl;

    public GsoupHttpConnector(@Value("${server.port}") int port) {
        this.serverUrl = "http://localhost:" + port + "/";
    }

    public Connection.Response deleteRequestJson(String controllerUrl) throws IOException {
        return Jsoup.connect(serverUrl + controllerUrl)
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .method(Connection.Method.DELETE)
                .execute();
    }
    public Connection.Response deleteRequestJson(String controllerUrl, Map<String, String> cookie) throws IOException {
        return Jsoup.connect(serverUrl + controllerUrl)
                .cookies(cookie)
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .method(Connection.Method.DELETE)
                .execute();
    }

    public Connection.Response getRequestJson(String controllerUrl) throws IOException {
        return Jsoup.connect(serverUrl + controllerUrl)
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .method(Connection.Method.GET)
                .execute();
    }
    public Connection.Response getRequestJson(String controllerUrl, Map<String, String> cookie) throws IOException {
        return Jsoup.connect(serverUrl + controllerUrl)
                .cookies(cookie)
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .method(Connection.Method.GET)
                .execute();
    }

    public Connection.Response postRequestJson(String body, String controllerUrl) throws IOException {
        return Jsoup.connect(serverUrl + controllerUrl)
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .header("Content-Type", "application/json")
                .method(Connection.Method.POST)
                .requestBody(body)
                .execute();
    }
    public Connection.Response postRequestJson(String body, String controllerUrl, Map<String, String> cookie) throws IOException {
        return Jsoup.connect(serverUrl + controllerUrl)
                .ignoreContentType(true)
                .header("Content-Type", "application/json")
                .cookies(cookie)
                .method(Connection.Method.POST)
                .requestBody(body)
                .execute();
    }

    public Connection.Response getRequestHtml(String controllerUrl) throws IOException {
        return Jsoup.connect(serverUrl + controllerUrl)
                .ignoreHttpErrors(true)
                .method(Connection.Method.GET)
                .execute();
    }
    public Connection.Response getRequestHtml(String controllerUrl, Map<String, String> cookies) throws IOException {
        return Jsoup.connect(serverUrl + controllerUrl)
                .cookies(cookies)
                .method(Connection.Method.GET)
                .execute();
    }

    public Connection.Response postRequestHtml(UserRequest userRequest, String controllerUrl) throws IOException {
        return Jsoup.connect(serverUrl + controllerUrl)
                .data("name", userRequest.getName())
                .data("password", userRequest.getPassword())
                .data("email", userRequest.getEmail())
                .method(Connection.Method.POST)
                .execute();
    }
    public Connection.Response postRequestHtml(UrlRequest urlRequest, Map<String, String> cookies, String controllerUrl) throws IOException {
        return Jsoup.connect(serverUrl + controllerUrl)
                .data("longUrl", urlRequest.getLongUrl())
                .cookies(cookies)
                .method(Connection.Method.POST)
                .execute();
    }
    public Connection.Response postRequestHtml(String dataLogin, String dataPassword, String controllerUrl) throws IOException {
        return Jsoup.connect(serverUrl + controllerUrl)
                .ignoreContentType(true)
                .data("username", dataLogin)
                .data("password", dataPassword)
                .method(Connection.Method.POST)
                .execute();
    }
}
