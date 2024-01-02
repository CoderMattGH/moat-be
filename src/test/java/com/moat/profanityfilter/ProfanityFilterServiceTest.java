package com.moat.profanityfilter;

import com.moat.exceptions.FilterNotEnabledException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A test class to unit test the ProfanityFilterService.
 *
 * This test requires the following file in the root directory:
 * `./filters/prof_filter.txt`.
 *
 * If this file isn't present, then the test will fail.
 */
public class ProfanityFilterServiceTest {
    private final static Logger logger = LoggerFactory.getLogger(ProfanityFilterServiceTest.class);

    public ProfanityFilterServiceTest() {
        logger.info("Constructing ProfanityFilterServiceTest.");
    }

    @Test
    public void testIsValid_FilterDisabled() {
        ProfanityFilterService profanityFilterService =
                new ProfanityFilterServiceImpl(false);

        boolean exceptionOccurred = false;

        try {
            profanityFilterService.isValid("valid");
        } catch (FilterNotEnabledException e) {
            exceptionOccurred = true;
        }

        assertTrue(exceptionOccurred);
    }

    @Test
    public void testIsValid_ValidAndFilterEnabled() {
        ProfanityFilterService profanityFilterService =
                new ProfanityFilterServiceImpl(true);

        boolean result = profanityFilterService.isValid("valid");

        assertTrue(result);
    }

    @Test
    public void testIsValid_InValidAndFilterEnabled() {
        ProfanityFilterService profanityFilterService =
                new ProfanityFilterServiceImpl(true);

        boolean result = profanityFilterService.isValid("BLYAT");

        assertFalse(result);
    }
}
