package com.hexanome.fourteen.form.server;

import com.hexanome.fourteen.boards.Expansion;
import com.hexanome.fourteen.form.server.cardform.CardForm;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Game Board form.
 */
public final class GameBoardForm {
  private String playerTurnid;
  private Set<NobleForm> availableNobles;
  private Set<CityForm> availableCities;
  private GemsForm availableGems;
  private Set<List<CardForm>> cards;
  private Set<Expansion> expansions;
  private PlayerForm leadingPlayer;
  private Set<PlayerForm> players;
  private String gameid;
  private String creator;
  private boolean isLastRound;
  private boolean isGameOver;

  /**
   * Constructor.
   *
   * @param playerTurnid    The player's user ID whose turn it is.
   * @param availableNobles The available nobles.
   * @param availableCities The available cities.
   * @param availableGems   The gems in the bank.
   * @param cards           The cards that are face up.
   * @param expansions      The expansions for the game.
   * @param leadingPlayer   The current leading player.
   * @param players         The players.
   * @param gameid          The game ID.
   * @param creator         The creator.
   * @param isLastRound     Is it the last round of the game.
   * @param isGameOver      Is the game over.
   */
  public GameBoardForm(String playerTurnid, Set<NobleForm> availableNobles,
                       Set<CityForm> availableCities, GemsForm availableGems,
                       Set<List<CardForm>> cards, Set<Expansion> expansions,
                       PlayerForm leadingPlayer,
                       Set<PlayerForm> players, String gameid, String creator, boolean isLastRound,
                       boolean isGameOver) {
    this.playerTurnid = playerTurnid;
    this.availableNobles = availableNobles;
    this.availableCities = availableCities;
    this.availableGems = availableGems;
    this.cards = cards;
    this.expansions = expansions;
    this.leadingPlayer = leadingPlayer;
    this.players = players;
    this.gameid = gameid;
    this.creator = creator;
    this.isLastRound = isLastRound;
    this.isGameOver = isGameOver;
  }

  /**
   * No args constructor.
   */
  public GameBoardForm() {

  }

  public String playerTurnid() {
    return playerTurnid;
  }

  public Set<NobleForm> availableNobles() {
    return availableNobles;
  }

  public Set<CityForm> availableCities() {
    return availableCities;
  }

  public GemsForm availableGems() {
    return availableGems;
  }

  public Set<List<CardForm>> cards() {
    return cards;
  }

  public Set<Expansion> expansions() {
    return expansions;
  }

  public PlayerForm leadingPlayer() {
    return leadingPlayer;
  }

  public Set<PlayerForm> players() {
    return players;
  }

  public String gameid() {
    return gameid;
  }

  public String creator() {
    return creator;
  }

  public boolean isLastRound() {
    return isLastRound;
  }

  public boolean isGameOver() {
    return isGameOver;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    GameBoardForm that = (GameBoardForm) obj;
    return Objects.equals(this.playerTurnid, that.playerTurnid)
           && Objects.equals(this.availableNobles, that.availableNobles)
           && Objects.equals(this.availableCities, that.availableCities)
           && Objects.equals(this.availableGems, that.availableGems)
           && Objects.equals(this.cards, that.cards)
           && Objects.equals(this.expansions, that.expansions)
           && Objects.equals(this.leadingPlayer, that.leadingPlayer)
           && Objects.equals(this.players, that.players)
           && Objects.equals(this.gameid, that.gameid)
           && Objects.equals(this.creator, that.creator)
           && this.isLastRound == that.isLastRound
           && this.isGameOver == that.isGameOver;
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerTurnid, availableNobles, availableCities, availableGems, cards,
        expansions, leadingPlayer, players, gameid, creator, isLastRound, isGameOver);
  }

  @Override
  public String toString() {
    return "GameBoardForm[" +
           "playerTurnid=" + playerTurnid + ", " +
           "availableNobles=" + availableNobles + ", " +
           "availableCities=" + availableCities + ", " +
           "availableGems=" + availableGems + ", " +
           "cards=" + cards + ", " +
           "expansions=" + expansions + ", " +
           "leadingPlayer=" + leadingPlayer + ", " +
           "players=" + players + ", " +
           "gameid=" + gameid + ", " +
           "creator=" + creator + ", " +
           "isLastRound=" + isLastRound + ", " +
           "isGameOver=" + isGameOver + ']';
  }
}