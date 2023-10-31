package telrun.shortnik.generator;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * This class generates short links. Here are the allowed characters: {@link Generator#ALLOWED_CHARS}.
 * A short link consists of 6 random characters and provides approximately 60 billion combinations.
 */
@Component
public class Generator {
    private static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LINK_SIZE = 6;
    private final Random random = new Random();
    private final StringBuilder shortLink = new StringBuilder();
    public String generate() {
        shortLink.setLength(0);
        char temp;
        for (int i = 0; i < LINK_SIZE; i++) {
            temp = ALLOWED_CHARS.charAt(random.nextInt(ALLOWED_CHARS.length()));
            shortLink.append(temp);
        }
        return shortLink.toString();
    }

}
