package comp1406a4;

public class StandardCard extends Card{

    // purpose: creates a card with given rank and suit 
    // preconditions: suit must be a string found in Card.SUITS 
    // rank must be a string found in Card.RANKS 
    // Note: If the rank is Card.RANKS[1] then any 
    // valid suit from Card.SUITS can be given 
    // but the card's suit will be set to Card.SUITS[4]

    public StandardCard(String rank, String suit) {
        super(suit, rank);
        if (this.rank.equals("Joker")){
            this.suit = Card.SUITS[4];
        }
    }


    // purpose: creates a card with the given rank and suit 
    // preconditions: suit must be a string found in Card.SUITS 
    // rank is an integer satisfying 1 <= rank <= 14, where 
    // 1 for joker, 2 for 2, 3 for 3, .., 10 for 10 
    // 11 for jack, 12 for queen, 13 for king, 14 for ace 
    // Note: as with the other constructor, if a joker is created, any valid suit can be passed 
    // but the card's suit will be set to Card.SUITS[4]

    public StandardCard(int rank, String suit) {
        super(suit, RANKS[rank]);
        if (this.rank.equals("Joker")){
            this.suit = Card.SUITS[4];
        }
    }

    @Override
    public int getRankValue(){
        int index = 0;

        for (int i = 0; i < RANKS.length; i++){
            if (this.rank.equals(RANKS[i])){
                index = i;
            }
        }

        return index;
    }

    public int getSuitValue(Card other){
        int index = 0;

        for (int i = 0; i < SUITS.length; i++){
            if (other.suit.equals(SUITS[i])){
                index = i;
            }
        }

        return index;
    }

    @Override
    public int compareTo(Card other){

        if (getSuitValue(this) > getSuitValue(other)){
            return 1;
        }else if(getSuitValue(this) < getSuitValue(other)){
            return -1;
        }else{
            if(this.getRankValue() > other.getRankValue()){
                return 1;
            }else if(this.getRankValue() < other.getRankValue()){
                return -1;
            }else{
                return 0;
            }
        }
    }
}