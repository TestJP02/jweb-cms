package io.sited.exception;


import io.sited.ApplicationException;
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