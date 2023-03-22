package com.hexanome.fourteen.boards;

import com.google.gson.reflect.TypeToken;
import com.hexanome.fourteen.GameServiceName;
import com.hexanome.fourteen.Main;
import com.hexanome.fourteen.ServerCaller;
import com.hexanome.fourteen.form.server.CityForm;
import com.hexanome.fourteen.form.server.ClaimCityForm;
import com.hexanome.fourteen.form.server.ClaimNobleForm;
import com.hexanome.fourteen.form.server.GameBoardForm;
import com.hexanome.fourteen.form.server.GemsForm;
import com.hexanome.fourteen.form.server.HandForm;
import com.hexanome.fourteen.form.server.NobleForm;
import com.hexanome.fourteen.form.server.PlayerForm;
import com.hexanome.fourteen.form.server.PurchaseCardForm;
import com.hexanome.fourteen.form.server.ReserveCardForm;
import com.hexanome.fourteen.form.server.cardform.CardForm;
import com.hexanome.fourteen.form.server.cardform.CardLevelForm;
import com.hexanome.fourteen.form.server.cardform.DoubleBonusCardForm;
import com.hexanome.fourteen.form.server.cardform.GoldGemCardForm;
import com.hexanome.fourteen.form.server.cardform.ReserveNobleCardForm;
import com.hexanome.fourteen.form.server.cardform.SatchelCardForm;
import com.hexanome.fourteen.form.server.cardform.StandardCardForm;
import com.hexanome.fourteen.form.server.cardform.WaterfallCardForm;
import com.hexanome.fourteen.form.server.payment.GemPaymentForm;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.TokenRefreshFailedException;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import com.hexanome.fourteen.lobbyui.*;
import javafx.util.Duration;
import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;
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

  private static final Random random = new Random();

  // Player's Info
  public Player player;
  private List<Player> players;
  @FXML
  private ArrayList<ImageView> playerViews;
  @FXML
  private ArrayList<Label> playerNameLabels;
  @FXML
  private Label currentPlayerTurnLabel;
  private static final Effect BLUE_GLOW_EFFECT =
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
  private final List<Image> purchasedCardImages = new ArrayList<Image>();
  @FXML
  private BorderPane purchasedBorderPane;

  // RESERVED CARDS PANE
  @FXML
  private Pane reservedCardsView;
  @FXML
  private VBox reservedCardsVBox;
  private final List<Image> reservedCardImages = new ArrayList<Image>();

  // ACQUIRED NOBLES PANE
  @FXML
  private BorderPane acquiredNoblesView;
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
  private Label waterfallPaneSubtitle;

  @FXML
  private Label waterfallPaneTitle;
  @FXML
  private VBox waterfallVBox;

  private Card tentativeCardSelection;
  private Noble tentativeNobleSelection;
  private City tentativeCitySelection;
  @FXML
  private VBox nobleAcquiredLabelVBox;


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
  @FXML
  public DialogPane discardTokenPane;
  @FXML
  public GridPane discardTokenGrid;
  @FXML
  public Label discardTokenLabel;
  public TokenDiscarder tokenDiscarder;
  private boolean hasBeenLastRound;
  @FXML
  private Button tradingPostsButton;
  private TradingPostsMenu tradingPostsMenu;
  @FXML
  private AnchorPane backgroundPane;

  /**
   * A call to this method displays the game on screen by initializing the scene with the gameboard.
   *
   * @param stage the stage currently being used to hold JavaFX UI items
   */
  public void goToGame(Stage stage) {
    this.stage = stage;
    hasBeenLastRound = false;

    HttpResponse<String> s = ServerCaller.getGameBoard(LobbyServiceCaller.getCurrentUserLobby());
    // Get gameBoardForm
    gameBoardForm = Main.GSON.fromJson(
        Objects.requireNonNull(s)
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

    // Setup trading posts menu
    if (GameServiceName.getExpansions(LobbyServiceCaller.getCurrentUserLobby().getGameServiceName())
        .contains(Expansion.TRADING_POSTS)) {
      tradingPostsButton.setVisible(true);
      try {
        tradingPostsMenu = new TradingPostsMenu(this);
        backgroundPane.getChildren().add(tradingPostsMenu);
        tradingPostsMenu.setLayoutX(
            (backgroundPane.getPrefWidth() - tradingPostsMenu.getPrefWidth() - 100) / 2);
        tradingPostsMenu.setLayoutY(
            (backgroundPane.getPrefHeight() - tradingPostsMenu.getPrefHeight()) / 2);
        tradingPostsMenu.setVisible(false);
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    } else {
      tradingPostsButton.setVisible(false);
    }


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

    // Update tradingPostsMenu
    if(GameServiceName.getExpansions(LobbyServiceCaller.getCurrentUserLobby().getGameServiceName())
        .contains(Expansion.TRADING_POSTS)){
      tradingPostsMenu.updateMenu();
    }

    final String leadingPlayer = gameBoardForm.leadingPlayer().uid();
    if (gameBoardForm.isGameOver()) {
      closeAllActionWindows();
      disableGameAlteringActions();
      // TODO show the player that won
      System.out.printf("Game is over, winner: %s\n", leadingPlayer);
    } else {
      if (gameBoardForm.isLastRound() && !hasBeenLastRound) {
        hasBeenLastRound = true;
        System.out.println("Last round of the game!!"); // TODO show a last round message instead
        // Stub for showing popup
        final PauseTransition wait = new PauseTransition(Duration.seconds(2));
        wait.setOnFinished((e) -> {
          // Disabling the button after a duration of time
//          popup.setDisable(true);
//          popup.setVisible(false);
        });
//        popup.setDisable(false);
//        popup.setVisible(true);
        wait.play();
      }
      // TODO show the leading player??
    }
  }

  public void closeAllActionWindows() {
    cardActionMenu.setVisible(false);
    reservedCardsView.setVisible(false);
    purchasedCardsView.setVisible(false);
    if (tokenDiscarder != null) {
      tokenDiscarder.close();
    }
    bank.close(gameBoardForm.availableGems());
  }

  public GameBoardForm getGameBoardForm() {
    return gameBoardForm;
  }

  /**
   * Puts all visible cards on the board
   */
  private void setupCards() {

    if (gameBoardForm == null) {
      throw new InvalidParameterException("gameBoardForm is null");
    }
    final Map<CardLevelForm, Map<Expansion, List<CardForm>>> cardLevelFormListMap = new HashMap<>();
    for (CardLevelForm level : CardLevelForm.values()) {
      cardLevelFormListMap.put(level, new HashMap<>());
      cardLevelFormListMap.get(level).put(Expansion.STANDARD, Collections.emptyList());
      cardLevelFormListMap.get(level).put(Expansion.ORIENT, Collections.emptyList());
    }
    // Iterate through each list of the gameBoardForm.cards set (each different levels)
    for (List<CardForm> cardList : gameBoardForm.cards()) {
      // Check to see that we have at least one card to assign
      if (!cardList.isEmpty()) {
        // set listToUpdate (GUI list) according list's level
        final ArrayList<ImageView> listToUpdate;
        final CardForm cardForm = cardList.get(0);
        if (cardForm.expansion() == Expansion.STANDARD) {
          cardLevelFormListMap.get(cardForm.level()).put(Expansion.STANDARD, cardList);
          switch (cardForm.level()) {
            case ONE -> listToUpdate = level1CardViewsBase;
            case TWO -> listToUpdate = level2CardViewsBase;
            case THREE -> listToUpdate = level3CardViewsBase;
            default -> throw new IllegalStateException("Invalid card level from gameBoardForm.");
          }
          for (int i = 0; i < listToUpdate.size(); i++) {
            if (i < cardList.size()) {
              listToUpdate.get(i).setImage(new StandardCard((StandardCardForm) cardList.get(i)));
              listToUpdate.get(i).setDisable(false);
            } else {
              listToUpdate.get(i).imageProperty().set(null);
              listToUpdate.get(i).setDisable(true);
            }
          }
        } else {
          cardLevelFormListMap.get(cardForm.level()).put(Expansion.ORIENT, cardList);
          switch (cardForm.level()) {
            case ONE -> listToUpdate = level1CardViewsOrient;
            case TWO -> listToUpdate = level2CardViewsOrient;
            case THREE -> listToUpdate = level3CardViewsOrient;
            default -> throw new IllegalStateException("Invalid card level from gameBoardForm.");
          }
          for (int i = 0; i < listToUpdate.size(); i++) {
            if (i < cardList.size()) {
              listToUpdate.get(i).setImage(new OrientCard(cardList.get(i)));
              listToUpdate.get(i).setDisable(false);
            } else {
              listToUpdate.get(i).imageProperty().set(null);
              listToUpdate.get(i).setDisable(true);
            }
          }
        }
      }
    }
    for (CardLevelForm level : CardLevelForm.values()) {
      final List<CardForm> standardCards = cardLevelFormListMap.get(level).get(Expansion.STANDARD);
      final List<CardForm> orientCards = cardLevelFormListMap.get(level).get(Expansion.ORIENT);
      switch (level) {
        case ONE -> removeImagesFromDeck(standardCards, orientCards, level1CardViewsBase,
            level1CardViewsOrient);
        case TWO -> removeImagesFromDeck(standardCards, orientCards, level2CardViewsBase,
            level2CardViewsOrient);
        case THREE -> removeImagesFromDeck(standardCards, orientCards, level3CardViewsBase,
            level3CardViewsOrient);
        default -> throw new IllegalStateException("Invalid card level from gameBoardForm.");
      }
    }
  }

  private void removeImagesFromDeck(List<CardForm> standardCards, List<CardForm> orientCards,
                                    List<ImageView> standardView, List<ImageView> orientView) {
    if (standardCards.isEmpty()) {
      standardView.forEach(e -> {
        e.imageProperty().set(null);
        e.setDisable(true);
      });
    }
    if (orientCards.isEmpty()) {
      orientView.forEach(e -> {
        e.imageProperty().set(null);
        e.setDisable(true);
      });
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
    players = new ArrayList<>();

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
        playerViews.get(i).setImage(players.get(i));
        Tooltip.install(playerViews.get(i), new Tooltip(players.get(i).getUserId()
            + (player.getUserId().equals(
            players.get(i).getUserId())
            ? " (you)" : "")));
      } else {
        playerViews.get(i).imageProperty().set(null);
      }
    }

    // Show whose turn it is
    displayCurrentPlayer();

    // Initialize the player's gems
    for (PlayerForm playerForm : gameBoardForm.players()) {
      if (playerForm.uid().equals(LobbyServiceCaller.getCurrentUserid())) {
        for (i = 0; i < 6; i++) {
          pGemLabels.get(i).textProperty()
              .set("" + GemsForm.costHashToArrayWithGold(playerForm.hand().gems())[i]);
        }
      }
    }
  }

  private void displayCurrentPlayer() {
    if (gameBoardForm == null || playerViews == null) {
      throw new InvalidParameterException("neither gameBoardForm nor playerViews can be null");
    }

    for (int i = 0; i < players.size(); i++) {
      if (players.get(i).getUserId().equals(gameBoardForm.playerTurnid())) {
        currentPlayerTurnLabel.setText(players.get(i).getUserId() + "'s Turn");
        currentPlayerTurnLabel.setPadding(new Insets(5));
        playerViews.get(i).setEffect(BLUE_GLOW_EFFECT);
      } else {
        playerViews.get(i).setEffect(null);
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
    final CardForm cardForm = ((Card) selectedCardView.getImage()).getCardForm();
    final GemsForm cost = cardForm.cost();
    final int[] gemsCost = cost == null ? new int[] {0, 0, 0, 0, 0, 0} : GemsForm.costHashToArray(
        cost.getDiscountedCost(player.getHandForm().gemDiscounts()));

    // Set values in action pane to the card's cost
    for (int i = 0; i < gemsCost.length; i++) {
      // Set label string to the respective cost of the card
      actionGemLabels.get(i).setText(String.valueOf(gemsCost[i]));
    }

    // Set gold gems to 0 -> !!!can change this later when implementing gold purchases!!!
    actionGemLabels.get(5).setText("0");

    ///// Handle Purchase Availability (By default: is available)
    cardPurchaseButton.setDisable(false);

    // get the player's hand and the cost of the card
    HandForm handForm = player.getHandForm();
    int[] selectedCost = Arrays.copyOf(gemsCost, gemsCost.length);

    if (isYourTurn()) {
      // If player's gems cannot pay for card, disable purchase button
      boolean canAfford = true;
      for (int i = 0; i < 5; i++) {
        if (selectedCost[i] > GemsForm.costHashToArray(handForm.gems())[i]) {
          cardPurchaseButton.setDisable(true);
          canAfford = false;
          break;
        }
      }
      if (canAfford && cardForm instanceof SatchelCardForm) {
        final long gemDiscountCards =
            player.getHandForm().purchasedCards().stream().filter(this::cardIsAttachable)
                .count();
        if (gemDiscountCards < 1) {
          cardPurchaseButton.setDisable(true);
        }
      }
    } else {
      cardPurchaseButton.setDisable(true);
    }

    //// Handle Reserve Availability
    cardReserveButton.setDisable(!isYourTurn() || handForm.reservedCards().size() >= 3
        || handForm.reservedCards().contains(cardForm));

    // Open menu
    cardActionMenu.toFront();
    cardActionMenu.setVisible(true);
  }

  private boolean cardIsAttachable(CardForm cardForm) {
    return !(cardForm instanceof GoldGemCardForm || cardForm instanceof SatchelCardForm);
  }

  /**
   * Performs all necessary UI changes when a player purchases a card.
   */
  public void handlePurchase() {
    // Get card to be purchased
    Card cardPurchased = (Card) selectedCardView.getImage();

    final CardForm cardForm = cardPurchased.getCardForm();
    if (cardForm instanceof StandardCardForm
        || cardForm instanceof GoldGemCardForm
        || cardForm instanceof DoubleBonusCardForm) {
      final PurchaseCardForm purchaseCardForm = new PurchaseCardForm(cardForm,
          new GemPaymentForm(cardForm.cost()
              .getDiscountedCost(player.getHandForm().gemDiscounts()), 0),
          player.getHandForm().reservedCards().contains(cardForm), null);
      purchaseCard(purchaseCardForm);
    } else if (cardForm instanceof WaterfallCardForm) {
      displayWaterfallChoices(cardForm, CardLevelForm.TWO, f ->
          purchaseCard(new PurchaseCardForm(cardForm,
              new GemPaymentForm(
                  cardForm.cost().getDiscountedCost(player.getHandForm().gemDiscounts()),
                  0),
              player.getHandForm().reservedCards().contains(cardForm), null)));
    } else if (cardForm instanceof ReserveNobleCardForm r) {
      purchaseReserveNobleCard(r, f ->
          purchaseCard(new PurchaseCardForm(cardForm,
              new GemPaymentForm(cardForm.cost()
                  .getDiscountedCost(player.getHandForm().gemDiscounts()), 0),
              player.getHandForm().reservedCards().contains(cardForm), null)));
    } else if (cardForm instanceof SatchelCardForm s) {
      if (s.level() == CardLevelForm.TWO) {
        purchaseLevelOneSatchelCard(s, f -> displayWaterfallChoices(s, CardLevelForm.ONE, x -> {
          purchaseCard(new PurchaseCardForm(cardForm,
              new GemPaymentForm(
                  cardForm.cost().getDiscountedCost(player.getHandForm().gemDiscounts()),
                  0),
              player.getHandForm().reservedCards().contains(cardForm), null));
        }));
      } else {
        purchaseLevelOneSatchelCard(s, f ->
            purchaseCard(new PurchaseCardForm(s,
                new GemPaymentForm(s.cost()
                    .getDiscountedCost(player.getHandForm().gemDiscounts()), 0),
                player.getHandForm().reservedCards().contains(s), null)));
      }
    } else {
      System.out.println("This card is not yet handled");
    }
  }

  private void purchaseLevelOneSatchelCard(SatchelCardForm satchelCard,
                                           Consumer<Void> function) {
    final List<CardForm> cardToAttachSelection =
        player.getHandForm().purchasedCards().stream().filter(this::cardIsAttachable).toList();
    // Generate HBox to display the card choices to user
    final HBox choicesHBox = new HBox();
    choicesHBox.setSpacing(5);
    choicesHBox.setPadding(new Insets(5));

    // Generate ImageView for each selectable card form
    for (CardForm c : cardToAttachSelection) {
      ImageView iv = new ImageView();
      iv.setImage((c instanceof StandardCardForm) ? new StandardCard((StandardCardForm) c) :
          new OrientCard(c));
      iv.setFitHeight(140);
      iv.setFitWidth(100);

      // Apply event handler to each card in choices
      iv.setOnMouseClicked(event -> {
        tentativeCardSelection = (Card) ((ImageView) event.getSource()).getImage();
        choicesHBox.getChildren().forEach((child) -> child.setEffect(null));
        ((ImageView) event.getSource()).setEffect(BLUE_GLOW_EFFECT);
        waterfallPane.lookupButton(ButtonType.FINISH).setDisable(false);
      });

      choicesHBox.getChildren().add(iv);
    }

    closeAllActionWindows();
    waterfallPaneSubtitle.setText("Select one of the following cards to attach.");
    waterfallPaneTitle.setText("Attach a card");
    waterfallPane.setContent(choicesHBox);
    waterfallPane.lookupButton(ButtonType.FINISH).setOnMouseClicked(event -> {
      player.getHandForm().purchasedCards().remove(tentativeCardSelection.getCardForm());
      satchelCard.setCardToAttach(tentativeCardSelection.getCardForm());
      waterfallPane.setVisible(false);
      waterfallPane.setDisable(true);
      function.accept(null);
    });
    waterfallPane.setVisible(true);
    waterfallPane.setDisable(false);
    waterfallPane.lookupButton(ButtonType.FINISH).setDisable(true);
  }

  private void purchaseReserveNobleCard(ReserveNobleCardForm cardPurchased,
                                        Consumer<Void> function) {
    final HBox choices = new HBox();
    choices.setSpacing(20);
    choices.setPadding(new Insets(10));

    final Set<NobleForm> nobleForms = gameBoardForm.availableNobles();
    if (!nobleForms.isEmpty()) {
      // Generate ImageView for each selectable noble
      for (NobleForm n : nobleForms) {
        addNobleToAcquiredNobleAlertPane(choices, n);
      }

      closeAllActionWindows();
      ((Label) acquiredNobleAlertPane.getHeader()).setText("Choose noble to reserve");
      acquiredNobleAlertPane.setContent(choices);
      acquiredNobleAlertPane.lookupButton(ButtonType.FINISH).setOnMouseClicked(event -> {
        cardPurchased.setNobleToReserve(tentativeNobleSelection.nobleForm);
        acquiredNobleAlertPane.setVisible(false);
        function.accept(null);
      });
      acquiredNobleAlertPane.setVisible(true);
      acquiredNobleAlertPane.setDisable(false);
      acquiredNobleAlertPane.lookupButton(ButtonType.FINISH).setDisable(true);
    } else {
      purchaseCard(new PurchaseCardForm(cardPurchased,
          new GemPaymentForm(cardPurchased.cost()
              .getDiscountedCost(player.getHandForm().gemDiscounts()), 0),
          player.getHandForm().reservedCards().contains(cardPurchased), null));
    }
  }

  private void addNobleToAcquiredNobleAlertPane(HBox choices, NobleForm n) {
    ImageView iv = new ImageView();
    iv.setImage(new Noble(n));
    iv.setFitHeight(150);
    iv.setFitWidth(150);

    // Apply event handler to each card in choices
    iv.setOnMouseClicked(event -> {
      tentativeNobleSelection = (Noble) ((ImageView) event.getSource()).getImage();
      choices.getChildren().forEach((child) -> child.setEffect(null));
      ((ImageView) event.getSource()).setEffect(BLUE_GLOW_EFFECT);
      acquiredNobleAlertPane.lookupButton(ButtonType.FINISH).setDisable(false);
    });
    choices.getChildren().add(iv);
  }

  /**
   * Sends a request to the server to purchase a card.
   *
   * @param purchaseCardForm form of purchase card
   */
  private void purchaseCard(PurchaseCardForm purchaseCardForm) {
    final HttpResponse<String> response =
        ServerCaller.purchaseCard(LobbyServiceCaller.getCurrentUserLobby(),
            LobbyServiceCaller.getCurrentUserAccessToken(), purchaseCardForm);

    System.out.println(response.getBody());

    // Close card menu
    cardActionMenu.setVisible(false);

    closeAllActionWindows();
    //updateBoard();
    acquireNobleAndCityCheck(response);
  }

  /**
   * Performs all necessary UI changes when a player reserves a card.
   */
  public void handleReserve() {
    // Get card to be reserved
    Card cardReserved = (Card) selectedCardView.getImage();

    int[] gemsInHand = GemsForm.costHashToArrayWithGold(player.getHandForm().gems());
    int totalGemsInHand = 0;

    for (int i : gemsInHand) {
      totalGemsInHand += i;
    }

    if (totalGemsInHand + 1 > 10 && gameBoardForm.availableGems().get(GemColor.GOLD) > 0) {
      closeAllActionWindows();
      tokenDiscarder =
          new TokenDiscarder(this, player.getHandForm().gems(), 1, this::handleReserve);
      tokenDiscarder.open();
      cardMatrix.setDisable(true);
      return;
    }

    ReserveCardForm reserveCardForm = new ReserveCardForm(cardReserved.getCardForm(), null, false);

    HttpResponse<String> response =
        ServerCaller.reserveCard(LobbyServiceCaller.getCurrentUserLobby(),
            LobbyServiceCaller.getCurrentUserAccessToken(), reserveCardForm);

    System.out.println(response.getBody());

    // Close card menu
    cardActionMenu.setVisible(false);

    closeAllActionWindows();
    //updateBoard();
    acquireNobleAndCityCheck(response);
  }

  public void handleReserve(GemsForm gemsToRemove) {
    // Get card to be reserved
    Card cardReserved = (Card) selectedCardView.getImage();

    ReserveCardForm reserveCardForm = null;

    for (int i = 0; i < 6; i++) {
      if (gemsToRemove.getOrDefault(GemColor.INT_CONVERSION_ARRAY.get(i), null) != null) {
        reserveCardForm =
            new ReserveCardForm(cardReserved.getCardForm(), GemColor.INT_CONVERSION_ARRAY.get(i),
                false);
        break;
      }
    }

    cardMatrix.setDisable(false);
    HttpResponse<String> response =
        ServerCaller.reserveCard(LobbyServiceCaller.getCurrentUserLobby(),
            LobbyServiceCaller.getCurrentUserAccessToken(), reserveCardForm);

    System.out.println(response.getBody());

    // Close card menu
    cardActionMenu.setVisible(false);

    closeAllActionWindows();
    updateBoard();
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
  private void handleDiscardGems(MouseEvent e) {
    tokenDiscarder.handleClick(e);
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
  private void handleClickMenuPopupSaveButton() {
    menuPopupPane.setDisable(true);
    final HttpResponse<String> response =
        ServerCaller.saveGame(LobbyServiceCaller.getCurrentUserLobby(),
            LobbyServiceCaller.getCurrentUserAccessToken());
    if (response.getStatus() == HttpStatus.OK) {
      System.out.printf("Saved game successfully with save game id: %s%n", response.getBody());
    } else {
      System.err.printf("Unable to save game\n%s%n", response.getBody());
    }
    menuPopupPane.setDisable(false);
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

    // TODO fix error with showing satchel cards when they've been purchased
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
//      if (isValidNobleVisit(GemsForm.costHashToArray(currentPlayer.hand().gemDiscounts()),
//          n.getCost())) {
//        activateAcquireNoblePrompt(iv);
//        // Only one noble can be acquired per turn
//        break;
//      } else {
      publicNoblesVBox.getChildren().add(iv);
//      }
    }
  }

  private boolean isValidNobleVisit(int[] playerBonuses, int[] nobleCost) {
    return IntStream.range(0, nobleCost.length).noneMatch(i -> playerBonuses[i] < nobleCost[i]);
  }

  @FXML
  public void handleAcquiredNoblesViewSelect(MouseEvent event) {
    PlayerForm requestedPlayer = null;

    // Determines which player's information is being requested
    try {
      for (PlayerForm p : gameBoardForm.players()) {
        if (LobbyServiceCaller.getCurrentUserid().equals(p.uid())) {
          requestedPlayer = p;
        }
      }
    } catch (NullPointerException e) {
      System.out.println("DEBUG: Failed to identify player");
      return;
    }

    if (requestedPlayer == null) {
      return;
    }

    List<Image> nobleImages = new ArrayList<Image>();
    for (NobleForm n : requestedPlayer.hand().visitedNobles()) {
      nobleImages.add(new Noble(n));
    }

    // Set the purchased pane's content to the card image grid
    GridPane nobleGrid = generateCardGrid(nobleImages, new int[] {180, 180, 3});
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
      requestedPlayer = player.getPlayerForm();
    } catch (NullPointerException e) {
      System.out.println("DEBUG: Failed to identify player");
      return;
    }

    // Aborts if no player data was found
    if (requestedPlayer == null) {
      System.out.println("DEBUG: Requested player data was null");
      return;
    }

    // Set summary label as player title
    playerSummaryUserLabel.setText(
        requestedPlayer.uid() + "'s Board\nPrestige points: " +
            requestedPlayer.hand().prestigePoints());

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
    Card cardToAdd;
    for (CardForm c : requestedPlayer.hand().reservedCards()) {
      if (c instanceof StandardCardForm) {
        cardToAdd = new StandardCard((StandardCardForm) c);
      } else {
        cardToAdd = new OrientCard(c);
      }

      requestedPlayerReservedCardImages.add(cardToAdd);
    }
    reservedSummary.getChildren().clear();
    reservedSummary.getChildren()
        .add(generateCardGrid(requestedPlayerReservedCardImages, new int[] {110, 160, 3}));

    // TODO: Fetch and apply the player's nobles as images
    List<Image> requestedPlayerNoblesImages = new ArrayList<>();
    for (NobleForm n : requestedPlayer.hand().visitedNobles()) {
      requestedPlayerNoblesImages.add(new Noble(n));
    }
    noblesSummary.getChildren()
        .add(generateCardGrid(requestedPlayerNoblesImages, new int[] {140, 140, 5}));

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

  private List<CardForm> fetchWaterfallSelection(CardLevelForm level) {
    final boolean canPurchaseSatchelCard =
        player.getHandForm().purchasedCards().stream().anyMatch(this::cardIsAttachable);
    // Fetch user's free card choices
    return gameBoardForm.cards().stream().flatMap(Collection::stream)
        .filter(c -> (c.level().equals(level))
            && (!(c instanceof SatchelCardForm) || canPurchaseSatchelCard))
        .collect(Collectors.toList());
  }

  @FXML
  private void displayWaterfallChoices(CardForm rootCard, CardLevelForm levelToTake,
                                       Consumer<Void> function) {
    final List<CardForm> waterfallSelection = fetchWaterfallSelection(levelToTake);

    // Generate HBox to display the free card choices to user
    HBox choicesHBox = new HBox();
    choicesHBox.setSpacing(5);
    choicesHBox.setPadding(new Insets(5));

    // Generate ImageView for each selectable card form
    for (CardForm c : waterfallSelection) {
      ImageView iv = new ImageView();
      iv.setImage((c instanceof StandardCardForm x) ? new StandardCard(x) : new OrientCard(c));
      iv.setFitHeight(140);
      iv.setFitWidth(100);

      // Apply event handler to each card in choices
      iv.setOnMouseClicked(event -> {
        tentativeCardSelection = (Card) ((ImageView) event.getSource()).getImage();
        choicesHBox.getChildren().forEach((child) -> child.setEffect(null));
        ((ImageView) event.getSource()).setEffect(BLUE_GLOW_EFFECT);
        waterfallPane.lookupButton(ButtonType.FINISH).setDisable(false);
      });

      choicesHBox.getChildren().add(iv);
    }

    closeAllActionWindows();
    waterfallPaneSubtitle.setText("Select one of the following cards to acquire.");
    waterfallPaneTitle.setText("Waterfall Effect!");
    waterfallPane.setContent(choicesHBox);
    waterfallPane.lookupButton(ButtonType.FINISH).setOnMouseClicked(event -> {
      final CardForm selectedCard = tentativeCardSelection.getCardForm();
      if (rootCard instanceof WaterfallCardForm w) {
        w.setCardToTake(selectedCard);
      } else {
        ((SatchelCardForm) rootCard).setFreeCardToTake(selectedCard);
      }
      waterfallPane.setVisible(false);
      waterfallPane.setDisable(true);
      if (selectedCard instanceof ReserveNobleCardForm c) {
        purchaseReserveNobleCard(c, f -> function.accept(null));
      } else if (selectedCard instanceof SatchelCardForm c) {
        if (levelToTake == CardLevelForm.TWO) {
          purchaseLevelOneSatchelCard(c,
              f -> displayWaterfallChoices(c, CardLevelForm.ONE, x -> function.accept(null)));
        } else {
          purchaseLevelOneSatchelCard(c, f -> function.accept(null));
        }
      } else {
        function.accept(null);
      }
    });
    waterfallPane.setVisible(true);
    waterfallPane.setDisable(false);
    waterfallPane.lookupButton(ButtonType.FINISH).setDisable(true);
  }

  public void acquireNobleAndCityCheck(HttpResponse<String> response) {
    final Set<NobleForm> validNobles;
    final Set<CityForm> validCities;

    // Fetch valid noble or cities choices
    if (response.getBody() != null && response.getStatus() == HttpStatus.OK) {
      if (response.getBody().contains("cost")) { // They're nobles
        final Type type = new TypeToken<Set<NobleForm>>() {
        }.getType();
        validNobles = Main.GSON.fromJson(response.getBody(), type);
        validCities = null;
      } else { // They're cities that can be claimed
        final Type type = new TypeToken<Set<CityForm>>() {
        }.getType();
        validCities = Main.GSON.fromJson(response.getBody(), type);
        validNobles = null;
      }
    } else {
      if (response.getBody() != null && response.getStatus() != HttpStatus.OK) {
        System.out.printf("DEBUG: Failed to get available nobles or cities\n\tResponse body: %s\n",
            response.getBody());
      }
      return;
    }

    // Generate HBox to display the choices
    final HBox choicesHBox = new HBox();
    choicesHBox.setSpacing(20);
    choicesHBox.setPadding(new Insets(10));

    if (validNobles != null) {
      // Generate ImageView for each selectable noble
      for (NobleForm n : validNobles) {
        addNobleToAcquiredNobleAlertPane(choicesHBox, n);
      }

      closeAllActionWindows();
      ((Label) acquiredNobleAlertPane.getHeader()).setText("Noble Acquired!");
      acquiredNobleAlertPane.setContent(choicesHBox);
      acquiredNobleAlertPane.lookupButton(ButtonType.FINISH).setOnMouseClicked(event -> {
        // Acquire the noble via the server
        final HttpResponse<String> nobleResponse =
            ServerCaller.claimNoble(LobbyServiceCaller.getCurrentUserLobby(),
                LobbyServiceCaller.getCurrentUserAccessToken(),
                new ClaimNobleForm(tentativeNobleSelection.nobleForm));
        // Close noble select screen
        acquiredNobleAlertPane.setVisible(false);
        // Call this method again to check for claimable cities
        acquireNobleAndCityCheck(nobleResponse);
      });
      acquiredNobleAlertPane.setVisible(true);
      acquiredNobleAlertPane.setDisable(false);
      acquiredNobleAlertPane.lookupButton(ButtonType.FINISH).setDisable(true);
    } else if (validCities != null) {
      // Generate ImageView for each selectable city
      for (CityForm c : validCities) {
        ImageView iv = new ImageView();
        iv.setImage(new City(c));
        iv.setFitHeight(200);
        iv.setFitWidth(300);

        // Apply event handler to each city in choices
        iv.setOnMouseClicked(event -> {
          tentativeCitySelection = (City) ((ImageView) event.getSource()).getImage();
          choicesHBox.getChildren().forEach((child) -> child.setEffect(null));
          ((ImageView) event.getSource()).setEffect(BLUE_GLOW_EFFECT);
          acquiredNobleAlertPane.lookupButton(ButtonType.FINISH).setDisable(false);
        });
        choicesHBox.getChildren().add(iv);
      }

      closeAllActionWindows();
      ((Label) acquiredNobleAlertPane.getHeader()).setText("City Acquired!");
      acquiredNobleAlertPane.setContent(choicesHBox);
      acquiredNobleAlertPane.lookupButton(ButtonType.FINISH).setOnMouseClicked(event -> {
        // Acquire the city via the server
        ServerCaller.claimCity(LobbyServiceCaller.getCurrentUserLobby(),
            LobbyServiceCaller.getCurrentUserAccessToken(),
            new ClaimCityForm(tentativeCitySelection.cityForm));
        // Close city select screen
        acquiredNobleAlertPane.setVisible(false);
      });
      acquiredNobleAlertPane.setVisible(true);
      acquiredNobleAlertPane.setDisable(false);
      acquiredNobleAlertPane.lookupButton(ButtonType.FINISH).setDisable(true);
    }
  }

  @FXML
  private void openTradingPostsMenu() {
    tradingPostsMenu.toggleVisibility();
  }
}