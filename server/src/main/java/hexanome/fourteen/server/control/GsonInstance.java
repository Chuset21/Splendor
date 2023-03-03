package hexanome.fourteen.server.control;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import hexanome.fourteen.server.control.form.payment.CardPayment;
import hexanome.fourteen.server.control.form.payment.GemPayment;
import hexanome.fourteen.server.control.form.payment.Payment;
import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.card.DoubleBonusCard;
import hexanome.fourteen.server.model.board.card.GoldGemCard;
import hexanome.fourteen.server.model.board.card.ReserveNobleCard;
import hexanome.fourteen.server.model.board.card.SacrificeCard;
import hexanome.fourteen.server.model.board.card.SatchelCard;
import hexanome.fourteen.server.model.board.card.StandardCard;
import hexanome.fourteen.server.model.board.card.WaterfallCard;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * Component holding a common gson instance.
 */
@Component
public class GsonInstance {

  /**
   * GSON Object.
   */
  public Gson gson;

  /**
   * GSON Constructor.
   */
  public GsonInstance() {
  }

  /**
   * Initialize gson.
   */
  @PostConstruct
  public void initGson() {
    final RuntimeTypeAdapterFactory<Card> adapter =
        RuntimeTypeAdapterFactory.of(Card.class).registerSubtype(GoldGemCard.class)
            .registerSubtype(ReserveNobleCard.class).registerSubtype(SacrificeCard.class)
            .registerSubtype(SatchelCard.class).registerSubtype(StandardCard.class)
            .registerSubtype(WaterfallCard.class).registerSubtype(DoubleBonusCard.class);
    gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .registerTypeAdapterFactory(adapter).registerTypeAdapterFactory(
            RuntimeTypeAdapterFactory.of(Payment.class).registerSubtype(CardPayment.class)
                .registerSubtype(GemPayment.class)).serializeNulls().create();
  }
}