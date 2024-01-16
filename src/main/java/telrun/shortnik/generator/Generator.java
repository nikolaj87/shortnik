package telrun.shortnik.generator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * This class generates short links. The allowed characters are: {@link Generator#ALLOWED_CHARS}.
 * A short link consists of 6 random characters and provides approximately 60 billion combinations.
 */
@Component
public class Generator {
    @Value("${allowed_chars}")
    private String ALLOWED_CHARS;
    @Value("${link_size}")
    private int LINK_SIZE;

    public String generate() {
        Random random = new Random();
        StringBuilder shortLink = new StringBuilder();
        char temp;
        for (int i = 0; i < LINK_SIZE; i++) {
            temp = ALLOWED_CHARS.charAt(random.nextInt(ALLOWED_CHARS.length()));
            shortLink.append(temp);
        }
        return shortLink.toString();
    }
}
