package com.moat.profanityfilter;

import com.moat.constants.Constants;
import com.moat.exceptions.FilterNotEnabledException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * Class to filter out profanity in nicknames, etc.
 */
@Component
public class ProfanityFilterServiceImpl implements ProfanityFilterService {
    private final static Logger logger = LoggerFactory.getLogger(ProfanityFilterServiceImpl.class);

    private boolean enabled;
    private File filterFile;
    private Set<String> profFilter;

    public ProfanityFilterServiceImpl(
            @Value("${moat.filters.load-profanity-filter}") boolean loadFilter) {
        logger.info("Constructing ProfanityFilterServiceImpl");
        enabled = false;

        if (loadFilter) {
            logger.info("Initialising Profanity Filter.");
            init();
        } else {
            logger.info("Skipping initialising Profanity Filter.");
        }
    }

    private void init() {
        profFilter = new HashSet<>();

        String filterFilePath = Constants.FILTERS_DIR + File.separator + Constants.FILTER_PROF_FILE;
        filterFile = new File(filterFilePath);

        boolean loaded = loadProfanityFilter();

        if (loaded) {
            enabled = true;
            logger.info("Profanity Filter was successfully loaded.");
        } else {
            enabled = false;
            logger.error("Failed to load Profanity Filter! Disabling filter.");
        }
    }

    private boolean loadProfanityFilter() {
        logger.info("Loading Profanity Filter from file: " + filterFile.getAbsolutePath());

        try {
            BufferedReader br = new BufferedReader(new FileReader(filterFile));

            int i = 0;
            for (String line; (line = br.readLine()) != null;) {
                line = line.trim();

                if (!line.isEmpty()) {
                    profFilter.add(line);
                    i++;
                }
            }

            logger.info("Added " + i + " words to Profanity Filter.");
        } catch (Exception e) {
            logger.error("Failed to load Profanity Filter file!");
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean isValid(String text) throws FilterNotEnabledException {
        if (!enabled) {
            throw new FilterNotEnabledException("Profanity Filter is not enabled.");
        }

        if (text == null) {
            logger.error("Parameter 'text' cannot be null.");
            throw new NullPointerException();
        }

        text = text.trim();
        text = text.toUpperCase();

        Iterator<String> iterator = profFilter.iterator();
        while (iterator.hasNext()) {
            String profaneWord = iterator.next();

            // Convert to uppercase and compare.
            if (text.toUpperCase().contains(profaneWord.toUpperCase())) {
                logger.info("Profane word detected.");

                return false;
            }
        }

        return true;
    }

    public boolean isEnabled() {
        return this.enabled;
    }
}
