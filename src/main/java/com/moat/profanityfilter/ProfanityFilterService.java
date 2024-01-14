package com.moat.profanityfilter;

import com.moat.exceptions.FilterNotEnabledException;

public interface ProfanityFilterService {
    /**
     * Checks if the supplied text contains any profanity included in the profanity filter file.
     *
     * @return Returns 'true' if no profane words were detected or 'false' otherwise.
     *
     * @throws com.moat.exceptions.FilterNotEnabledException the Profanity Filter is not enabled,
     * then an error is thrown.
     */
    boolean isValid(String text) throws FilterNotEnabledException;

    /**
     * Returns true if the ProfanityFilter is enabled, or false if it is disabled.
     */
    boolean isEnabled();
}
