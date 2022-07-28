package de.hzd.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ThrowablesTest {

    @Test
    public void toString_NullGiven_ShouldReturnNull() {
        assertNull(Throwables.toString(null));
    }

    @Test
    public void toString_ThrowableGiven_ShouldReturnString() {
        assertNotNull(Throwables.toString(new Throwable()));
    }

    @Test
    public void getRootCause_NullGiven_ShouldReturnNull() {
        assertNull(Throwables.getRootCause(null));
    }

    @Test
    public void getRootCause_SimpleExceptionGiven_ShouldReturnThatException() {
        Exception ex = new Exception();
        Throwable rootCause = Throwables.getRootCause(ex);
        assertTrue(ex == rootCause);
    }

    @Test
    public void getRootCause_ChainedExceptionsGiven_ShouldReturnRootException() {
        Exception ex1 = new Exception();
        Exception ex2 = new Exception(ex1);
        Exception ex3 = new Exception(ex2);
        Throwable rootCause = Throwables.getRootCause(ex3);
        assertTrue(ex1 == rootCause);
    }
}
