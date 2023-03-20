package hexanome.fourteen.server.control;

import hexanome.fourteen.server.model.board.Hand;
import hexanome.fourteen.server.model.board.gem.GemColor;
import hexanome.fourteen.server.model.board.gem.Gems;
import hexanome.fourteen.server.model.board.tradingposts.TradingPosts;
import hexanome.fourteen.server.model.board.tradingposts.TradingPostsEnum;

/**
 * A class to check if a player acquires any new trading post powers after purchasing a card or
 * claiming a noble.
 */
public class TradingPostManager {

  /**
   * Private no args constructor to stop instantiation.
   */
  private TradingPostManager() {

  }

  /**
   * This method will call all trading posts that can be acquired after purchasing a card.
   *
   * @param hand the players hand to check.
   */
  public static void checkCardTradingPosts(Hand hand) {
    final int amountOfTradingPostsBefore =
        hand.tradingPosts().values().stream().mapToInt(b -> b ? 1 : 0).sum();
    checkTradingPost1(hand);
    checkTradingPost2(hand);
    checkTradingPost3(hand);
    if (!hand.tradingPosts().getOrDefault(TradingPostsEnum.FIVE_PRESTIGE_POINTS, false)) {
      checkTradingPost4(hand);
    }
    checkTradingPost5(hand, amountOfTradingPostsBefore);
  }

  /**
   * This method will call trading post methods to check if they are lost when cards have been lost
   * due to the loss of cards from the purchase of a sacrifice card.
   *
   * @param hand the players hand to check.
   */
  public static void checkLoseCardTradingPosts(Hand hand) {
    final int amountOfTradingPostsBefore =
        hand.tradingPosts().values().stream().mapToInt(b -> b ? 1 : 0).sum();
    if (hand.tradingPosts().getOrDefault(TradingPostsEnum.BONUS_GEM_WITH_CARD, false)) {
      checkLoseTradingPost1(hand);
    }
    if (hand.tradingPosts().getOrDefault(TradingPostsEnum.BONUS_GEM_AFTER_TAKE_TWO, false)) {
      checkLoseTradingPost2(hand);
    }
    if (hand.tradingPosts().getOrDefault(TradingPostsEnum.DOUBLE_GOLD_GEMS, false)) {
      checkLoseTradingPost3(hand);
    }
    if (hand.tradingPosts().getOrDefault(TradingPostsEnum.FIVE_PRESTIGE_POINTS, false)) {
      checkLoseTradingPost4(hand);
    }
    if (hand.tradingPosts().getOrDefault(TradingPostsEnum.ONE_POINT_PER_POWER, false)) {
      checkLoseTradingPost5(hand, amountOfTradingPostsBefore);
    }
  }

  /**
   * This method will call all trading posts that can be acquired after claiming a noble.
   *
   * @param hand the players hand to check.
   */
  public static void checkNobleTradingPosts(Hand hand) {
    final int amountOfTradingPostsBefore =
        hand.tradingPosts().values().stream().mapToInt(b -> b ? 1 : 0).sum();
    checkTradingPost4(hand);
    checkTradingPost5(hand, amountOfTradingPostsBefore);
  }

  /**
   * Check the trading post that allows you to take a gem after purchasing a card. Requires that a
   * player has 3 red gem discounts and one white gem discount.
   *
   * @param hand the players hand to check.
   */
  private static void checkTradingPost1(Hand hand) {
    Gems gemsNeeded = new Gems();
    gemsNeeded.put(GemColor.RED, 3);
    gemsNeeded.put(GemColor.WHITE, 1);
    if (hand.gemDiscounts().hasEnoughGems(gemsNeeded)) {
      TradingPosts tp = hand.tradingPosts();
      tp.put(TradingPostsEnum.BONUS_GEM_WITH_CARD, true);
      hand.setTradingPosts(tp);
    }
  }

  /**
   * Check if a player loses trading post 1 when losing cards from the purchase of a sacrifice
   * card.
   *
   * @param hand the players hand to check.
   */
  private static void checkLoseTradingPost1(Hand hand) {
    Gems gemsNeeded = new Gems();
    gemsNeeded.put(GemColor.RED, 3);
    gemsNeeded.put(GemColor.WHITE, 1);
    if (!hand.gemDiscounts().hasEnoughGems(gemsNeeded)) {
      TradingPosts tp = hand.tradingPosts();
      tp.put(TradingPostsEnum.BONUS_GEM_WITH_CARD, false);
      hand.setTradingPosts(tp);
    }
  }

  /**
   * Check the trading post that allows you to take a gem of a different color after taking two gems
   * of the same color. Requires that a player has 2 white gem discounts.
   *
   * @param hand the players hand to check.
   */
  private static void checkTradingPost2(Hand hand) {
    Gems gemsNeeded = new Gems();
    gemsNeeded.put(GemColor.WHITE, 2);
    if (hand.gemDiscounts().hasEnoughGems(gemsNeeded)) {
      TradingPosts tp = hand.tradingPosts();
      tp.put(TradingPostsEnum.BONUS_GEM_AFTER_TAKE_TWO, true);
      hand.setTradingPosts(tp);
    }
  }

