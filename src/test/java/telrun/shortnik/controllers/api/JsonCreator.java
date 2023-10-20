package telrun.shortnik.controllers.api;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.RedirectView;
import telrun.shortnik.dto.UrlResponse;
import telrun.shortnik.dto.UserResponse;

import java.lang.reflect.Type;
import java.util.List;

@Component
public class JsonCreator {

    private final Gson gson = new Gson();

    public String createJson(Object object) {
        return gson.toJson(object);
    }
    public List<UserResponse> convertJsonToObject(String json, Type type) {
         return gson.fromJson(json, type);
    }
    public UserResponse convertJsonToObject(String json, UserResponse userResponse) {
        return gson.fromJson(json, UserResponse.class);
    }
    public UrlResponse convertJsonToObject(String json, UrlResponse urlResponse) {
        return gson.fromJson(json, UrlResponse.class);
    }
}
