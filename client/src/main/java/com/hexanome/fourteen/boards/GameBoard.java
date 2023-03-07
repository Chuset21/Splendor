package com.hexanome.fourteen.boards;

import com.hexanome.fourteen.Main;
import com.hexanome.fourteen.ServerCaller;
import com.hexanome.fourteen.form.server.GameBoardForm;
import com.hexanome.fourteen.form.server.GemsForm;
import com.hexanome.fourteen.form.server.HandForm;
import com.hexanome.fourteen.form.server.PlayerForm;
import com.hexanome.fourteen.form.server.PurchaseCardForm;
import com.hexanome.fourteen.form.server.ReserveCardForm;
import com.hexanome.fourteen.form.server.cardform.CardForm;
import com.hexanome.fourteen.form.server.cardform.StandardCardForm;
import com.hexanome.fourteen.form.server.cardform.WaterfallCardForm;
import com.hexanome.fourteen.form.server.payment.GemPaymentForm;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.TokenRefreshFailedException;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import com.hexanome.fourteen.lobbyui.*;
import kong.unirest.HttpResponse;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * A class to represent the game objects required to represent a OrientExpansion Splendor game.
 */
public class GameBoard {

  private Stage stage;

  private GameBoardForm gameBoardForm;

  private static final Map<String/*Player id*/, String/*Player Icon Filename*/> PLAYER_ID_MAP =
      new HashMap<>();
  private static final String[] DEFAULT_PLAYER_ICONS =
      {"cat.jpg", "dog.jpg", "squirrel.jpg", "chameleon.jpg"};

  Bank bank;

  public static final int[] GEM_INDEX = {0, 1, 2, 3, 4, 5};

  private static Random random = new Random();

  // Player's Info
  private Player player;
  private List<Player> players;
  @FXML
  private ArrayList<ImageView> playerViews;
  @FXML
  private ArrayList<Label> playerNameLabels;
  @FXML
  private Label currentPlayerTurnLabel;
  private static final Effect currentPlayerEffect =
      new DropShadow(BlurType.GAUSSIAN, Color.web("#0048ff"), 24.5, 0, 0, 0);

  private ArrayList<Noble> gameNobles;

  @FXML
  private Button openBankButton;
  @FXML
  private Pane menuPopupPane;
  @FXML
  private Pane cardActionMenu;
  @FXML
  private Button cardReserveButton;
  @FXML
  private Button cardPurchaseButton;

  // PURCHASED CARDS PANE
  @FXML
  private Pane purchasedCardsView;
  private List<Image> purchasedCardImages = new ArrayList<Image>();
  @FXML
  private BorderPane purchasedBorderPane;

  // RESERVED CARDS PANE
  @FXML
  private Pane reservedCardsView;
  @FXML
  private VBox reservedCardsVBox;
  private List<Image> reservedCardImages = new ArrayList<Image>();

  // ACQUIRED NOBLES PANE
  @FXML
  private BorderPane acquiredNoblesView;
  private List<Image> acquiredNoblesImages = new ArrayList<Image>();
  @FXML
  private BorderPane reservedBorderPane;
  @FXML
  private VBox publicNoblesVBox;
  @FXML
  private DialogPane acquiredNobleAlertPane;
  @FXML
  private ImageView noblesStack;
  @FXML
  private GridPane discountSummary;
  @FXML
  private GridPane gemSummary;
  @FXML
  private BorderPane playerSummaryPane;
  @FXML
  private HBox reservedSummary;
  @FXML
  private HBox noblesSummary;
  @FXML
  private Label playerSummaryUserLabel;
  @FXML
  private Pane bankPane;
  @FXML
  private GridPane cardMatrix;
  @FXML
  private DialogPane waterfallPane;
  @FXML
  private VBox waterfallVBox;


  //CARD FIELDS
  //2D Array with all card FX ID names.
  @FXML
  private static final String[][] CARDS = new String[3][6];

  static {
    for (int row = 1; row < 4; row++) {
      for (int column = 1; column < 7; column++) {
        CARDS[row - 1][column - 1] = "R" + row + "C" + column;
      }
    }
  }

