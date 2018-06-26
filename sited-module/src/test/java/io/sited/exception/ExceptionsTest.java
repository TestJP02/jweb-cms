package io.sited.exception;

import io.sited.ApplicationException;
import io.sited.util.exception.Exceptions;
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