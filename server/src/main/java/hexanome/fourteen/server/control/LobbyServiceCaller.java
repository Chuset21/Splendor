package hexanome.fourteen.server.control;

import kong.unirest.HttpResponse;

/**
 * Lobby service caller.
 */
public interface LobbyServiceCaller {
  HttpResponse<String> login(String username, String password);

  void createUser(String username, String password, String accessToken);

  void logout(String accessToken);

  String getGameServices();

  boolean registerGameService(String gameServiceName, String accessToken);

  HttpResponse<String> refreshToken(String refreshToken);

  void unregisterGameService(String gameServiceName, String accessToken);

  String getUsername(String accessToken);
}
