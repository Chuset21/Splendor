package com.hexanome.fourteen.boards;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;

public class OrientExpansion implements Initializable {

  Bank bank;
  private ArrayList<Card> gameCards;
  private Stack<Card> level3Cards;
  private Stack<Card> level2Cards;
  private Stack<Card> level1Cards;
  private String selectedCardId;

  @FXML
  private Button takeBankButton;
  @FXML
  private Pane menuPopupPane;
  @FXML
  private Pane cardActionMenu;

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
  private List<Button> removeGemButtons;
  @FXML
  private List<Button> addGemButtons;

  public void goToGame(Stage aPrimaryStage) throws IOException {
    // Import root from fxml file
    Parent root = FXMLLoader.load(Objects.requireNonNull(
        OrientExpansion.class.getResource("OrientExpansionBoard1600x900.fxml")));
    // Set up root on stage (window)
    Scene aScene = new Scene(root, 1440, 810);

    // Initialize stage settings
    aPrimaryStage.setScene(aScene);
    aPrimaryStage.setTitle("Splendor");
    aPrimaryStage.setResizable(false);
    aPrimaryStage.centerOnScreen();

    aPrimaryStage.show();
  }

  // Use this function if you want to initialize nodes and their properties.
  // E.G. Button text, Labels positions, etc. etc.
  private void init() {
    // Set up bank
    bank = new Bank(4, addGemButtons, removeGemButtons, pGemLabels, bGemLabels, takeBankButton);

    // Set up game screen
    cardActionMenu.setVisible(false);

    // Set up cards
    setupCards("CardData.csv");
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    init();
  }

  private void setupCards(String pCardDataCSV){
    // Initialize a list of cards to use for the game
    gameCards = null;

    // Fill list with final cards
    try{
      gameCards = Card.setupCards(pCardDataCSV);
    } catch (IOException ioe){
      ioe.printStackTrace();
    }

    // Print cards being used (for testing)
    System.out.println(gameCards);

    for(ArrayList<ImageView> cl : cardViews){
      for(ImageView iv : cl){
        iv.setImage(null);
      }
    }
  }

  public void handleCardSelect(MouseEvent event) {
    selectedCard = (ImageView) event.getSource();
    //Input validation for null spaces
    if (selectedCard.getImage() == null) {
      return;
    }
    selectedCardId = selectedCard.getId();
    cardActionImage.setImage(selectedCard.getImage());
    cardActionMenu.setVisible(true);
  }

  public void handlePurchase() {
    Image cardPurchased = selectedCard.getImage();
    selectedCard.setImage(null);
    purchasedStack.setImage(cardPurchased);
    System.out.println("Got image:\n"+purchasedStack.getImage());
    cardActionMenu.setVisible(false);
  }

  public void handleReserve() {
    Image cardReserved = selectedCard.getImage();
    selectedCard.setImage(null);
    reservedStack.setImage(cardReserved);
    System.out.println("Got image:\n"+reservedStack.getImage());
    cardActionMenu.setVisible(false);
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



