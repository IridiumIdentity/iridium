package software.iridium.api.filter;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {
  public static final Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);
  private final String[] filteredFields = {"password", "currentPassword", "newPassword"};

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    long startTimeInMillis = System.currentTimeMillis();
    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
    try {
      logBeforeMessage(requestWrapper);
      chain.doFilter(requestWrapper, response);
    } finally {
      logAfterMessage(requestWrapper, startTimeInMillis);
    }
  }

  private void logBeforeMessage(ContentCachingRequestWrapper requestWrapper) {
    logger.info(
        String.format(
            "Before request - URI: %s, Method: %s",
            getRequestUri(requestWrapper), requestWrapper.getMethod()));
  }

  private void logAfterMessage(
      ContentCachingRequestWrapper requestWrapper, long startTimeInMillis) {
    long timeToProcessInMillis = System.currentTimeMillis() - startTimeInMillis;
    String messageFormat =
        "After request - URI: %s, Method: %s, Payload: %s, Time to process: %sms";
    logger.info(
        String.format(
            messageFormat,
            getRequestUri(requestWrapper),
            requestWrapper.getMethod(),
            getPayload(requestWrapper),
            timeToProcessInMillis));
  }

  private String getRequestUri(ContentCachingRequestWrapper requestWrapper) {
    String requestUri = requestWrapper.getRequestURI();
    String queryString = requestWrapper.getQueryString();
    if (StringUtils.isNotBlank(queryString)) {
      requestUri = requestUri + "?" + queryString;
    }
    return requestUri;
  }

  private String getPayload(ContentCachingRequestWrapper requestWrapper) {
    String payload = "No Payload";
    byte[] contentBytes = requestWrapper.getContentAsByteArray();
    if (contentBytes.length > 0) {
      try {
        ObjectMapper mapper = new ObjectMapper();
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter(
            "requestPayloadObjectForLogging",
            SimpleBeanPropertyFilter.serializeAllExcept(filteredFields));
        mapper.setFilterProvider(filterProvider);
        mapper.addMixIn(Object.class, PropertyFilterMixIn.class);
        Object jsonObject = mapper.readValue(contentBytes, Object.class);
        payload = mapper.writer().writeValueAsString(jsonObject);
      } catch (IOException io) {
        payload = "unable to parse payload";
      }
    }
    return payload;
  }

  @JsonFilter("requestPayloadObjectForLogging")
  private interface PropertyFilterMixIn {}
}
