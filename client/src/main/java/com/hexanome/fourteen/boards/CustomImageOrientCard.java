package com.hexanome.fourteen.boards;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class CustomImageOrientCard extends WritableImage {
  private final OrientCard cardData;

  public CustomImageOrientCard(Image image, OrientCard cardData) {
    this(createWritableImageFromImage(image), cardData);
  }

  private CustomImageOrientCard(WritableImage writableImage, OrientCard cardData) {
    super(writableImage.getPixelReader(), (int) writableImage.getWidth(),
        (int) writableImage.getHeight());
    this.cardData = cardData;
  }

  // Helper method to create a WritableImage from an Image.
  private static WritableImage createWritableImageFromImage(Image normalImage) {
    int width = (int) normalImage.getWidth();
    int height = (int) normalImage.getHeight();

    WritableImage writableImage = new WritableImage(width, height);
    PixelReader pixelReader = normalImage.getPixelReader();

    // Copy the pixels from the original image to the writable image.
    writableImage.getPixelWriter().setPixels(0, 0, width, height, pixelReader, 0, 0);

    return writableImage;
  }

  public OrientCard getCardData() {
    return cardData;
  }
}

