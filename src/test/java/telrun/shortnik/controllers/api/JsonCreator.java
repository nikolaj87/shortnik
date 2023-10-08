package telrun.shortnik.controllers.api;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.RedirectView;
import telrun.shortnik.dto.UserResponse;

import java.lang.reflect.Type;
import java.util.List;

@Component
public class JsonCreator {

    private final Gson gson = new Gson();

    String createJson(Object object) {
        return gson.toJson(object);
    }
     List<UserResponse> convertJsonToObject(String json, Type type) {
         return gson.fromJson(json, type);
    }
    RedirectView convertJsonToObject(String json, Class<RedirectView> clazz) {
        return gson.fromJson(json, clazz);
    }
}
