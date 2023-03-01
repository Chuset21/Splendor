package hexanome.fourteen.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class powers up Spring and ensures the annotated controllers are detected.
 */
@SpringBootApplication
public class RestLauncher {
  public static String gsLocation;

  /**
   * Constructor.
   */
  public RestLauncher() {

  }

  /**
   * Main method to allow our Application to run.
   *
   * @param args Arguments to be passed
   */
  public static void main(String[] args) {
    gsLocation = (args.length > 0) ? args[0] : null;
    SpringApplication.run(RestLauncher.class, args);
  }
}
