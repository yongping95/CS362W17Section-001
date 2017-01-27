/*
  Author: Levi Willmeth
  Written for OSU CS362 - Software Engineering II
  Assignment 1 - Dominion
*/
package willmetl;
import java.util.*;

public class Player{
  // The Player class represents a single Dominion player
  private final boolean DEBUGGING = true;
  // Attributes for this class are private
  private final String playerName;
  private final int drawCards = 7;  // inital hand size
  private int remActions;
  private int remBuys;
  private int remMoney;

  private Deck hand;
  private Deck drawPile;
  private Deck discardPile;
  public GameState gameState;

  public Player(String pName, GameState game){
    // Constructor for the Player class - sets their name
    this.playerName = pName;
    this.drawPile = new Deck();         // not shared, empty
    this.discardPile = new Deck();      // not shared, empty
    this.hand = new Deck();             // not shared, empty
    this.gameState = game;
  }

  public int addMoney(int m){
    this.remMoney += m;
    return remMoney;
  }

  public int addActions(int a){
    this.remActions += a;
    return remActions;
  }

  public int addBuys(int b){
    this.remBuys += b;
    return remBuys;
  }

  public Card chooseHand(){
    System.out.println("Please choose a card:");
    seeHand();
    // Should allow them to select a card
    return hand.drawCard(1);
  }

  public int getActions(){
    return this.remActions;
  }

  public int getBuys(){
    return this.remBuys;
  }

  public int getDiscardSize(){
    return this.discardPile.getSize();
  }

  public int getDrawSize(){
    return this.discardPile.getSize();
  }

  public void seeHand(){
    // Display all cards in a player's hand
    this.hand.seeDeck();
  }

  public boolean shuffle(){
    // Shuffles discardPile and adds it _under_ the drawPile
    if(drawPile.getSize() > 0) return false;
    discardPile.shuffle();
    return drawPile.addAll(discardPile);
  }

  public void seeDeck(){
    if(DEBUGGING){
      System.out.println("Player "+this.playerName+"'s hand:");
      hand.seeDeck();
      System.out.println("Player "+this.playerName+"'s drawPile:");
      drawPile.seeDeck();
      System.out.println("Player "+this.playerName+"'s discardPile:");
      discardPile.seeDeck();
    }
  }

  public boolean discard(){
    // This player discards a random card from their hand
    int handsize = this.hand.getSize();
    // generate random number in range of hand size
    Card c = hand.drawCard(1);
    return discard(c);
  }
  public boolean discard(Card c){
    // Returns result of attempting to move a card from hand to discardPile
    return discardPile.addCard(hand.drawCard(c));
  }
  public boolean discard(Card c, Player target){
    // Target player discards a specific card
    return target.discard(c);
  }

  public Card draw(){
    return draw(drawPile, drawPile.drawCard());
  }
  public Card draw(Deck d, Card c){
    // Take a card from a deck and put it into your discardPile
    c = d.drawCard(c);
    discardPile.addCard(c);
    return c;
  }

  public void returnCardToShared(Card c){
    gameState.bankCards.addCard(c);
  }

  public void newTurn(){
    // Start every turn with a new, full hand and 1 action, 1 buy
    if(DEBUGGING) System.out.println("It's "+playerName+"'s turn:");
    remActions = 1;
    remBuys = 1;
    discardPile.addCard(hand.drawAll());
    // Add 5 new cards from the top of this player's drawPile deck
    for(int i=0; i<5; i++) hand.addCard(drawPile.drawCard());

  }

  public boolean playCard(Card card){
    return playCard(card, this);
  }
  public boolean playCard(Card card, Player target){
    if(card.costsAction==0 || this.remActions>1){
      if(DEBUGGING) System.out.println("Playing "+card);
      card.play(this);
    } else {
      System.out.println("You do not have an action to play "+card);
    }
    return true;
  }

}