  /**
   * Check if a player loses trading post 2 when losing cards from the purchase of a sacrifice
   * card.
   *
   * @param hand the players hand to check.
   */
  private static void checkLoseTradingPost2(Hand hand) {
    Gems gemsNeeded = new Gems();
    gemsNeeded.put(GemColor.WHITE, 2);
    if (!hand.gemDiscounts().hasEnoughGems(gemsNeeded)) {
      TradingPosts tp = hand.tradingPosts();
      tp.put(TradingPostsEnum.BONUS_GEM_AFTER_TAKE_TWO, false);
      hand.setTradingPosts(tp);
    }
  }

  /**
   * Check the trading post that changes each of your gold gems to count as two gems of the same
   * color. Requires 3 blue gem discounts and 1 black gem discount.
   *
   * @param hand the players hand to check.
   */
  private static void checkTradingPost3(Hand hand) {
    Gems gemsNeeded = new Gems();
    gemsNeeded.put(GemColor.BLUE, 3);
    gemsNeeded.put(GemColor.BLACK, 1);
    if (hand.gemDiscounts().hasEnoughGems(gemsNeeded)) {
      TradingPosts tp = hand.tradingPosts();
      tp.put(TradingPostsEnum.DOUBLE_GOLD_GEMS, true);
      hand.setTradingPosts(tp);
    }
  }

  /**
   * Check if a player loses trading post 3 when losing cards from the purchase of a sacrifice
   * card.
   *
   * @param hand the players hand to check.
   */
  private static void checkLoseTradingPost3(Hand hand) {
    Gems gemsNeeded = new Gems();
    gemsNeeded.put(GemColor.BLUE, 3);
    gemsNeeded.put(GemColor.BLACK, 1);
    if (!hand.gemDiscounts().hasEnoughGems(gemsNeeded)) {
      TradingPosts tp = hand.tradingPosts();
      tp.put(TradingPostsEnum.DOUBLE_GOLD_GEMS, false);
      hand.setTradingPosts(tp);
    }
  }

  /**
   * Check the trading posts that grants you 5 prestige points. Requires 5 green gem discounts and
   * at least one noble.
   *
   * @param hand the players hand to check.
   */
  private static void checkTradingPost4(Hand hand) {
    Gems gemsNeeded = new Gems();
    gemsNeeded.put(GemColor.GREEN, 5);
    if (hand.gemDiscounts().hasEnoughGems(gemsNeeded) && hand.visitedNobles().size() >= 1) {
      TradingPosts tp = hand.tradingPosts();
      tp.put(TradingPostsEnum.FIVE_PRESTIGE_POINTS, true);
      hand.setTradingPosts(tp);
      hand.incrementPrestigePoints(5);
    }
  }

  /**
   * Check if a player loses trading post 4 when losing cards from the purchase of a sacrifice
   * card.
   *
   * @param hand the players hand to check.
   */
  private static void checkLoseTradingPost4(Hand hand) {
    Gems gemsNeeded = new Gems();
    gemsNeeded.put(GemColor.GREEN, 5);
    if (!hand.gemDiscounts().hasEnoughGems(gemsNeeded)) {
      TradingPosts tp = hand.tradingPosts();
      tp.put(TradingPostsEnum.FIVE_PRESTIGE_POINTS, false);
      hand.setTradingPosts(tp);
      hand.decrementPrestigePoints(5);
    }
  }

  /**
   * Check the trading posts that grants you 1 prestige point per trading post power you have,
   * including this one. Requires 3 black gem discounts.
   *
   * @param hand the players hand to check.
   */
  private static void checkTradingPost5(Hand hand, int amountOfTradingPostsBefore) {
    Gems gemsNeeded = new Gems();
    gemsNeeded.put(GemColor.BLACK, 3);
    if (hand.gemDiscounts().hasEnoughGems(gemsNeeded)) {
      TradingPosts tp = hand.tradingPosts();
      final Boolean before = tp.put(TradingPostsEnum.ONE_POINT_PER_POWER, true);
      hand.setTradingPosts(tp);
      final int numPosts = hand.tradingPosts().values().stream().mapToInt(b -> b ? 1 : 0).sum();
      // Decrement if this post was already owned
      if (before != null && before) {
        hand.decrementPrestigePoints(amountOfTradingPostsBefore);
      }
      hand.incrementPrestigePoints(numPosts);
    }
  }

  /**
   * Check if a player loses trading post 5 when losing cards from the purchase of a sacrifice
   * card.
   *
   * @param hand the players hand to check.
   */
  private static void checkLoseTradingPost5(Hand hand, int amountOfTradingPostsBefore) {
    TradingPosts tp = hand.tradingPosts();
    final boolean before = tp.getOrDefault(TradingPostsEnum.ONE_POINT_PER_POWER, false);
    Gems gemsNeeded = new Gems();
    gemsNeeded.put(GemColor.BLACK, 3);
    if (!hand.gemDiscounts().hasEnoughGems(gemsNeeded)) {
      if (before) {
        hand.decrementPrestigePoints(amountOfTradingPostsBefore);
      }
      tp.put(TradingPostsEnum.ONE_POINT_PER_POWER, false);
      hand.setTradingPosts(tp);
    } else if (before) {
      final int numPosts =
          hand.tradingPosts().values().stream().mapToInt(b -> b ? 1 : 0).sum();
      hand.decrementPrestigePoints(amountOfTradingPostsBefore - numPosts);
    }
  }
}