  @FXML
  private ImageView selectedCardView;
  @FXML
  private ImageView purchasedStack;
  @FXML
  private ImageView reservedStack;
  @FXML
  private ImageView cardActionImage;
  @FXML
  private ArrayList<ImageView> level3CardViewsBase;
  @FXML
  private ArrayList<ImageView> level2CardViewsBase;
  @FXML
  private ArrayList<ImageView> level1CardViewsBase;
  @FXML
  private ArrayList<ImageView> level3CardViewsOrient;
  @FXML
  private ArrayList<ImageView> level2CardViewsOrient;
  @FXML
  private ArrayList<ImageView> level1CardViewsOrient;
  @FXML
  private ArrayList<ArrayList> cardViews;

  //GEM FIELDS
  @FXML
  private List<Label> pGemLabels;
  @FXML
  private List<Label> bGemLabels;
  @FXML
  private List<Label> actionGemLabels;
  @FXML
  private List<Button> removeGemButtons;
  @FXML
  private List<Button> addGemButtons;
  @FXML
  private Pane takenTokenPane;
  @FXML
  private ArrayList<Label> takenTokenLabels;
  @FXML
  private Button takeBankButton;
  private Service<Void> service;

  /**
   * A call to this method displays the game on screen by initializing the scene with the gameboard.
   *
   * @param stage the stage currently being used to hold JavaFX UI items
   * @throws IOException throw an exception if an illegal UI action is executed
   */
  public void goToGame(Stage stage) throws IOException {
    this.stage = stage;

    // Get gameBoardForm
    gameBoardForm = Main.GSON.fromJson(
        Objects.requireNonNull(ServerCaller.getGameBoard(LobbyServiceCaller.getCurrentUserLobby()))
            .getBody(), GameBoardForm.class);

    setupPlayerMap();
    setupPlayers();

    // Set up bank
    bank = new Bank(players.size(), addGemButtons, removeGemButtons, takenTokenLabels, bGemLabels,
        openBankButton, takeBankButton, takenTokenPane, this);

    // Initialize the player's gems
    for (int idx : GEM_INDEX) {
      pGemLabels.get(idx).textProperty().set("" + player.getHand().gems[idx]);
    }

    // Set up game screen
    cardActionMenu.setVisible(false);
    purchasedCardsView.setVisible(false);
    takenTokenPane.setVisible(false);


    // Set up cards
    setupCards();
    setupBank();

    // Setup nobles CSV data and display on board
    generateNobles();

    service = new Service<>() {
      @Override
      protected Task<Void> createTask() {
        return new Task<>() {
          @Override
          protected Void call() {
            HttpResponse<String> longPollResponse = null;
            String hashedResponse = null;
            while (true) {
              int responseCode = 408;
              while (responseCode == 408) {
                longPollResponse = hashedResponse != null
                    ? ServerCaller.getGameBoard(LobbyServiceCaller.getCurrentUserLobby(),
                    hashedResponse) :
                    ServerCaller.getGameBoard(LobbyServiceCaller.getCurrentUserLobby());

                if (longPollResponse == null) {
                  break;
                }
                responseCode = longPollResponse.getStatus();
              }

              if (responseCode == 200) {
                hashedResponse = DigestUtils.md5Hex(longPollResponse.getBody());
                final GameBoardForm game =
                    Main.GSON.fromJson(longPollResponse.getBody(), GameBoardForm.class);
                Platform.runLater(() -> {
                  gameBoardForm = game;
                  updateBoard();
                });
              } else {
                LobbyServiceCaller.updateAccessToken();
              }
            }
          }
        };
      }
    };
    service.start();
  }

  public void updateBoard() {
    setupPlayers();

    // Set up cards
    setupCards();
    setupBank();

    // Setup nobles CSV data and display on board
    generateNobles();
  }

  public void closeAllActionWindows() {
    cardActionMenu.setVisible(false);
    reservedCardsView.setVisible(false);
    purchasedCardsView.setVisible(false);
    bank.close(gameBoardForm.availableGems());
  }

  public GameBoardForm getGameBoardForm(){
    return gameBoardForm;
  }

