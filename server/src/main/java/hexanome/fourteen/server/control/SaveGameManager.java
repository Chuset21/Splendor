package hexanome.fourteen.server.control;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Save game manager.
 */
@Service
public class SaveGameManager implements SavedGamesService {

  private final int port;
  private final String dbName;
  private final String collectionName;
  private final Gson gson;
  private MongoClient mongoClient;
  private MongoCollection<GameBoard> savedGames;
  private MongoCollection<Document> savedDocuments;

  /**
   * Constructor.
   *
   * @param port           port number
   * @param dbName         database name
   * @param collectionName collection name
   */
  public SaveGameManager(@Value("${db.port}") int port, @Value("${db.name}") String dbName,
                         @Value("${db.collection.name}") String collectionName) {
    this.port = port;
    this.dbName = dbName;
    this.collectionName = collectionName;
    this.gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).serializeNulls()
        .create();
  }

  @PostConstruct
  private void initialiseDatabase() {
    final CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    mongoClient = new MongoClient(new ServerAddress("localhost", port),
        new MongoClientOptions.Builder().maxConnectionIdleTime(600000).connectionsPerHost(2)
            .cursorFinalizerEnabled(false).build());
    System.out.println("Created mongoClient");
    final MongoDatabase db = mongoClient.getDatabase(dbName);
    System.out.println("Got database");
    savedDocuments = db.getCollection(collectionName);
    System.out.println("Got first collection");
    savedGames =
        db.getCollection(collectionName, GameBoard.class).withCodecRegistry(pojoCodecRegistry);
    System.out.println("Got second collection");
  }

  @Override
  public GameBoard getGame(String saveGameid) {
    return savedGames.find(Filters.eq(saveGameid)).first();
  }

  @Override
  public String putGame(GameBoard gameBoard) {
    final Document doc = Document.parse(gson.toJson(gameBoard));
    savedDocuments.insertOne(doc);
    return doc.getObjectId("_id").toString();
  }

  @PreDestroy
  private void closeMongo() {
    mongoClient.close();
  }
}
