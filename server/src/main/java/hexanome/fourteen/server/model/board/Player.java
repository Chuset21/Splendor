package hexanome.fourteen.server.model.board;

/**
 * Player.
 *
 * @param userID the player's user ID
 * @param hand   the player's hand
 */
public record Player(String userID, Hand hand) {
}
