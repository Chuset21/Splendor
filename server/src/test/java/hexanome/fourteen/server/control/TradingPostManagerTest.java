package hexanome.fourteen.server.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import hexanome.fourteen.server.model.board.Noble;
import hexanome.fourteen.server.model.board.expansion.Expansion;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;
import hexanome.fourteen.server.model.board.player.Player;
import hexanome.fourteen.server.model.board.tradingposts.TradingPostsEnum;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(PER_CLASS)
public class TradingPostManagerTest {

  private static Player player;

  @BeforeEach
  public void setUp() {
    player =
        new Player("test", Set.of(Expansion.TRADING_POSTS, Expansion.STANDARD, Expansion.ORIENT));
    Gems gemDiscounts = new Gems();
    gemDiscounts.put(GemColor.RED, 2);
    gemDiscounts.put(GemColor.BLUE, 2);
    gemDiscounts.put(GemColor.BLACK, 1);
    gemDiscounts.put(GemColor.GREEN, 4);
    gemDiscounts.put(GemColor.WHITE, 0);
    player.hand().setGemDiscounts(gemDiscounts);
  }

  @Test
  public void testCheckTradingPost1() {
    assertFalse(player.hand().tradingPosts().get(TradingPostsEnum.BONUS_GEM_WITH_CARD));
    player.hand().gemDiscounts().replace(GemColor.RED, 3);
    player.hand().gemDiscounts().replace(GemColor.WHITE, 1);
    TradingPostManager.checkCardTradingPosts(player.hand());
    assertTrue(player.hand().tradingPosts().get(TradingPostsEnum.BONUS_GEM_WITH_CARD));
  }

  @Test
  public void testCheckLoseTradingPost1() {
    player.hand().tradingPosts().replace(TradingPostsEnum.BONUS_GEM_WITH_CARD, true);
    TradingPostManager.checkLoseCardTradingPosts(player.hand());
    assertFalse(player.hand().tradingPosts().get(TradingPostsEnum.BONUS_GEM_WITH_CARD));
  }

  @Test
  public void testCheckTradingPost2() {
    TradingPostManager.checkCardTradingPosts(player.hand());
    assertFalse(player.hand().tradingPosts().get(TradingPostsEnum.BONUS_GEM_AFTER_TAKE_TWO));
    player.hand().gemDiscounts().replace(GemColor.WHITE, 2);
    TradingPostManager.checkCardTradingPosts(player.hand());
    assertTrue(player.hand().tradingPosts().get(TradingPostsEnum.BONUS_GEM_AFTER_TAKE_TWO));
  }

  @Test
  public void testCheckLoseTradingPost2() {
    player.hand().tradingPosts().replace(TradingPostsEnum.BONUS_GEM_AFTER_TAKE_TWO, true);
    TradingPostManager.checkLoseCardTradingPosts(player.hand());
    assertFalse(player.hand().tradingPosts().get(TradingPostsEnum.BONUS_GEM_AFTER_TAKE_TWO));
  }

  @Test
  public void testCheckTradingPost3() {
    assertFalse(player.hand().tradingPosts().get(TradingPostsEnum.DOUBLE_GOLD_GEMS));
    player.hand().gemDiscounts().replace(GemColor.BLUE, 3);
    TradingPostManager.checkCardTradingPosts(player.hand());
    assertTrue(player.hand().tradingPosts().get(TradingPostsEnum.DOUBLE_GOLD_GEMS));
  }

  @Test
  public void testCheckLoseTradingPost3() {
    player.hand().tradingPosts().replace(TradingPostsEnum.DOUBLE_GOLD_GEMS, true);
    TradingPostManager.checkLoseCardTradingPosts(player.hand());
    assertFalse(player.hand().tradingPosts().get(TradingPostsEnum.DOUBLE_GOLD_GEMS));
  }

