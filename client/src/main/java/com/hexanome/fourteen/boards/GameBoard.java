package com.hexanome.fourteen.boards;

import com.hexanome.fourteen.GameServiceName;
import com.hexanome.fourteen.ServerCaller;
import com.hexanome.fourteen.form.server.GameBoardForm;
import com.hexanome.fourteen.form.server.GemsForm;
import com.hexanome.fourteen.form.server.PlayerForm;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.hexanome.fourteen.LobbyServiceCaller;
import com.hexanome.fourteen.TokenRefreshFailedException;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import com.hexanome.fourteen.lobbyui.*;

/**
 * A class to represent the game objects required to represent a OrientExpansion Splendor game.
 */
public class GameBoard {

  private Stage stage;

  private GameBoardForm gameBoardForm;

  private static final Map<String/*Player id*/, String/*Player Icon Filename*/> PLAYER_ID_MAP = new HashMap<>();
  private static final String[] DEFAULT_PLAYER_ICONS = {"cat.jpg","dog.jpg","squirrel.jpg","chameleon.jpg"};

  Bank bank;
  public int numPlayers = 4;

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
  private static final Effect currentPlayerEffect = new DropShadow(BlurType.GAUSSIAN, Color.web("#0048ff"), 24.5, 0, 0,0);

  private ArrayList<Card> gameCards;
  private ArrayList<Noble> gameNobles;
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
  @FXML
  private VBox publicNoblesVBox;


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
  @FXML
  private Pane takenTokenPane;
  @FXML
  private ArrayList<Label> takenGemLabels;

  /**
   * A call to this method displays the game on screen by initializing the scene with the gameboard.
   *
   * @param stage the stage currently being used to hold JavaFX UI items
   * @throws IOException throw an exception if an illegal UI action is executed
   */
  public void goToGame(Stage stage) throws IOException {
    this.stage = stage;

    // Get gameBoardForm
    gameBoardForm = ServerCaller.getGameBoard(LobbyServiceCaller.getCurrentUserLobby());

    setupPlayerMap();
    setupPlayers();

    // Set up bank
    bank = new Bank(players.size(), addGemButtons, removeGemButtons, takenGemLabels, bGemLabels,
            takeBankButton, takenTokenPane, this);

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

    // Setup nobles CSV data and display on board
    generateNobles();

  }

  public void updateBoard(){
    // Get gameBoardForm
    gameBoardForm = ServerCaller.getGameBoard(LobbyServiceCaller.getCurrentUserLobby());

    setupPlayers();

    // Set up game screen
    cardActionMenu.setVisible(false);
    purchasedCardsView.setVisible(false);
    takenTokenPane.setVisible(false);


    // Set up cards
    setupCards();

    // Setup nobles CSV data and display on board
    generateNobles();
  }


  private void setupCards(String cardDatacsv) {
    // Initialize a list of cards to use for the game
    gameCards = null;
    // Fill list with final cards
    //TODO: THIS IS TAKING FOREVER PLEASE FIX (@KAI)
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
   * Puts all visible cards on the board
   */
  private void setupCards() {
    if(gameBoardForm == null){
      throw new InvalidParameterException("gameBoardForm is null");
    }

  }

  /**
   * Statically maps all players to a single image
   */
  private void setupPlayerMap(){
    if(gameBoardForm == null){
      throw new InvalidParameterException("gameBoardForm is null");
    }

    int i = 0;

    for(PlayerForm player : gameBoardForm.players()){
      PLAYER_ID_MAP.put(player.uid(), User.class.getResource("images/" + DEFAULT_PLAYER_ICONS[i]).toString());
      i = (i + 1) % DEFAULT_PLAYER_ICONS.length;
    }
  }

  /**
   * Displays players to the board and generates list of players.
   * Current user will always be in position players.get(0)
   */
  private void setupPlayers(){
    if(gameBoardForm == null){
      throw new InvalidParameterException("gameBoardForm is null");
    }

    // Reset list of players
    players = new ArrayList<Player>();

    int i = 0;

    // Add all players to players list
    for(PlayerForm playerForm : gameBoardForm.players()){
      if(playerForm.uid().equals(LobbyServiceCaller.getCurrentUserid())){
        player = new Player(playerForm, PLAYER_ID_MAP.get(playerForm.uid()));

        if(i != 0){
          Player temp = players.get(0);
          players.set(0,player);
          players.add(temp);
        } else{
          players.add(player);
        }
      } else{
        players.add(new Player(playerForm, PLAYER_ID_MAP.get(playerForm.uid())));
      }
      i++;
    }

    // Place all players in their frames
    for(i = 0;i<playerViews.size();i++){
      if(i < players.size() && players.get(i) != null){
        ((ImageView) playerViews.get(i)).setImage(players.get(i));
        Tooltip.install(((ImageView) playerViews.get(i)), new Tooltip(players.get(i).getUserId() + (player.getUserId().equals(players.get(i).getUserId()) ? " (you)":"")));
      } else{
        ((ImageView) playerViews.get(i)).imageProperty().set(null);
      }
    }

    // Show whose turn it is
    displayCurrentPlayer();

    // Initialize the player's gems
    for (PlayerForm playerForm : gameBoardForm.players()) {
      if(playerForm.uid().equals(LobbyServiceCaller.getCurrentUserid())){
        for(i = 0;i<5;i++){
          pGemLabels.get(i).textProperty().set("" + GemsForm.costHashToArray(playerForm.hand().gems())[i]);
        }
      }
    }
  }

  private void displayCurrentPlayer(){
    if(gameBoardForm == null || playerViews == null){
      throw new InvalidParameterException("gameBoardForm nor playerViews can be null");
    }

    for(int i = 0;i<players.size();i++){
      if(players.get(i).getUserId().equals(gameBoardForm.playerTurnid())){
        currentPlayerTurnLabel.setText(players.get(i).getUserId()+"'s Turn");
        ((ImageView) playerViews.get(i)).setEffect(currentPlayerEffect);
      } else{
        ((ImageView) playerViews.get(i)).setEffect(null);
      }
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
    bank.takeGem( 0);
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

  @FXML
  public void generateNobles() {
    if(gameBoardForm == null){
      throw new InvalidParameterException("gameBoardForm is null");
    }

    // Create noble objects from CSV data
    gameNobles = Noble.interpretNobles(gameBoardForm);

    for (Noble n : gameNobles) {
      // Select a random noble from the gameNobles

      // Format the noble into a JavaFX Image
      ImageView iv = new ImageView();
      iv.setFitHeight(100);
      iv.setFitWidth(100);
      iv.setImage(n);

      //Add noble event handler
      iv.setOnMouseClicked(e -> handleImageViewClick(iv));

      //Add to board
      publicNoblesVBox.getChildren().add(iv);
    }
  }
  @FXML
  public void handleImageViewClick(ImageView iv) {
    System.out.println(iv.getImage());
  }
}