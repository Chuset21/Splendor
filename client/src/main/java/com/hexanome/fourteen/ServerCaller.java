package com.hexanome.fourteen;

import com.hexanome.fourteen.form.server.GameBoardForm;
import com.hexanome.fourteen.form.server.PurchaseCardForm;
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
  public static GameBoardForm getGameBoard(String serverLocation, String accessToken) {
    HttpResponse<String> response = Unirest.get("%s/api/games/board".formatted(serverLocation))
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
   * @return The game board form if successful, null otherwise.
   */
  public static HttpResponse<String> purchaseCard(String serverLocation, String accessToken,
                                                  PurchaseCardForm purchaseCardForm) {
    return Unirest.post("%s/api/games/board".formatted(serverLocation))
        .header("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
        .queryString("access_token", accessToken).body(Main.GSON.toJson(purchaseCardForm))
        .asString();
  }
}