  @Test
  public void testCheckTradingPost4WithCard() {
    player.hand().visitedNobles().add(new Noble());
    assertFalse(player.hand().tradingPosts().get(TradingPostsEnum.FIVE_PRESTIGE_POINTS));
    int prestigePoints = player.hand().prestigePoints();
    player.hand().gemDiscounts().replace(GemColor.GREEN, 5);
    TradingPostManager.checkCardTradingPosts(player.hand());
    assertTrue(player.hand().tradingPosts().get(TradingPostsEnum.FIVE_PRESTIGE_POINTS));
    assertEquals(prestigePoints + 5, player.hand().prestigePoints());
  }

  @Test
  public void testCheckLoseTradingPost4() {
    player.hand().setPrestigePoints(5);
    player.hand().tradingPosts().replace(TradingPostsEnum.FIVE_PRESTIGE_POINTS, true);
    TradingPostManager.checkLoseCardTradingPosts(player.hand());
    assertFalse(player.hand().tradingPosts().get(TradingPostsEnum.FIVE_PRESTIGE_POINTS));
    assertEquals(0, player.hand().prestigePoints());
  }

  @Test
  public void testCheckTradingPost4WithNoble() {
    player.hand().gemDiscounts().put(GemColor.GREEN, 5);
    assertFalse(player.hand().tradingPosts().get(TradingPostsEnum.FIVE_PRESTIGE_POINTS));
    player.hand().visitedNobles().add(new Noble());
    int prestigePoints = player.hand().prestigePoints();
    TradingPostManager.checkNobleTradingPosts(player.hand());
    assertTrue(player.hand().tradingPosts().get(TradingPostsEnum.FIVE_PRESTIGE_POINTS));
    assertEquals(prestigePoints + 5, player.hand().prestigePoints());
  }

  @Test
  public void testCheckTradingPost5() {
    player.hand().gemDiscounts().put(GemColor.BLACK, 3);
    int prestigePoints = player.hand().prestigePoints();
    TradingPostManager.checkCardTradingPosts(player.hand());
    assertTrue(player.hand().tradingPosts().get(TradingPostsEnum.ONE_POINT_PER_POWER));
    int bonusPoints = player.hand().tradingPosts().values().stream().mapToInt(b -> b ? 1 : 0).sum();
    assertEquals(prestigePoints + bonusPoints, player.hand().prestigePoints());

    player.hand().visitedNobles().add(new Noble());
    player.hand().gemDiscounts().put(GemColor.GREEN, 5);
    TradingPostManager.checkNobleTradingPosts(player.hand());
    TradingPostManager.checkCardTradingPosts(player.hand());

    bonusPoints = player.hand().tradingPosts().values().stream().mapToInt(b -> b ? 1 : 0).sum();
    assertEquals(prestigePoints + 5 + bonusPoints, player.hand().prestigePoints());
  }

  @Test
  public void testCheckLoseTradingPost5() {
    player.hand().setPrestigePoints(1);
    player.hand().tradingPosts().replace(TradingPostsEnum.ONE_POINT_PER_POWER, true);
    TradingPostManager.checkLoseCardTradingPosts(player.hand());
    assertFalse(player.hand().tradingPosts().get(TradingPostsEnum.ONE_POINT_PER_POWER));
    assertEquals(0, player.hand().prestigePoints());

    player.hand().visitedNobles().add(new Noble());
    player.hand().gemDiscounts().put(GemColor.GREEN, 5);
    TradingPostManager.checkNobleTradingPosts(player.hand());
    assertTrue(player.hand().tradingPosts().get(TradingPostsEnum.FIVE_PRESTIGE_POINTS));
    player.hand().gemDiscounts().put(GemColor.BLACK, 3);
    TradingPostManager.checkCardTradingPosts(player.hand());
    assertTrue(player.hand().tradingPosts().get(TradingPostsEnum.ONE_POINT_PER_POWER));
    assertEquals(7, player.hand().prestigePoints());
    player.hand().gemDiscounts().clear();
    player.hand().gemDiscounts().put(GemColor.BLACK, 3);
    TradingPostManager.checkLoseCardTradingPosts(player.hand());
    assertTrue(player.hand().tradingPosts().get(TradingPostsEnum.ONE_POINT_PER_POWER));
    assertEquals(1, player.hand().prestigePoints());
  }
}
