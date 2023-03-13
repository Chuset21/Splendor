package hexanome.fourteen.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * Request logging filter config class to configure the logging filter.
 */
@Configuration
public class RequestLoggingFilterConfig {

  /**
   * Default no args constructor.
   */
  public RequestLoggingFilterConfig() {

  }

  /**
   * Configure the log filter.
   *
   * @return The CommonsRequestLoggingFilter
   */
  @Bean
  public CommonsRequestLoggingFilter logFilter() {
    final CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
    filter.setIncludeQueryString(true);
    filter.setIncludePayload(true);
    filter.setMaxPayloadLength(10000);
    filter.setIncludeHeaders(false);
    return filter;
  }
}