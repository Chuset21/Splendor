package hexanome.fourteen.server.control;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import hexanome.fourteen.server.model.board.GameBoard;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Save game manager.
 */
@Service
public class SaveGameManager implements SavedGamesService {

  private final int port;
  private final String dbName;
  private final String host;
  private final String collectionName;
  private final GsonInstance gsonInstance;
  private MongoClient mongoClient;
  private MongoCollection<Document> savedDocuments;

  /**
   * Constructor.
   *
   * @param port           port number
   * @param dbName         database name
   * @param host           the host name
   * @param collectionName collection name
   * @param gsonInstance   gson instance
   */
  public SaveGameManager(@Value("${spring.data.mongodb.port}") int port,
                         @Value("${spring.data.mongodb.database}") String dbName,
                         @Value("${spring.data.mongodb.host}") String host,
                         @Value("${db.collection.name}") String collectionName,
                         @Autowired GsonInstance gsonInstance) {
    this.port = port;
    this.dbName = dbName;
    this.host = host;
    this.collectionName = collectionName;
    this.gsonInstance = gsonInstance;
  }

  @PostConstruct
  private void initialiseDatabase() {
    mongoClient = new MongoClient(new ServerAddress(host, port),
        new MongoClientOptions.Builder().maxConnectionIdleTime(600000).build());
    final MongoDatabase db = mongoClient.getDatabase(dbName);
    savedDocuments = db.getCollection(collectionName);
  }

  @Override
  public GameBoard getGame(String saveGameid) {
    final Document doc = savedDocuments.find(Filters.eq(new ObjectId(saveGameid))).first();
    return doc != null ? gsonInstance.gson.fromJson(doc.toJson(), GameBoard.class) : null;
  }

  @Override
  public String putGame(GameBoard gameBoard) {
    final Document doc = Document.parse(gsonInstance.gson.toJson(gameBoard));
    savedDocuments.insertOne(doc);
    return doc.getObjectId("_id").toHexString();
  }

  @PreDestroy
  private void closeMongo() {
    mongoClient.close();
  }
}
