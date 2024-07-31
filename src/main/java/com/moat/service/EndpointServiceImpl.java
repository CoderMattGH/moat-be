package com.moat.service;

import com.moat.constant.Constants;
import com.moat.constant.ValidationMsg;
import com.moat.exception.EndpointParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Service("endpointService")
public class EndpointServiceImpl implements EndpointService {
  private final static Logger logger =
      LoggerFactory.getLogger(EndpointServiceImpl.class);

  public String getEndpoints() throws EndpointParseException {
    logger.debug("In getEndpoints() in EndpointServiceImpl.");

    StringBuilder endpointsStr = new StringBuilder();

    try {
      InputStream bis = new BufferedInputStream(new ClassPathResource(
          Constants.ENDPOINTS_FILE_PATH).getInputStream());

      Reader reader = new BufferedReader(
          new InputStreamReader(bis, StandardCharsets.UTF_8));

      int c;
      while ((c = reader.read()) != -1) {
        endpointsStr.append((char) c);
      }
    } catch (Exception e) {
      throw new EndpointParseException(ValidationMsg.ERROR_GETTING_ENDPOINTS);
    }

    return endpointsStr.toString();
  }
}
