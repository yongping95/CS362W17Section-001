/*
  Author: Levi Willmeth
  Written for OSU CS362 - Software Engineering II
  Assignment 1 - Dominion
*/
package willmetl;
import java.util.*;

public class GameState{
  private final int defaultDeckSize = 100;
  private final int pileSize = 8;     // How many cards per pile
  // Initial currency cards in the bank
  private final int bankCopper = 60;
  private final int bankSilver = 40;
  private final int bankGold = 30;
  // Initial victory point cards in the bank
  private final int bankEstates = 24;
  private final int bankDuchies = 12;
  private final int bankProvinces = 12;
  // Initial kingdom cards in the bank
  private final int bankKingdomCards = 10;
  private final int bankCurses = 10;  // scales with players, 10, 20, 30, etc

  private ArrayList<Card> supply;
  public Player[] players;
  public int numPlayers = 0;
  public int playerTurn = 0;

  public GameState(){
    // shared cards that players can buy
    this.supply = new ArrayList<Card>(200);
    this.players = new Player[2];
    // Fill the shared deck with the starting cards
    for(int i=0; i<bankCopper; i++)       supply.add(Card.COPPER);
    for(int i=0; i<bankSilver; i++)       supply.add(Card.SILVER);
    for(int i=0; i<bankGold; i++)         supply.add(Card.GOLD);
    for(int i=0; i<bankEstates; i++)      supply.add(Card.ESTATE);
    for(int i=0; i<bankDuchies; i++)      supply.add(Card.DUCHY);
    for(int i=0; i<bankProvinces; i++)    supply.add(Card.PROVINCE);
    for(int i=0; i<bankCurses; i++)       supply.add(Card.CURSE);
    for(int i=0; i<bankKingdomCards; i++) supply.add(Card.ADVENTURER);
    for(int i=0; i<bankKingdomCards; i++) supply.add(Card.AMBASSADOR);
    for(int i=0; i<bankKingdomCards; i++) supply.add(Card.BARON);
    for(int i=0; i<bankKingdomCards; i++) supply.add(Card.COUNCILROOM);
    for(int i=0; i<bankKingdomCards; i++) supply.add(Card.CUTPURSE);
    for(int i=0; i<bankKingdomCards; i++) supply.add(Card.EMBARGO);
    for(int i=0; i<bankKingdomCards; i++) supply.add(Card.FEAST);
    for(int i=0; i<bankKingdomCards; i++) supply.add(Card.GARDENS);
    for(int i=0; i<bankKingdomCards; i++) supply.add(Card.GREAT_HALL);
    for(int i=0; i<bankKingdomCards; i++) supply.add(Card.MINE);
    for(int i=0; i<bankKingdomCards; i++) supply.add(Card.SALVAGER);
    for(int i=0; i<bankKingdomCards; i++) supply.add(Card.SMITHY);
    for(int i=0; i<bankKingdomCards; i++) supply.add(Card.VILLAGE);
  }

  public void addPlayer(String name, GameState game){
    Player a = new Player(name, game);
    players[this.numPlayers++] = a;
  }

  public boolean addCard(Card c){
    return supply.add(c);
  }

  public int countCard(Card c){
    return Collections.frequency(supply, c);
  }

  public void checkEndConditions(){
    int missingCards = 0;
    for(Card c: Card.values()){
      if(supply.contains(c) == false) missingCards++;
    }
    if(supply.contains(Card.PROVINCE)==false || missingCards>=3) endGame();
  }

  public Card takeCard(Card c){
    int i = supply.indexOf(c);
    if(i >= 0) return supply.remove(i);
    return null;
  }

  public void nextTurn(){
    players[playerTurn].newTurn();
    playerTurn = (playerTurn+1)%numPlayers;
    checkEndConditions();
  }

  public int listCards(){
    int i = 0;
    System.out.format(
      " # - %-15s %s  %s  %s\n", "Card Name", "Cost", "Qty", "Description"
    );
    for(Card c: Card.values()){
      String desc = c.getDesc();
      desc = desc.substring(0, Math.min(desc.length(), 50));
      System.out.format("%2d - %-15s %-5d %2d    %s\n",
        ++i, c, c.costsMoney, Collections.frequency(supply, c), desc
      );
    }
    return i;
  }

  private void endGame(){
    System.out.println("Game over!");
    for(Player p: players){
      System.out.format("%s amassed %d victory points.\n",
        p, p.countVictoryPoints()
      );
    }
    System.out.println("Thanks for playing!");
    System.exit(0);
  }
}
