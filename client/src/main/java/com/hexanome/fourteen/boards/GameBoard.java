package com.hexanome.fourteen.boards;

import com.hexanome.fourteen.ServerCaller;
import com.hexanome.fourteen.form.server.GameBoardForm;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.TokenRefreshFailedException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import com.hexanome.fourteen.lobbyui.*;

/**
 * A class to represent the game objects required to represent a OrientExpansion Splendor game.
 */
public class GameBoard {

  private Stage stage;

  public static GameBoardForm gb;
  Bank bank;
  public int numPlayers = 4;

  public static final int[] GEM_INDEX = {0, 1, 2, 3, 4, 5};

  // Player's Info
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

  // PURCHASED CARDS PANE
  @FXML
  private Pane purchasedCardsView;
  private List<Image> purchasedCardImages = new ArrayList<Image>();
  @FXML
  private BorderPane purchasedBorderPane;

  // RESERVED CARDS PANE
  @FXML
  private Pane reservedCardsView;
  private List<Image> reservedCardImages = new ArrayList<Image>();
  @FXML
  private BorderPane reservedBorderPane;


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
  private List<Label> pGemLabels;
  @FXML
  private List<Label> bGemLabels;
  @FXML
  private List<Label> actionGemLabels;
  @FXML
  private List<Button> removeGemButtons;
  @FXML
  private List<Button> addGemButtons;

  /**
   * A call to this method displays the game on screen by initializing the scene with the gameboard.
   *
   * @param stage the stage currently being used to hold JavaFX UI items
   * @throws IOException throw an exception if an illegal UI action is executed
   */
  public void goToGame(Stage stage) throws IOException {
    this.stage = stage;

    // Set up bank
    bank = new Bank(numPlayers, addGemButtons, removeGemButtons, pGemLabels, bGemLabels,
            takeBankButton);

    // Set up players
    player = new Player("0", "joebiden43");

    // Initialize the player's gems
    for (int idx : GEM_INDEX) {
      pGemLabels.get(idx).textProperty().set("" + player.getHand().gems[idx]);
    }

    // Set up game screen
    cardActionMenu.setVisible(false);
    purchasedCardsView.setVisible(false);

    // Set up cards
    setupCards("CardData.csv");

    gb = ServerCaller.getGameBoard(LobbyServiceCaller.getCurrentUserLobby());
    System.out.println(gb.playerTurnid());

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
    gameDecks.add(level3Cards = new Deck(3, Expansion.STANDARD, level3CardViewsBase));
    gameDecks.add(level2Cards = new Deck(2, Expansion.STANDARD, level2CardViewsBase));
    gameDecks.add(level1Cards = new Deck(1, Expansion.STANDARD, level1CardViewsBase));
    gameDecks.add(level3CardsOrient = new Deck(3, Expansion.ORIENT, level3CardViewsOrient));
    gameDecks.add(level2CardsOrient = new Deck(2, Expansion.ORIENT, level2CardViewsOrient));
    gameDecks.add(level1CardsOrient = new Deck(1, Expansion.ORIENT, level1CardViewsOrient));

    for (Card c : gameCards) {
      for (Deck d : gameDecks) {
        if (d.getLevel() == c.getLevel() && d.getExpansions() == c.getExpansion()) {
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

    // Store image of purchased card in player's purchase stack
    purchasedCardImages.add(selectedCard.getImage());

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

    // Store image of purchased card in player's purchase stack
    reservedCardImages.add(selectedCard.getImage());

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
    try{
      LobbyServiceCaller.deleteLaunchedSession();
      LobbyServiceCaller.setCurrentUserLobby(null);
    } catch(TokenRefreshFailedException e){
      try{
        MenuController.returnToLogin("Session timed out, retry login");
        stage.close();
      } catch(IOException ioe){
        ioe.printStackTrace();
      }
    }

    try{
      MenuController.goToWelcomeScreen();
    } catch (IOException ioe){
      ioe.printStackTrace();
    }
  }

  @FXML
  private void handleExitCardMenu() {
    selectedCard = null;
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
  public void handlePurchasedPaneSelect(MouseEvent event) {

    //Print to console all the player's purchased cards
    System.out.println("DEBUG: USER'S PURCHASED CARDS");
    Hand hand = player.getHand();
    for (int i = 0; i < hand.purchasedCards.size(); i++) {
      Card current = hand.purchasedCards.get(i);
      System.out.println(current.toString());
    }

    // Set the purchased pane's content to the card image grid
    purchasedBorderPane.setCenter(generateCardGrid(purchasedCardImages, new int[] {120, 170, 9}));

    // Open purchased pane
    purchasedCardsView.setVisible(true);
  }

  @FXML
  private void handleExitPurchaseOrReservePane() {
    purchasedCardsView.setVisible(false);
    reservedCardsView.setVisible(false);
  }

  @FXML
  public void handleReservedPaneSelect(MouseEvent event) {

    //Print to console all the player's purchased cards
    System.out.println("DEBUG: USER'S RESERVED CARDS");
    Hand hand = player.getHand();
    for (int i = 0; i < hand.reservedCards.size(); i++) {
      Card current = hand.reservedCards.get(i);
      System.out.println(current.toString());
    }

    // Set the purchased pane's content to the card image grid
    reservedBorderPane.setCenter(generateCardGrid(reservedCardImages,new int[] {200, 260, 3}));

    // Open purchased pane
    reservedCardsView.setVisible(true);
  }
}