  /**
   * Puts all visible cards on the board
   */
  private void setupCards() {

    if (gameBoardForm == null) {
      throw new InvalidParameterException("gameBoardForm is null");
    }

    // Iterate through each list of the gameBoardForm.cards set (each different levels)
    for (List<CardForm> cardlist : gameBoardForm.cards()) {

      // Check to see that we have at least one card to assign
      if (!cardlist.isEmpty()) {

        // set listToUpdate (GUI list) according list's level
        final ArrayList<ImageView> listToUpdate;

        if (cardlist.get(0).expansion() == Expansion.STANDARD) {
          switch (cardlist.get(0).level()) {
            case ONE -> listToUpdate = level1CardViewsBase;
            case TWO -> listToUpdate = level2CardViewsBase;
            case THREE -> listToUpdate = level3CardViewsBase;
            default -> throw new IllegalStateException("Invalid card level from gameBoardForm.");
          }
          for (int i = 0; i < cardlist.size(); i++) {
            listToUpdate.get(i).setImage(new StandardCard((StandardCardForm) cardlist.get(i)));
          }
        } else {
          switch (cardlist.get(0).level()) {
            case ONE -> listToUpdate = level1CardViewsOrient;
            case TWO -> listToUpdate = level2CardViewsOrient;
            case THREE -> listToUpdate = level3CardViewsOrient;
            default -> throw new IllegalStateException("Invalid card level from gameBoardForm.");
          }
          for (int i = 0; i < cardlist.size(); i++) {
            listToUpdate.get(i).setImage(new OrientCard(cardlist.get(i)));
          }
        }
      }
    }
  }

  /**
   * Statically maps all players to a single image
   */
  private void setupPlayerMap() {
    if (gameBoardForm == null) {
      throw new InvalidParameterException("gameBoardForm is null");
    }

    int i = 0;

    for (PlayerForm player : gameBoardForm.players()) {
      PLAYER_ID_MAP.put(player.uid(),
          User.class.getResource("images/" + DEFAULT_PLAYER_ICONS[i]).toString());
      i = (i + 1) % DEFAULT_PLAYER_ICONS.length;
    }
  }

  /**
   * Refreshes displayed info for all player-related fields.
   * Current user will always be in position players.get(0)
   */
  private void setupPlayers() {
    if (gameBoardForm == null) {
      throw new InvalidParameterException("gameBoardForm is null");
    }

    // Reset list of players
    players = new ArrayList<Player>();

    int i = 0;

    // Add all players to players list
    for (PlayerForm playerForm : gameBoardForm.players()) {
      if (playerForm.uid().equals(LobbyServiceCaller.getCurrentUserid())) {
        player = new Player(playerForm, PLAYER_ID_MAP.get(playerForm.uid()));

        if (i != 0) {
          Player temp = players.get(0);
          players.set(0, player);
          players.add(temp);
        } else {
          players.add(player);
        }
      } else {
        players.add(new Player(playerForm, PLAYER_ID_MAP.get(playerForm.uid())));
      }
      i++;
    }

    // Place all players in their frames
    for (i = 0; i < playerViews.size(); i++) {
      if (i < players.size() && players.get(i) != null) {
        ((ImageView) playerViews.get(i)).setImage(players.get(i));
        Tooltip.install(((ImageView) playerViews.get(i)), new Tooltip(players.get(i).getUserId() +
            (player.getUserId().equals(
                players.get(i)
                    .getUserId()) ?
                " (you)" : "")));
      } else {
        ((ImageView) playerViews.get(i)).imageProperty().set(null);
      }
    }

    // Show whose turn it is
    displayCurrentPlayer();

    // Initialize the player's gems
    for (PlayerForm playerForm : gameBoardForm.players()) {
      if (playerForm.uid().equals(LobbyServiceCaller.getCurrentUserid())) {
        for (i = 0; i < 5; i++) {
          pGemLabels.get(i).textProperty()
              .set("" + GemsForm.costHashToArray(playerForm.hand().gems())[i]);
        }
      }
    }
  }

