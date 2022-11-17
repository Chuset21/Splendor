package com.hexanome.fourteen.boards;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Card extends Image {

  private static final ArrayList<Card> aInitCards = new ArrayList<>();

  private final HashMap<GemColors, Integer> aCost;
  private final GemColors aDiscountColor;
  private final int aDiscountAmt;
  private final Expansions aExpansion;
  private final int aLevel;
  private final int aVictoryPoints;

  public Card(HashMap<GemColors, Integer> pCost, GemColors pDiscountColor, int pDiscountAmt,
              Expansions pExpansion, int pLevel, int pVictoryPoints, File file) throws FileNotFoundException {
    super(new FileInputStream(file));

    aCost = new HashMap<>(pCost);
    aDiscountColor = pDiscountColor;
    aDiscountAmt = pDiscountAmt;
    aExpansion = pExpansion;
    aLevel = pLevel;
    aVictoryPoints = pVictoryPoints;
  }

  public static ArrayList<Card> getInitCards(){
    return new ArrayList<>(aInitCards);
  }

  public static void setupCards() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Profile Picture");
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*jpg"));
    File selectedFile = fileChooser.showOpenDialog(null);
    File selectedFileInput = selectedFile;

    //File selectedFile = new File(Objects.requireNonNull(Card.class.getClassLoader().getResource("images/0a.png").getFile()));
    System.out.println(selectedFile.getPath());


    Card c = null;
    if(selectedFile.exists()) {

      try {
        c = new Card(new HashMap<GemColors, Integer>(), null, 0, null, 0, 0, selectedFile);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    aInitCards.add(c);

    //System.out.println(Objects.requireNonNull(OrientExpansion.class.getResource("*.png")));
  }

  public String toString(){
    return "A card with image: "+super.toString();
  }
}
