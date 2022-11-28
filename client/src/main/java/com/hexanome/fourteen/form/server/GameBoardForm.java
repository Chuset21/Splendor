package com.hexanome.fourteen.form.server;

import com.hexanome.fourteen.boards.Expansion;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Game Board form.
 */
public final class GameBoardForm {
  private String playerTurnid;
  private Set<NobleForm> availableNobles;
  private GemsForm availableGems;
  private Set<List<CardForm>> cards;
  private Set<Expansion> expansions;
  private PlayerForm leadingPlayer;
  private Set<PlayerForm> players;
  private String gameid;
  private String creator;

  /**
   * Constructor.
   *
   * @param playerTurnid    The player's user ID whose turn it is.
   * @param availableNobles The available nobles.
   * @param availableGems   The gems in the bank.
   * @param cards           The cards that are face up.
   * @param expansions      The expansions for the game.
   * @param leadingPlayer   The current leading player.
   * @param players         The players.
   * @param gameid          The game ID.
   * @param creator         The creator.
   */
  public GameBoardForm(String playerTurnid, Set<NobleForm> availableNobles, GemsForm availableGems,
                       Set<List<CardForm>> cards, Set<Expansion> expansions,
                       PlayerForm leadingPlayer, Set<PlayerForm> players, String gameid,
                       String creator) {
    this.playerTurnid = playerTurnid;
    this.availableNobles = availableNobles;
    this.availableGems = availableGems;
    this.cards = cards;
    this.expansions = expansions;
    this.leadingPlayer = leadingPlayer;
    this.players = players;
    this.gameid = gameid;
    this.creator = creator;
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
}
