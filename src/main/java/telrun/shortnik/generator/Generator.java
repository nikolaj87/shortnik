package telrun.shortnik.generator;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Generator {
    //57 млрд комб
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
