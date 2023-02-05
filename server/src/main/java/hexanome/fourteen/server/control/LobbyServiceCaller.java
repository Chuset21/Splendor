package hexanome.fourteen.server.control;

import hexanome.fourteen.server.control.form.SaveGameForm;
import kong.unirest.HttpResponse;

/**
 * Lobby service caller.
 */
public interface LobbyServiceCaller {

  /**
   * Login a User.
   *
   * @param username The Username
   * @param password The Password
   * @return HTTP Response
   */
  HttpResponse<String> login(String username, String password);

  /**
   * Creates a User.
   *
   * @param username    The Name of the User
   * @param password    The Password of the User
   * @param accessToken The Access Token
   */
  void createUser(String username, String password, String accessToken);

  /**
   * Logout a User.
   *
   * @param accessToken The Access Token
   */
  void logout(String accessToken);

  /**
   * Gets all Game Services.
   *
   * @return The Game Services
   */
  String getGameServices();

  /**
   * Registers a Game Service to LobbyService.
   *
   * @param gameServiceName The Name of the Game Service
   * @param accessToken     The Access Token
   * @return True/False for registration being confirmed
   */
  boolean registerGameService(String gameServiceName, String accessToken);

  /**
   * Refreshes Token.
   *
   * @param refreshToken The Refresh Token
   * @return HTTP Response
   */
  HttpResponse<String> refreshToken(String refreshToken);

  /**
   * Unregisters a Game Service from LobbyService.
   *
   * @param gameServiceName The Name of the GameService
   * @param accessToken     The Access Token
   */
  void unregisterGameService(String gameServiceName, String accessToken);

  /**
   * Gets the Username.
   *
   * @param accessToken The Access Token
   * @return The Username
   */
  String getUsername(String accessToken);

  /**
   * Save a game.
   *
   * @param accessToken  The Access Token.
   * @param saveGameForm The save game form.
   */
  void saveGame(String accessToken, SaveGameForm saveGameForm);
}
