package hexanome.fourteen.server.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import hexanome.fourteen.server.model.board.Noble;
import hexanome.fourteen.server.model.board.card.Card;
import hexanome.fourteen.server.model.board.card.CardLevel;
import hexanome.fourteen.server.model.board.card.StandardCard;
import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;
import hexanome.fourteen.server.model.board.player.Player;
import hexanome.fourteen.server.model.board.tradingposts.TradingPostsEnum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(PER_CLASS)
public class TradingPostManagerTest {

  private static Player player;

  @BeforeAll
  public static void setUp() {
    player = new Player("test");
    Card redCard = new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD,
        GemColor.RED);
    Card blueCard = new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD,
        GemColor.BLUE);
    Card blackCard = new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD,
        GemColor.BLACK);
    Card greenCard = new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD,
        GemColor.GREEN);
    //hand begins with 2 red, 2 blue, 1 black, 4 green
    player.hand().purchasedCards().add(redCard);
    player.hand().purchasedCards().add(redCard);
    player.hand().purchasedCards().add(blueCard);
    player.hand().purchasedCards().add(blueCard);
    player.hand().purchasedCards().add(blackCard);
    player.hand().purchasedCards().add(greenCard);
    player.hand().purchasedCards().add(greenCard);
    player.hand().purchasedCards().add(greenCard);
    player.hand().purchasedCards().add(greenCard);
  }

  @Test
  public void testCheckTradingPost1() {
    assertFalse(player.hand().tradingPosts().get(TradingPostsEnum.BONUS_GEM_WITH_CARD));
    Card redCard = new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD,
        GemColor.RED);
    Card whiteCard = new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD,
        GemColor.WHITE);
    player.hand().purchasedCards().add(redCard);
    player.hand().purchasedCards().add(whiteCard);
    TradingPostManager.checkCardTradingPosts(player.hand());
    assertTrue(player.hand().tradingPosts().get(TradingPostsEnum.BONUS_GEM_WITH_CARD));
  }

  @Test
  public void testCheckTradingPost2() {
    assertFalse(player.hand().tradingPosts().get(TradingPostsEnum.BONUS_GEM_AFTER_TAKE_TWO));
    Card whiteCard = new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD,
        GemColor.WHITE);
    player.hand().purchasedCards().add(whiteCard);
    player.hand().purchasedCards().add(whiteCard);
    TradingPostManager.checkCardTradingPosts(player.hand());
    assertTrue(player.hand().tradingPosts().get(TradingPostsEnum.BONUS_GEM_AFTER_TAKE_TWO));
  }

  @Test
  public void testCheckTradingPost3() {
    assertFalse(player.hand().tradingPosts().get(TradingPostsEnum.FIVE_PRESETIGE_POINTS));
    Card blueCard = new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD,
        GemColor.RED);
    player.hand().purchasedCards().add(blueCard);
    TradingPostManager.checkCardTradingPosts(player.hand());
    assertTrue(player.hand().tradingPosts().get(TradingPostsEnum.FIVE_PRESETIGE_POINTS));
  }

  @Test
  public void testCheckTradingPost4WithCard() {
    player.hand().visitedNobles().add(new Noble());
    assertFalse(player.hand().tradingPosts().get(TradingPostsEnum.FIVE_PRESETIGE_POINTS));
    int prestigePoints = player.hand().prestigePoints();
    Card greenCard = new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD,
        GemColor.GREEN);
    player.hand().purchasedCards().add(greenCard);
    TradingPostManager.checkCardTradingPosts(player.hand());
    assertTrue(player.hand().tradingPosts().get(TradingPostsEnum.FIVE_PRESETIGE_POINTS));
    assertEquals(prestigePoints + 5, player.hand().prestigePoints());
  }

  @Test
  public void testCheckTradingPost4WithNoble() {
    Card greenCard = new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD,
        GemColor.GREEN);
    player.hand().purchasedCards().add(greenCard);player.hand().visitedNobles().add(new Noble());
    assertFalse(player.hand().tradingPosts().get(TradingPostsEnum.FIVE_PRESETIGE_POINTS));
    player.hand().visitedNobles().add(new Noble());
    int prestigePoints = player.hand().prestigePoints();
    TradingPostManager.checkNobleTradingPosts(player.hand());
    assertTrue(player.hand().tradingPosts().get(TradingPostsEnum.FIVE_PRESETIGE_POINTS));
    assertEquals(prestigePoints + 5, player.hand().prestigePoints());
  }

  @Test
  public void testCheckTradingPost5() {
    assertFalse(player.hand().tradingPosts().get(TradingPostsEnum.ONE_POINT_PER_POWER));
    Card blackCard = new StandardCard(1, new Gems(), CardLevel.ONE, Expansion.STANDARD,
        GemColor.BLACK);
    player.hand().purchasedCards().add(blackCard);
    player.hand().purchasedCards().add(blackCard);
    int prestigePoints = player.hand().prestigePoints();
    TradingPostManager.checkCardTradingPosts(player.hand());
    assertTrue(player.hand().tradingPosts().get(TradingPostsEnum.ONE_POINT_PER_POWER));
    assertEquals(prestigePoints + 1, player.hand().prestigePoints());
  }
}
