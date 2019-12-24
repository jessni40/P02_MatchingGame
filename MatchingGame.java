//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title: CalendarPrinter
// Files: MatchingGame.java
// Course: CS 300, FALL, 2019
//
// Author: Jessica Ni
// Email: jni29@wisc.edu
// Lecturer's Name: Gary Dahl
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Partner Name: (name of your pair programming partner)
// Partner Email: (email address of your programming partner)
// Partner Lecturer's Name: (name of your partner's lecturer)
//
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
// ___ Write-up states that pair programming is allowed for this assignment.
// ___ We have both read and understand the course Pair Programming Policy.
// ___ We have registered our team prior to the team registration deadline.
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully
// acknowledge and credit those sources of help here. Instructors and TAs do
// not need to be credited here, but tutors, friends, relatives, room mates,
// strangers, and others do. If you received no outside help from either type
// of source, then please explicitly indicate NONE.
//
// Persons: None (identify each person and describe their help in detail)
// Online Sources: None (identify each URL and describe their assistance in detail)
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////
import java.io.File;
import java.util.Random;
import processing.core.PApplet;
import processing.core.PImage;

public class MatchingGame {

  // Congratulations message
  private final static String CONGRA_MSG = "CONGRATULATIONS! YOU WON!";
  // Cards not matched message
  private final static String NOT_MATCHED = "CARDS NOT MATCHED. Try again!";
  // Cards matched message
  private final static String MATCHED = "CARDS MATCHED! Good Job!";
  // 2D-array which stores cards coordinates on the window display
  private final static float[][] CARDS_COORDINATES =
      new float[][] {{170, 170}, {324, 170}, {478, 170}, {632, 170}, {170, 324}, {324, 324},
          {478, 324}, {632, 324}, {170, 478}, {324, 478}, {478, 478}, {632, 478}};
  // Array that stores the card images filenames
  private final static String[] CARD_IMAGES_NAMES = new String[] {"apple.png", "ball.png",
      "peach.png", "redFlower.png", "shark.png", "yellowFlower.png"};


  private static PApplet processing; // PApplet object that represents
  // the graphic display window
  private static Card[] cards; // one dimensional array of cards
  private static PImage[] images = new PImage[CARD_IMAGES_NAMES.length]; // array of images of the
                                                                         // different cards
  private static Random randGen; // generator of random numbers
  private static Card selectedCard1; // First selected card
  private static Card selectedCard2; // Second selected card
  private static boolean winner; // boolean evaluated true if the game is won,
  // and false otherwise
  private static int matchedCardsCount; // number of cards matched so far
  // in one session of the game
  private static String message; // Displayed message to the display window

  /**
   * Defines the initial environment properties of this game as the program starts
   */
  public static void setup(PApplet processing) {
    // Set the color used for the background of the Processing window
    MatchingGame.processing = processing;
    // processing.background(252, 237, 243); // Baby pink color

    // load all image files as PImage object and store its reference into images[];
    for (int i = 0; i < CARD_IMAGES_NAMES.length; i++) {
      images[i] = processing.loadImage("images" + File.separator + CARD_IMAGES_NAMES[i]);
    }
    // Draw an image of an apple at the center of the screen
    // processing.image(images[2], processing.width / 2, processing.height / 2);
    // width [resp. height]: System variable of the processing library
    // that stores the width [resp. height] of the display window.
    initGame();
  }

  /**
   * Initializes the Game
   */
  public static void initGame() {
    randGen = new Random(Utility.getSeed());
    selectedCard1 = null;
    selectedCard2 = null;
    matchedCardsCount = 0;
    winner = false;
    message = "";
    int index;
    int size;

    // Stores cards
    cards = new Card[CARD_IMAGES_NAMES.length * 2]; // size 12
    int[] indexArray = new int[cards.length];

    for (int i = 0; i < cards.length; i++) {
      indexArray[i] = i;
    }

    size = cards.length;
    //
    boolean checkers[] =
        {false, false, false, false, false, false, false, false, false, false, false, false};
    for (int i = 0; i < size; i++) {
      index = randGen.nextInt(12);
      // if checkers[index] == true, it means a card was already assigned that coordinate so we do
      // randGen again
      while (checkers[index] == true) { // All are false in the first iteration and we continue to
                                        // make a card
        index = randGen.nextInt(12);
      }
      cards[i] = new Card(images[i % 6], CARDS_COORDINATES[index][0], CARDS_COORDINATES[index][1]);
      checkers[index] = true;
    }

  }

  /**
   * Callback method called each time the user presses a key
   */
  public static void keyPressed() {
    if (processing.key == 'N' || processing.key == 'n') {
      initGame();
    }
  }

  /**
   * Callback method draws continuously this application window display
   */
  public static void draw() {
    processing.background(245, 255, 250); // Mint color

    for (int i = 0; i < cards.length; i++) {
      // cards[i].setVisible(true);
      cards[i].draw();
    }

    displayMessage(message);
  }

  /**
   * Displays a given message to the display window
   * 
   * @param message to be displayed to the display window
   */
  // Provided method. Sets up where message will be displayed.
  public static void displayMessage(String message) {
    processing.fill(0);
    processing.textSize(20);
    processing.text(message, processing.width / 2, 50);
    processing.textSize(12);
  }

  /**
   * Checks whether the mouse is over a given Card
   * 
   * @return true if the mouse is over the storage list, false otherwise
   */
  public static boolean isMouseOver(Card card) {
    int mouseX = processing.mouseX;
    int mouseY = processing.mouseY;
    double cardX = card.getX();
    double cardY = card.getY();
    int width = card.getImage().width;
    int height = card.getImage().height;

    if (mouseY > cardY - height / 2 && mouseY < cardY + height / 2) { //Sets the 
      if (mouseX > cardX - width / 2 && mouseX < cardX + width / 2) {
        return true;
      }
    }

    return false;
  }

  /**
   * Callback method called each time the user presses the mouse
   */
  // Checks if the mouse is over a card, if so, set card to visible and select it
  public static void mousePressed() {
    if (winner) {
      return;
    }
    if (selectedCard1 != null && selectedCard2 != null) {
      selectedCard1.deselect();
      selectedCard2.deselect();
      message = "";
      if (!matchingCards(selectedCard1, selectedCard2)) {
        selectedCard1.setVisible(false);
        selectedCard2.setVisible(false);
      }
      selectedCard1 = null;
      selectedCard2 = null;

    } else {
      for (int i = 0; i < cards.length; i++) {
        Card card = cards[i];
        if (isMouseOver(card) && !card.isVisible()) {
          if (selectedCard1 == null) {
            selectedCard1 = card;
            card.select();
            card.setVisible(true);
          } else if (selectedCard2 == null) {
            selectedCard2 = card;
            card.select();
            card.setVisible(true);
            if (matchingCards(selectedCard1, selectedCard2)) {
              matchedCardsCount++;
              message = MATCHED;
              if (matchedCardsCount == 6) {
                winner = true;
                message = CONGRA_MSG;
              }
            } else {
              message = NOT_MATCHED;
            }
          }
        }
      }
    }

  }

  /**
   * Checks whether two cards match or not
   * 
   * @param card1 reference to the first card
   * @param card2 reference to the second card
   * @return true if card1 and card2 image references are the same, false otherwise
   */
  public static boolean matchingCards(Card card1, Card card2) {
    if (card1.getImage() == card2.getImage()) {
      return true;
    } else {
      return false;
    }
  }

  public static void main(String[] args) {

    Utility.runApplication(); // starts the application
  }
}

