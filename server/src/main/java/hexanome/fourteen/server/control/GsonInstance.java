package hexanome.fourteen.server.control;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * Component holding a common gson instance.
 */
@Component
public class GsonInstance {

  public Gson gson;

  public GsonInstance() {
  }

  @PostConstruct
  private void initGson() {
    gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();
  }
}
