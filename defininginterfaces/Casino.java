package defininginterfaces;

import java.util.Comparator;
import java.util.Map;

public class Casino {
    public static void main(String[] args) {
        Deck deck = new StandardDeck();
        deck.shuffle();
        System.out.println(deck.deckToString());
        deck.sort();
        System.out.println(deck.deckToString());

        deck.sort(
            Comparator.comparing(Card::getRank)
                .reversed()
                .thenComparing(Comparator.comparing(Card::getSuit))
        );

        for (Map.Entry<Integer, Deck> entry : deck.deal(4, 5).entrySet()) {
            System.out.println("Player " + entry.getKey() + " has " + entry.getValue().deckToString());
        }
    }
}
