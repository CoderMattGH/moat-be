package com.moat.profanityfilter;

import com.moat.exception.FilterNotEnabledException;

public interface ProfanityFilterService {
  boolean isValid(String text) throws FilterNotEnabledException;

  boolean isEnabled();
}
