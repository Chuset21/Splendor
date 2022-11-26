package com.hexanome.fourteen.boards;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * A class to represent the game objects required to represent a OrientExpansion Splendor game.
 */
public class OrientExpansion implements Initializable {
  Bank bank;
  public int numPlayers = 4;

  public static final int[] GEM_INDEX = {0, 1, 2, 3, 4, 5};

  // Player's Info
  private int[] playerGems = new int[6]; // player's Gems (in Hand)

  private Player player;
  private ArrayList<Card> gameCards;
  private Deck level3Cards;
  private Deck level2Cards;
  private Deck level1Cards;
  private Deck level3CardsOrient;
  private Deck level2CardsOrient;
  private Deck level1CardsOrient;
  private ArrayList<Deck> gameDecks;
  private String selectedCardId;

  @FXML
  private Button takeBankButton;
  @FXML
  private Pane menuPopupPane;
  @FXML
  private Pane cardActionMenu;
  @FXML
  private Button cardReserveButton;
  @FXML
  private Button cardPurchaseButton;


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
  private ImageView selectedCard;
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
  private List<Label> gemLabels;
  @FXML
  private List<Label> bankGemLabels;
  @FXML
  private List<Label> actionGemLabels;
  @FXML
  private List<Button> removeGemButtons;
  @FXML
  private List<Button> addGemButtons;

  /**
   * A call to this method displays the game on screen by initializing the scene with the gameboard.
   *
   * @param primaryStage the stage currently being used to hold JavaFX UI items
   * @throws IOException throw an exception if an illegal UI action is executed
   */
  public void goToGame(Stage primaryStage) throws IOException {
    // Import root from fxml file
    Parent root = FXMLLoader.load(Objects.requireNonNull(
        OrientExpansion.class.getResource("OrientExpansionBoard1600x900.fxml")));
    // Set up root on stage (window)
    Scene scene = new Scene(root);

    // Initialize stage settings
    primaryStage.setScene(scene);
    primaryStage.setTitle("Splendor");
    primaryStage.setResizable(false);
    primaryStage.centerOnScreen();

    primaryStage.show();
  }

  // Use this function if you want to initialize nodes and their properties.
  // E.G. Button text, Labels positions, etc. etc.
  private void init() {

    // Set up bank
    bank = new Bank(numPlayers, addGemButtons, removeGemButtons, gemLabels, bankGemLabels,
        takeBankButton);

    // Set up players
    player = new Player("0", "joebiden43");

    // Initialize the player's gems
    for (int idx : GEM_INDEX) {
      gemLabels.get(idx).textProperty().set("" + playerGems[idx]);
    }

    // Set up game screen
    cardActionMenu.setVisible(false);

    // Set up cards
    setupCards("CardData.csv");

  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    init();
  }

