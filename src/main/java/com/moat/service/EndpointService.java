package com.moat.service;

import com.moat.exception.EndpointParseException;

public interface EndpointService {
  String getEndpoints() throws EndpointParseException;
}
