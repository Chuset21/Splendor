package com.hexanome.fourteen;

import com.hexanome.fourteen.form.server.ClaimCityForm;
import com.hexanome.fourteen.form.server.ClaimNobleForm;
import com.hexanome.fourteen.form.server.GameBoardForm;
import com.hexanome.fourteen.form.server.PurchaseCardForm;
import com.hexanome.fourteen.form.server.ReserveCardForm;
import com.hexanome.fourteen.form.server.TakeGemsForm;
import com.hexanome.fourteen.lobbyui.Lobby;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

/**
 * Class containing calls to game server.
 */
public final class ServerCaller {
  /**
   * Private constructor to stop instantiation.
   */
  private ServerCaller() {
  }

  /**
   * Get game board.
   *
   * @return The game board form if successful, null otherwise.
   */
  public static GameBoardForm getGameBoard(String serverLocation, String gameid,
                                           String accessToken) {
    HttpResponse<String> response = Unirest.get("%s/api/games/%s".formatted(serverLocation, gameid))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", accessToken).asString();

    if (!response.isSuccess()) {
      return null;
    }

    return Main.GSON.fromJson(response.getBody(), GameBoardForm.class);
  }

  /**
   * Overloaded get game board, uses a lobby object to get all session info.
   *
   * @param lobby lobby to get info from
   * @return The game board form if successful, null otherwise.
   */
  public static HttpResponse<String> getGameBoard(Lobby lobby) {
    return Unirest.get(
            "%s/api/games/%s".formatted(lobby.getGameServiceLocation(), lobby.getSessionid()))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", LobbyServiceCaller.getCurrentUserAccessToken()).asString();
  }

  /**
   * Overloaded get game board, uses a lobby object to get all session info.
   *
   * @param lobby lobby to get info from
   * @return The game board form if successful, null otherwise.
   */
  public static HttpResponse<String> getGameBoard(Lobby lobby, String hash) {
    return Unirest.get(
            "%s/api/games/%s".formatted(lobby.getGameServiceLocation(), lobby.getSessionid()))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", LobbyServiceCaller.getCurrentUserAccessToken())
        .queryString("hash", hash == null || hash.isEmpty() ? "" : hash).asString();
  }

  /**
   * Purchase a card.
   * Returns null as the string if successful,
   * however if a noble can be reserved, it returns the list of nobles that can be reserved
   *
   * @return The response.
   */
  public static HttpResponse<String> purchaseCard(String serverLocation, String gameid,
                                                  String accessToken,
                                                  PurchaseCardForm purchaseCardForm) {
    return Unirest.put("%s/api/games/%s/card/purchase".formatted(serverLocation, gameid))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", accessToken).body(Main.GSON.toJson(purchaseCardForm))
        .asString();
  }

  /**
   * Purchase a card.
   * Returns null as the string if successful,
   * however if a noble can be reserved, it returns the list of nobles that can be reserved
   *
   * @param lobby current user's lobby
   * @param accessToken access token of current user
   * @param purchaseCardForm form with details of purchase
   * @return The response.
   */
  public static HttpResponse<String> purchaseCard(Lobby lobby,
                                                  String accessToken,
                                                  PurchaseCardForm purchaseCardForm) {
    return Unirest.put("%s/api/games/%s/card/purchase".formatted(lobby.getGameServiceLocation(),
            lobby.getSessionid()))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", accessToken).body(Main.GSON.toJson(purchaseCardForm))
        .asString();
  }

  /**
   * Reserve a card.
   *
   * @return The response.
   */
  public static HttpResponse<String> reserveCard(String serverLocation, String gameid,
                                                 String accessToken,
                                                 ReserveCardForm reserveCardForm) {
    return Unirest.put("%s/api/games/%s/card/reserve".formatted(serverLocation, gameid))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", accessToken).body(Main.GSON.toJson(reserveCardForm))
        .asString();
  }

  /**
   * Reserve a card.
   *
   * @return The response.
   */
  public static HttpResponse<String> reserveCard(Lobby lobby,
                                                 String accessToken,
                                                 ReserveCardForm reserveCardForm) {
    return Unirest.put("%s/api/games/%s/card/reserve".formatted(lobby.getGameServiceLocation(),
            lobby.getSessionid()))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", accessToken).body(Main.GSON.toJson(reserveCardForm))
        .asString();
  }

  /**
   * Take gems.
   *
   * @return The response.
   */
  public static HttpResponse<String> takeGems(String serverLocation, String gameid,
                                              String accessToken, TakeGemsForm takeGemsForm) {
    return Unirest.put("%s/api/games/%s/gems".formatted(serverLocation, gameid))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", accessToken).body(Main.GSON.toJson(takeGemsForm)).asString();
  }

  /**
   * Take gems.
   *
   * @return The response.
   */
  public static HttpResponse<String> takeGems(Lobby lobby, String accessToken,
                                              TakeGemsForm takeGemsForm) {
    return Unirest.put(
            "%s/api/games/%s/gems".formatted(lobby.getGameServiceLocation(), lobby.getSessionid()))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", accessToken).body(Main.GSON.toJson(takeGemsForm)).asString();
  }

  /**
   * Claim a noble.
   *
   * @return The response.
   */
  public static HttpResponse<String> claimNoble(String serverLocation, String gameid,
                                                String accessToken, ClaimNobleForm claimNobleForm) {
    return Unirest.put("%s/api/games/%s/noble".formatted(serverLocation, gameid))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", accessToken).body(Main.GSON.toJson(claimNobleForm)).asString();
  }

  /**
   * Claim a noble.
   *
   * @return The response.
   */
  public static HttpResponse<String> claimNoble(Lobby lobby,
                                                String accessToken, ClaimNobleForm claimNobleForm) {
    return claimNoble(lobby.getGameServiceLocation(), lobby.getSessionid(), accessToken,
        claimNobleForm);
  }

  /**
   * Claim a city.
   *
   * @return The response.
   */
  public static HttpResponse<String> claimCity(String serverLocation, String gameid,
                                                String accessToken, ClaimCityForm claimCityForm) {
    return Unirest.put("%s/api/games/%s/city".formatted(serverLocation, gameid))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", accessToken).body(Main.GSON.toJson(claimCityForm)).asString();
  }

  /**
   * Claim a city.
   *
   * @return The response.
   */
  public static HttpResponse<String> claimCity(Lobby lobby,
                                                String accessToken, ClaimCityForm claimCityForm) {
    return claimCity(lobby.getGameServiceLocation(), lobby.getSessionid(), accessToken,
        claimCityForm);
  }

  /**
   * Save a game.
   *
   * @return The response.
   */
  public static HttpResponse<String> saveGame(String serverLocation, String gameid,
                                              String accessToken) {
    return Unirest.post("%s/api/games/%s".formatted(serverLocation, gameid))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", accessToken).asString();
  }

  /**
   * Save a game.
   *
   * @return The response.
   */
  public static HttpResponse<String> saveGame(Lobby lobby, String accessToken) {
    return saveGame(lobby.getGameServiceLocation(), lobby.getSessionid(), accessToken);
  }
}