  private void setupCards(String cardDatacsv) {
    // Initialize a list of cards to use for the game
    gameCards = null;

    // Fill list with final cards
    try {
      gameCards = Card.setupCards(cardDatacsv);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    // Clear all card views
    for (ArrayList<ImageView> cl : cardViews) {
      for (ImageView iv : cl) {
        iv.setImage(null);
      }
    }

    gameDecks = new ArrayList<>();
    gameDecks.add(level3Cards = new Deck(3, Expansions.BASEGAME, level3CardViewsBase));
    gameDecks.add(level2Cards = new Deck(2, Expansions.BASEGAME, level2CardViewsBase));
    gameDecks.add(level1Cards = new Deck(1, Expansions.BASEGAME, level1CardViewsBase));
    gameDecks.add(level3CardsOrient = new Deck(3, Expansions.ORIENT, level3CardViewsOrient));
    gameDecks.add(level2CardsOrient = new Deck(2, Expansions.ORIENT, level2CardViewsOrient));
    gameDecks.add(level1CardsOrient = new Deck(1, Expansions.ORIENT, level1CardViewsOrient));

    for (Card c : gameCards) {
      for (Deck d : gameDecks) {
        if (d.getLevel() == c.getLevel() && d.getExpansions() == c.getExpansions()) {
          d.push(c);
        }
      }
    }

    // This just prints the level 1 and 2 base game cards in their respective Decks
    //System.out.println(level1Cards+"\n"+level2Cards);

    for (int i = 0; i < 4; i++) {
      ((ImageView) cardViews.get(0).get(i)).setImage(level1Cards.pop());
    }
  }

  /**
   * A call to this method is made to determine the necessary action to do when a user uses the
   * mouse to perform an action.
   *
   * @param event a parameter with the MouseEvent to handle
   */
  public void handleCardSelect(MouseEvent event) {
    // Get imageview
    selectedCard = (ImageView) event.getSource();


    //Input validation for null spaces
    if (selectedCard.getImage() == null) {
      return;
    }

    // Get id of imageview
    selectedCardId = selectedCard.getId();

    // Set action pane image to the selected card
    cardActionImage.setImage(selectedCard.getImage());

    // Set values in action pane to the card's cost
    for (int i = 0; i < ((Card) selectedCard.getImage()).getCost().length; i++) {
      // Set label string to the respective cost of the card
      actionGemLabels.get(i).setText(((Card) selectedCard.getImage()).getCost()[i] + "");
    }

    // Set gold gems to 0 -> !!!can change this later when implementing gold purchases!!!
    actionGemLabels.get(5).setText("0");

    ///// Handle Purchase Availability (By default: is available)
    cardPurchaseButton.setDisable(false);

    // get the player's hand and the cost of the card
    Hand hand = player.getHand();
    int[] selectedCost = ((Card) selectedCard.getImage()).getCost();

    for (int i = 0; i < 5; i++) {
      //if (selectedCost[i] > pHand.Gems[i] + pHand.gemDiscounts[i]) {
      if (selectedCost[i] > hand.gems[i]) {
        cardPurchaseButton.setDisable(true);
        break;
      }
    }
    //// Handle Reserve Availability
    if (hand.reservedCards.size() < 3) {
      cardReserveButton.setDisable(false);
    } else {
      cardReserveButton.setDisable(true);
    }

    // Open menu
    cardActionMenu.setVisible(true);

  }

  /**
   * Performs all necessary UI changes when a player purchases a card.
   */
  public void handlePurchase() {

    // Get card to be purchased
    Card cardPurchased = (Card) selectedCard.getImage();


    // Clear imageview of purchased card
    selectedCard.setImage(null);

    // Refill imageview if a card is left in the deck
    for (Deck d : gameDecks) {
      if (d.hasCardSlot(selectedCard) && !d.empty()) {
        selectedCard.setImage(d.pop());
      }
    }

    // Put purchased card in inventory
    purchasedStack.setImage(cardPurchased);

    // Add purchased card to player's hand
    player.getHand().purchasedCards.add(cardPurchased);

    // Close card menu
    cardActionMenu.setVisible(false);
  }

  /**
   * Performs all necessary UI changes when a player reserves a card.
   */
  public void handleReserve() {
    // Get card to be purchased
    Card cardReserved = (Card) selectedCard.getImage();

    // Clear imageview of reserved card
    selectedCard.setImage(null);

    // Refill imageview if a card is left in the deck
    for (Deck d : gameDecks) {
      if (d.hasCardSlot(selectedCard) && !d.empty()) {
        selectedCard.setImage(d.pop());
      }
    }

    // Put reserved card in inventory
    reservedStack.setImage(cardReserved);

    // Add the card to the player's hand
    player.getHand().reservedCards.add(cardReserved);

    // Close card menu
    cardActionMenu.setVisible(false);
  }

  public void handleTakeGreenGemButton() {
    bank.takeGem(player.getHand().gems, 0);
  }

  public void handleReturnGreenGemButton() {
    bank.returnGem(player.getHand().gems, 0);
  }

  public void handleTakeWhiteGemButton() {
    bank.takeGem(player.getHand().gems, 1);
  }

  public void handleReturnWhiteGemButton() {
    bank.returnGem(player.getHand().gems, 1);
  }

  public void handleTakeBlueGemButton() {
    bank.takeGem(player.getHand().gems, 2);
  }

  public void handleReturnBlueGemButton() {
    bank.returnGem(player.getHand().gems, 2);
  }

  public void handleTakeBlackGemButton() {
    bank.takeGem(player.getHand().gems, 3);
  }

  public void handleReturnBlackGemButton() {
    bank.returnGem(player.getHand().gems, 3);
  }

  public void handleTakeRedGemButton() {
    bank.takeGem(player.getHand().gems, 4);
  }

  public void handleReturnRedGemButton() {
    bank.returnGem(player.getHand().gems, 4);
  }

  public void handleTakeYellowGemButton() {
    bank.takeGem(player.getHand().gems, 5);
  }

  public void handleReturnYellowGemButton() {
    bank.returnGem(player.getHand().gems, 5);
  }

  @FXML
  private void handleClickTakeBankButton() {
    bank.toggle();
  }

  @FXML
  private void handleClickMenuButton() {
    menuPopupPane.setVisible(true);
  }

  @FXML
  private void handleClickMenuPopupBackButton() {
    menuPopupPane.setVisible(false);
  }

  @FXML
  private void handleClickMenuPopupQuitButton() {
    Platform.exit();
    System.exit(0);
  }

  @FXML
  private void handleExitCardMenu() {
    selectedCard = null;
    cardActionMenu.setVisible(false);
  }

}



