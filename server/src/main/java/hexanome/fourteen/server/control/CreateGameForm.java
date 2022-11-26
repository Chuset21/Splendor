package hexanome.fourteen.server.control;

/**
 * Form needed to create a game.
 *
 * @param launchGameForm launch game form
 * @param gameid         game ID
 */
public record CreateGameForm(LaunchGameForm launchGameForm, String gameid) {
}
