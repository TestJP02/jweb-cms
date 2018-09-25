package app.jweb.exception;


import app.jweb.ApplicationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
public class ApplicationExceptionTest {
    @Test
    public void message() {
        ApplicationException e = new ApplicationException("some exception, name={}", "name");
        assertEquals("some exception, name=name", e.getMessage());
    }
}