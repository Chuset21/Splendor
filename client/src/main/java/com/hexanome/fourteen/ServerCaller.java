package com.hexanome.fourteen;

import com.hexanome.fourteen.form.server.ClaimNobleForm;
import com.hexanome.fourteen.form.server.GameBoardForm;
import com.hexanome.fourteen.form.server.PurchaseCardForm;
import com.hexanome.fourteen.form.server.ReserveCardForm;
import com.hexanome.fourteen.form.server.TakeGemsForm;
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

    if (response.getStatus() != 200) {
      return null;
    }

    return Main.GSON.fromJson(response.getBody(), GameBoardForm.class);
  }

  /**
   * Purchase a card.
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
   * Take gems.
   *
   * @return The response.
   */
  public static HttpResponse<String> takeGems(String serverLocation, String gameid,
                                                 String accessToken,
                                                 TakeGemsForm takeGemsForm) {
    return Unirest.put("%s/api/games/%s/gems".formatted(serverLocation, gameid))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", accessToken).body(Main.GSON.toJson(takeGemsForm))
        .asString();
  }

  /**
   * Claim a noble.
   *
   * @return The response.
   */
  public static HttpResponse<String> claimNoble(String serverLocation, String gameid,
                                              String accessToken,
                                              ClaimNobleForm claimNobleForm) {
    return Unirest.put("%s/api/games/%s/noble".formatted(serverLocation, gameid))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", accessToken).body(Main.GSON.toJson(claimNobleForm))
        .asString();
  }
}
