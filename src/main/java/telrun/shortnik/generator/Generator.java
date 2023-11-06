package telrun.shortnik.generator;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * This class generates short links. The allowed characters are: {@link Generator#ALLOWED_CHARS}.
 * A short link consists of 6 random characters and provides approximately 60 billion combinations.
 */
@Component
public class Generator {
    private static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LINK_SIZE = 6;
    private final Random random = new Random();
    public String generate() {
        StringBuilder shortLink = new StringBuilder();
        char temp;
        for (int i = 0; i < LINK_SIZE; i++) {
            temp = ALLOWED_CHARS.charAt(random.nextInt(ALLOWED_CHARS.length()));
            shortLink.append(temp);
        }
        return shortLink.toString();
    }
}
