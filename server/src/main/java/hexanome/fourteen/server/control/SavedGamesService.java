package hexanome.fourteen.server.control;

import hexanome.fourteen.server.model.board.GameBoard;

/**
 * Saved games service.
 */
public interface SavedGamesService {

  /**
   * Retrieve a saved game.
   *
   * @param saveGameid The save game id
   * @return The retrieved game board if successful, null otherwise.
   */
  GameBoard getGame(String saveGameid);

  /**
   * Stores a saved game.
   *
   * @param gameBoard Game board to save
   * @return The save game id of the newly stored game.
   */
  String putGame(GameBoard gameBoard);
}