  private void displayCurrentPlayer() {
    if (gameBoardForm == null || playerViews == null) {
      throw new InvalidParameterException("gameBoardForm nor playerViews can be null");
    }

    for (int i = 0; i < players.size(); i++) {
      if (players.get(i).getUserId().equals(gameBoardForm.playerTurnid())) {
        currentPlayerTurnLabel.setText(players.get(i).getUserId() + "'s Turn");
        currentPlayerTurnLabel.setPadding(new Insets(5));
        ((ImageView) playerViews.get(i)).setEffect(currentPlayerEffect);
      } else {
        ((ImageView) playerViews.get(i)).setEffect(null);
      }
    }

    // Only allow the current player to alter the game (i.e. open bank or open card action menu)
    if (LobbyServiceCaller.getCurrentUserid().equals(gameBoardForm.playerTurnid())) {
      currentPlayerTurnLabel.setText("Your Turn");
      enableGameAlteringActions();
    } else {
      disableGameAlteringActions();
    }
  }

  /**
   * Refreshes displays for all gems in the bank
   */
  private void setupBank() {
    if (gameBoardForm == null || bank == null) {
      throw new InvalidParameterException("gameBoardForm nor bank can be null");
    }

    bank.updateGemCount(gameBoardForm.availableGems());
  }

  /**
   * A call to this method is made to determine the necessary action to do when a user uses the
   * mouse to perform an action.
   *
   * @param event a parameter with the MouseEvent to handle
   */
  public void handleCardSelect(MouseEvent event) {
    // Get imageview
    selectedCardView = (ImageView) event.getSource();

    //Input validation for null spaces
    if (selectedCardView.getImage() == null) {
      return;
    }

    // Set action pane image to the selected card
    cardActionImage.setImage(selectedCardView.getImage());

    // Set values in action pane to the card's cost
    for (int i = 0; i < ((Card) selectedCardView.getImage()).getCost().length; i++) {
      // Set label string to the respective cost of the card
      actionGemLabels.get(i).setText(((Card) selectedCardView.getImage()).getCost()[i] + "");
    }

    // Set gold gems to 0 -> !!!can change this later when implementing gold purchases!!!
    actionGemLabels.get(5).setText("0");

    ///// Handle Purchase Availability (By default: is available)
    cardPurchaseButton.setDisable(false);

    // get the player's hand and the cost of the card
    HandForm handForm = player.getHandForm();
    int[] selectedCost = ((Card) selectedCardView.getImage()).getCost();

    if (isYourTurn()) {
      // If player's gems cannot pay for card, disable purchase button
      for (int i = 0; i < 5; i++) {
        if (selectedCost[i] > GemsForm.costHashToArray(handForm.gems())[i]) {
          cardPurchaseButton.setDisable(true);
          break;
        }
      }
    } else {
      cardPurchaseButton.setDisable(true);
    }

    //// Handle Reserve Availability
    if (isYourTurn() && handForm.reservedCards().size() < 3 &&
        !handForm.reservedCards().contains(((Card) selectedCardView.getImage()).getCardForm())) {
      cardReserveButton.setDisable(false);
    } else {
      cardReserveButton.setDisable(true);
    }

    // Open menu
    cardActionMenu.toFront();
    cardActionMenu.setVisible(true);
  }

