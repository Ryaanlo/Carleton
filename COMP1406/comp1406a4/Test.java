package comp1406a4;
import java.util.Random;
import java.util.Arrays;

public class Test{

    public static void main(String[] args){
        Random rnd = new Random();
        Card[] deck = new Card[40];

        for (int i = 0; i < deck.length; i++){
            deck[i] = new StandardCard(rnd.nextInt(14) + 1, Card.SUITS[rnd.nextInt(4)]);
        }

        Arrays.sort(deck);

        for (Card c : deck){
            System.out.println(c);
        }

    }
}