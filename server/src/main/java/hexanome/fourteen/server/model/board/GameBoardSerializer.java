package hexanome.fourteen.server.model.board;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import hexanome.fourteen.server.Mapper;
import hexanome.fourteen.server.control.GsonInstance;
import hexanome.fourteen.server.model.clientmapper.ServerToClientBoardGameMapper;
import hexanome.fourteen.server.model.sent.SentGameBoard;
import java.io.IOException;

/**
 * Custom game board serializer.
 */
public final class GameBoardSerializer extends JsonSerializer<GameBoard> {

  private final GsonInstance gsonInstance;

  private final Mapper<GameBoard, SentGameBoard> gameBoardMapper;

  /**
   * No args constructor.
   */
  public GameBoardSerializer() {
    this.gsonInstance = new GsonInstance();
    gsonInstance.initGson();
    this.gameBoardMapper = new ServerToClientBoardGameMapper();
  }

  @Override
  public void serialize(
      GameBoard gameBoard, JsonGenerator jsonGenerator, SerializerProvider serializer)
      throws IOException {
    jsonGenerator.writeRaw(gsonInstance.gson.toJson(gameBoardMapper.map(gameBoard)));
  }

  @Override
  public Class<GameBoard> handledType() {
    return GameBoard.class;
  }
}
