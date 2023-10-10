package telrun.shortnik.controllers.api;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GsoupHttpConnector {

    private final String urlPart1;

    public GsoupHttpConnector(@Value("${server.port}") int port) {
        this.urlPart1 = "http://localhost:" + port + "/";
    }

    public Connection.Response deleteRequestJson(String urlPart2) throws IOException {
        return Jsoup.connect(urlPart1 + urlPart2)
                .ignoreContentType(true)
                .method(Connection.Method.DELETE)
                .execute();
    }

    public Connection.Response getRequestJson(String urlPart2) throws IOException {
        return Jsoup.connect(urlPart1 + urlPart2)
                .ignoreContentType(true)
                .method(Connection.Method.GET)
                .execute();
    }

    public Connection.Response postRequestJson(String body, String urlPart2) throws IOException {
        return Jsoup.connect(urlPart1 + urlPart2)
                .ignoreContentType(true)
                .header("Content-Type", "application/json")
//                .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString(("testUser" + ":" + "testPassword").getBytes()))
                .method(Connection.Method.POST)
                .requestBody(body)
                .execute();
    }

    public Connection.Response getRequestHtml(String urlPart2) throws IOException {
        return Jsoup.connect(urlPart1 + urlPart2)
                .method(Connection.Method.GET)
                .execute();
    }

    public Connection.Response postRequestHtml(String urlPart2) throws IOException {
        return Jsoup.connect(urlPart1 + urlPart2)
                .method(Connection.Method.POST)
                .execute();
    }

    public Connection.Response patchRequestJson(String urlPart2) throws IOException {
        return Jsoup.connect(urlPart1 + urlPart2)
                .ignoreContentType(true)
                .header("Content-Type", "application/json")
                .method(Connection.Method.PATCH)
                .requestBody("")
                .execute();
    }
}