  /**
   * Performs all necessary UI changes when a player purchases a card.
   */
  public void handlePurchase() {
    // Get card to be purchased
    Card cardPurchased = (Card) selectedCardView.getImage();

    if (!(cardPurchased.getCardForm() instanceof StandardCardForm)) {
      return;
    }

    PurchaseCardForm purchaseCardForm = null;

    if (cardPurchased.getCardForm() instanceof StandardCardForm) {
      purchaseCardForm = new PurchaseCardForm(cardPurchased.getCardForm(),
          new GemPaymentForm(cardPurchased.getCardForm().cost(), null, 0),
          player.getHandForm().reservedCards().contains(cardPurchased.getCardForm()));
//    } else if (cardPurchased.getCardForm() instanceof WaterfallCardForm) {
//      purchaseCardForm = new PurchaseCardForm(,
//          new GemPaymentForm(cardPurchased.getCardForm().cost(), null, 0),
//          player.getHandForm().reservedCards().contains(cardPurchased.getCardForm()));
    }

    try {
      ServerCaller.purchaseCard(LobbyServiceCaller.getCurrentUserLobby(),
          LobbyServiceCaller.getCurrentUserAccessToken(), purchaseCardForm);

      // Close card menu
      cardActionMenu.setVisible(false);

      closeAllActionWindows();
      updateBoard();
    } catch (TokenRefreshFailedException e) {
      try {
        MenuController.returnToLogin("Session timed out, retry login");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
  }

  /**
   * Performs all necessary UI changes when a player reserves a card.
   */
  public void handleReserve() {
    // Get card to be purchased
    Card cardReserved = (Card) selectedCardView.getImage();

    ReserveCardForm reserveCardForm = new ReserveCardForm(cardReserved.getCardForm(), null, false);

    try {
      ServerCaller.reserveCard(LobbyServiceCaller.getCurrentUserLobby(),
          LobbyServiceCaller.getCurrentUserAccessToken(), reserveCardForm);

      // Close card menu
      cardActionMenu.setVisible(false);

      closeAllActionWindows();
      updateBoard();
    } catch (TokenRefreshFailedException e) {
      try {
        MenuController.returnToLogin("Session timed out, retry login");
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
  }

  private boolean isYourTurn() {
    return gameBoardForm.playerTurnid().equals(player.getUserId());
  }

  public void handleTakeGreenGemButton() {
    bank.takeGem(0);
  }

  public void handleReturnGreenGemButton() {
    bank.returnGem(0);
  }

  public void handleTakeWhiteGemButton() {
    bank.takeGem(1);
  }

  public void handleReturnWhiteGemButton() {
    bank.returnGem(1);
  }

  public void handleTakeBlueGemButton() {
    bank.takeGem(2);
  }

  public void handleReturnBlueGemButton() {
    bank.returnGem(2);
  }

  public void handleTakeBlackGemButton() {
    bank.takeGem(3);
  }

  public void handleReturnBlackGemButton() {
    bank.returnGem(3);
  }

  public void handleTakeRedGemButton() {
    bank.takeGem(4);
  }

  public void handleReturnRedGemButton() {
    bank.returnGem(4);
  }

  public void handleTakeYellowGemButton() {
    bank.takeGem(5);
  }

  public void handleReturnYellowGemButton() {
    bank.returnGem(5);
  }

  @FXML
  private void handleClickOpenBankButton() {
    bank.toggle();
  }

  @FXML
  private void handleClickTakeBankButton() {
    bank.take();
  }

  @FXML
  private void handleClickMenuButton() {
    menuPopupPane.toFront();
    menuPopupPane.setVisible(true);
  }

  @FXML
  private void handleClickMenuPopupBackButton() {
    menuPopupPane.setVisible(false);
  }

  @FXML
  private void handleClickMenuPopupQuitButton() {
    try {
      service.cancel();
      LobbyServiceCaller.deleteLaunchedSession();
      LobbyServiceCaller.setCurrentUserLobby(null);
    } catch (TokenRefreshFailedException e) {
      try {
        MenuController.returnToLogin("Session timed out, retry login");
        stage.close();
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }

    try {
      MenuController.goToWelcomeScreen();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  @FXML
  private void handleExitCardMenu() {
    selectedCardView = null;
    cardActionMenu.setVisible(false);
  }

  @FXML
  // viewParams = [cardWidth, cardHeight, numOfColumns]
  public GridPane generateCardGrid(List<Image> imageList, int[] viewParams) {
    // Create a GridPane to hold the images
    GridPane cardImageGrid = new GridPane();
    cardImageGrid.setHgap(10);
    cardImageGrid.setVgap(10);

    // Loop through the images and add them to the GridPane
    int row = 0;
    int col = 0;
    for (Image image : imageList) {
      ImageView cardIV = new ImageView(image);
      cardIV.setFitWidth(viewParams[0]);
      cardIV.setFitHeight(viewParams[1]);
      cardImageGrid.add(cardIV, col, row);
      col++;

      // Store 9 cards per row
      if (col == viewParams[2]) {
        col = 0;
        row++;
      }
    }
    return cardImageGrid;
  }

  @FXML
  // viewParams = [cardWidth, cardHeight, numOfColumns]
  public GridPane generateCardGridForReserved(List<Image> imageList, int[] viewParams,
                                              Consumer<MouseEvent> mouseClickEvent) {
    // Create a GridPane to hold the images
    GridPane cardImageGrid = new GridPane();
    cardImageGrid.setHgap(10);
    cardImageGrid.setVgap(10);

    // Loop through the images and add them to the GridPane
    int row = 0;
    int col = 0;
    for (Image image : imageList) {
      ImageView cardIV = new ImageView(image);
      cardIV.setFitWidth(viewParams[0]);
      cardIV.setFitHeight(viewParams[1]);
      cardIV.setOnMouseClicked(e -> {
        mouseClickEvent.accept(e);
      });

      // Add card and it's button
      cardImageGrid.add(cardIV, col, row);
      cardImageGrid.add(createPurchaseButtonForReservedCard(cardIV), col, 1);
      col++;

      // Store 9 cards per row
      if (col == viewParams[2]) {
        col = 0;
        row++;
      }
    }
    return cardImageGrid;
  }

  public GridPane generateCardGridForWaterfall(List<Image> imageList, int[] viewParams,
                                              Consumer<MouseEvent> mouseClickEvent) {
    // Create a GridPane to hold the images
    GridPane cardImageGrid = new GridPane();
    cardImageGrid.setHgap(10);
    cardImageGrid.setVgap(10);

    // Loop through the images and add them to the GridPane
    int row = 0;
    int col = 0;
    for (Image image : imageList) {
      ImageView cardIV = new ImageView(image);
      cardIV.setFitWidth(viewParams[0]);
      cardIV.setFitHeight(viewParams[1]);
      cardIV.setOnMouseClicked(e -> {
        mouseClickEvent.accept(e);
      });

      // Add card and it's button
      cardImageGrid.add(cardIV, col, row);
      col++;

      // Store 9 cards per row
      if (col == viewParams[2]) {
        col = 0;
        row++;
      }
    }
    return cardImageGrid;
  }

  @FXML
  public void handlePurchasedPaneSelect(MouseEvent event) {

    purchasedCardImages.clear();

    for (CardForm cardForm : player.getHandForm().purchasedCards()) {
      purchasedCardImages.add(
          (cardForm instanceof StandardCardForm) ? new StandardCard((StandardCardForm) cardForm) :
              new OrientCard(cardForm));
    }

    // Reset pane and set the purchased pane's content to the card image grid
    purchasedBorderPane.setCenter(generateCardGrid(purchasedCardImages, new int[] {120, 170, 9}));

    // Open purchased pane
    purchasedCardsView.toFront();
    purchasedCardsView.setVisible(true);
  }

  @FXML
  private void handleExitActionMenu() {
    purchasedCardsView.setVisible(false);
    reservedCardsView.setVisible(false);
    acquiredNoblesView.setVisible(false);
  }

  private Button createPurchaseButtonForReservedCard(ImageView imageView) {
    // Create purchase button for each reserved card
    Button purchaseReservedCardButton = new Button("Purchase");
    purchaseReservedCardButton.setStyle("-fx-background-color: #5A9BD7;");
    purchaseReservedCardButton.setFont(new Font("Satoshi Black", 30.0));
    purchaseReservedCardButton.setTextFill(javafx.scene.paint.Color.WHITE);

    purchaseReservedCardButton.setOnMouseClicked(e -> {
      handleCardSelect(e.copyFor(imageView, e.getTarget()));
    });

    return purchaseReservedCardButton;
  }

  @FXML
  public void handleReservedPaneSelect(MouseEvent event) {
    reservedCardImages.clear();

    for (CardForm cardForm : player.getHandForm().reservedCards()) {
      reservedCardImages.add(
          (cardForm instanceof StandardCardForm) ? new StandardCard((StandardCardForm) cardForm) :
              new OrientCard(cardForm));
    }

    // Create image grid of reserved cards
    GridPane imagePane = generateCardGridForReserved(reservedCardImages,
        new int[] {200, 260, reservedCardImages.size()}, this::handleCardSelect);

    // Reset card grid and add images to the pane
    reservedCardsVBox.getChildren().clear();
    reservedCardsVBox.getChildren().add(imagePane);

    // Open reserved cards pane
    reservedCardsView.toFront();
    reservedCardsView.setVisible(true);
  }

  @FXML
  public void generateNobles() {
    if (gameBoardForm == null) {
      throw new InvalidParameterException("gameBoardForm is null");
    }

    // Clear Nobles from board
    publicNoblesVBox.getChildren().clear();

    // Create list of available nobles
    gameNobles = Noble.interpretNobles(gameBoardForm);

    // Identify current player
    PlayerForm currentPlayer = null;
    for (PlayerForm p : gameBoardForm.players()) {
      if (p.uid().equals(gameBoardForm.playerTurnid())) {
        currentPlayer = p;
      }
    }

    // Add Nobles Text Label
    Label noblesTitleLabel = new Label("Nobles");
    noblesTitleLabel.setFont(Font.font("Satoshi", FontWeight.BOLD, 24));
    noblesTitleLabel.setTextFill(Color.rgb(114, 70, 91));
    noblesTitleLabel.setPadding(new Insets(5));
    publicNoblesVBox.getChildren().add(noblesTitleLabel);


    // Add the nobles to board
    for (Noble n : gameNobles) {
      ImageView iv = new ImageView();
      iv.setFitHeight(100);
      iv.setFitWidth(100);
      iv.setImage(n);

      // Check if current player should be visited by a noble, else add it publicly
      if (isValidNobleVisit(GemsForm.costHashToArray(currentPlayer.hand().gemDiscounts()),
          n.getCost())) {
        activateAcquireNoblePrompt(iv);
        // Only one noble can be acquired per turn
        break;
      } else {
        publicNoblesVBox.getChildren().add(iv);
      }
    }
  }

  private boolean isValidNobleVisit(int[] playerBonuses, int[] nobleCost) {
    return IntStream.range(0, nobleCost.length).noneMatch(i -> playerBonuses[i] < nobleCost[i]);
  }

  @FXML
  public void activateAcquireNoblePrompt(ImageView iv) {
    VBox vb = new VBox();
    vb.setPadding(new Insets(10));
    vb.setSpacing(10);
    vb.setAlignment(Pos.CENTER);

    Label notice = new Label("A noble has visited you! You gained 3 prestige points.");
    notice.setFont(new Font("Satoshi", 16));

    // Noble Image
    vb.getChildren().add(iv);
    // Textual Prompt
    vb.getChildren().add(notice);

    acquiredNobleAlertPane.setContent(vb);
    acquiredNobleAlertPane.setVisible(true);
    acquiredNobleAlertPane.lookupButton(ButtonType.OK).setOnMouseClicked(event -> {
      acquiredNobleAlertPane.setVisible(false);
      // Update top of noble stack for player
      noblesStack.setImage(iv.getImage());
      // Add image to full list of player's acquired nobles
      acquiredNoblesImages.add(iv.getImage());
    });
  }

  @FXML
  public void handleAcquiredNoblesViewSelect(MouseEvent event) {
    // Set the purchased pane's content to the card image grid
    GridPane nobleGrid = generateCardGrid(acquiredNoblesImages, new int[] {180, 180, 3});
    nobleGrid.setPadding(new Insets(0, 70, 0, 70));
    acquiredNoblesView.setCenter(nobleGrid);

    // Open purchased pane
    acquiredNoblesView.setVisible(true);
  }

  @FXML
  public void createPlayerSummary(MouseEvent event) {
    Player player;
    PlayerForm requestedPlayer = null;

    // Determines which player's information is being requested
    try {
      player = ((Player) ((ImageView) event.getSource()).getImage());
    } catch (NullPointerException e) {
      System.out.println("Failed to identify player");
      return;
    }

    requestedPlayer = player.getPlayerForm();

    // Aborts if no player data was found
    if (requestedPlayer == null) {
      System.out.println("DEBUG: Requested player data was null");
      return;
    }

    // Set summary label as player title
    playerSummaryUserLabel.setText(requestedPlayer.uid() + "'s Board");

    // Fetch and apply the user's discounts to the summary discount matrix
    int index = 0;
    for (int discount : GemsForm.costHashToArrayWithGold(requestedPlayer.hand().gemDiscounts())) {
      Label label = (Label) discountSummary.getChildren().get(index);
      label.setText(String.valueOf(discount));
      index++;
    }

    // Fetch and apply the user's gems to the summary gem matrix
    index = 0;
    for (int gemAmt : GemsForm.costHashToArrayWithGold(requestedPlayer.hand().gems())) {
      Label label = (Label) gemSummary.getChildren().get(index);
      label.setText(String.valueOf(gemAmt));
      index++;
    }

    // Reset content before updating it
    reservedSummary.getChildren().clear();
    noblesSummary.getChildren().clear();

    // Fetch and apply the player's reserved cards as images
    List<Image> requestedPlayerReservedCardImages = new ArrayList<>();
    Card cardToAdd = null;
    for (CardForm c : requestedPlayer.hand().reservedCards()) {
      if (c instanceof StandardCardForm) {
        cardToAdd = new StandardCard((StandardCardForm) c);
      } else {
        cardToAdd = new OrientCard(c);
      }
      // TODO Add instanceof Waterfall card and other card types if there are any
      if (cardToAdd != null) {
        requestedPlayerReservedCardImages.add(cardToAdd);
      }
    }
    reservedSummary.getChildren().clear();
    reservedSummary.getChildren()
        .add(generateCardGrid(requestedPlayerReservedCardImages, new int[] {110, 160, 3}));

    // TODO: Fetch and apply the player's nobles as images
    List<Image> requestedPlayerNobleImages = new ArrayList<>();
//    for (Noble n : requestedPlayer.getHand().nobles) {
//      requestedPlayerNobleImages.add(n);
//    }
//    noblesSummary.getChildren().add(generateCardGrid(requestedPlayerNoblesImages, new int[] {160, 160, 5}));

    // Display data
    playerSummaryPane.toFront();
    playerSummaryPane.setVisible(true);
  }

  @FXML
  public void closePlayerSummary() {
    playerSummaryPane.setVisible(false);
  }

  @FXML
  private void disableGameAlteringActions() {
    bankPane.setDisable(true);
    cardMatrix.setDisable(true);
  }


  @FXML
  private void enableGameAlteringActions() {
    bankPane.setDisable(false);
    cardMatrix.setDisable(false);
  }

  private List<CardForm> fetchWaterfallSelection() {
    // Initialize free card selection list
    List<CardForm> waterfallSelection = new ArrayList<>();

    // Fetch user's free card choices and add to list
    for (List<CardForm> decks : gameBoardForm.cards()) {
      for (CardForm c : decks) {
        if ((c.expansion().equals(Expansion.ORIENT)) && (c.level().equals(2))) {
          waterfallSelection.add(c);
        }
      }
    }
    return waterfallSelection;
  }

  @FXML
  private void displayWaterfallChoices() {
    List<CardForm> waterfallSelection = fetchWaterfallSelection();

    // Display the free card choices to user
    List<Image> cardChoicesImages = new ArrayList<>();
    waterfallSelection.forEach((c) -> cardChoicesImages.add((Image) new OrientCard(c)));

    // Apply event handler to each card in choices
    //cardChoicesImages.forEach((c) -> c);

    //waterfallVBox.getChildren().add(generateCardGridForWaterfall(cardChoicesImages, new int[] {120, 170}, this::handleWaterfallChoiceSelect));
    waterfallPane.setVisible(true);
  }

  @FXML
  public void handleWaterfallChoiceSelect(MouseEvent event, WaterfallCardForm wf) {
    CardForm selectedCard = ((Card) event.getSource()).getCardForm();
    WaterfallCardForm waterfallWithSelection = new WaterfallCardForm(wf, selectedCard);
  }
}