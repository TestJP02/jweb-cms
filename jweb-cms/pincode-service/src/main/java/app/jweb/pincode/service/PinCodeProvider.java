package app.jweb.pincode.service;

import app.jweb.pincode.PinCodeOptions;

import javax.inject.Inject;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

/**
 * @author chi
 */
public class PinCodeProvider implements Supplier<String> {
    @Inject
    PinCodeOptions options;

    @Override
    public String get() {
        if (options.length <= 0) {
            return "1111";
        } else {
            StringBuilder b = new StringBuilder();
            ThreadLocalRandom random = ThreadLocalRandom.current();
            for (int i = 0; i < options.length; i++) {
                b.append(Math.abs(random.nextInt(10)));
            }
            return b.toString();
        }
    }
}