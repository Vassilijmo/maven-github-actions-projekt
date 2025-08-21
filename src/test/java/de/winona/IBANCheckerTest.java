package de.winona;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IBANCheckerTest {

    @Test
    void validIban_returnsTrue() {
        assertTrue(IBANChecker.validate("DE22790200760027913168"));
    }

    @Test
    void invalidIban_returnsFalse() {
        assertFalse(IBANChecker.validate("DE21790200760027913173"));
    }

    @Test
    void wrongLength_returnsFalse() {
        assertFalse(IBANChecker.validate("DE227902007600279131"));
    }

    @Test
    void unknownCountry_returnsFalse() {
        assertFalse(IBANChecker.validate("XX22790200760027913168"));
    }
}
