package app.jweb.exception;

import app.jweb.ApplicationException;
import app.jweb.util.exception.Exceptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * @author chi
 */
public class ExceptionsTest {
    @Test
    public void dump() {
        String stackTrace = Exceptions.stackTrace(new ApplicationException("it happens"));
        assertNotNull(stackTrace);
    }
}