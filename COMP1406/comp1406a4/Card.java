package comp1406a4;

public abstract class Card implements Comparable<Card>{

  /* handy arrays for ranks and suits    */
  /* do NOT change these                 */
  public final static String[] RANKS = { "None", "Joker",
    "2", "3", "4", "5", "6", "7", "8", "9", "10",
    "Jack", "Queen", "King", "Ace"};
  public final static String[] SUITS = { "Diamonds",
    "Clubs", "Hearts", "Spades", "NONE"};


 protected String rank;
 protected String suit;


 /** Create a card with specified suit and rank
  *
  * @param suit is the suit of the card (must be a string from Card.SUITS)
  * @param rank is the rank of the card (must be a string from Card.RANKS)
  */
  public Card(String suit, String rank){
    this.rank = rank;
    this.suit = suit;
  }

  public String getRank(){ return this.rank; }
  public String getSuit(){ return this.suit; }



  /** the numerical representation of the rank of the current card
   *
   *  @return the numerical rank of this card
   */
  public abstract int getRankValue();

  @Override
  public final String toString(){
    // outputs a string representation of a card object
    if( this.rank.equals(Card.RANKS[1]) ){
      return Card.RANKS[1];   // Joker
    }
    return this.rank + ":" + this.suit;
  }


}
