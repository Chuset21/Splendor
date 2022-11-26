package hexanome.fourteen.server.control;

/**
 * Form needed to create a game.
 *
 * @param putGameForm launch game form
 * @param gameid         game ID
 */
public record CreateGameForm(PutGameForm putGameForm, String gameid) {
}
