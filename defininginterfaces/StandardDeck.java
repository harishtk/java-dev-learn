package defininginterfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandardDeck implements Deck {
    private List<Card> entireDeck;
    
    public StandardDeck() {
        entireDeck = new ArrayList<>();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                entireDeck.add(new PlayingCard(rank, suit));
            }
        }
    }
   
    public List<Card> getCards() {
        return entireDeck;
    }
   
    public Deck getDeckFactory() {
        return new StandardDeck();
    }
    
    public int size() {
        return entireDeck.size();
    }
    
    public void addCard(Card card) {
        entireDeck.add(card);
    }
    
    public void addCards(List<Card> cards) {
        this.entireDeck.addAll(cards);
    }
    
    public void addDeck(Deck deck) {
        entireDeck.addAll(deck.getCards());
    }
    
    public void shuffle() {
        Collections.shuffle(entireDeck);
    }
    
    public void sort() {
        Collections.sort(entireDeck);
    }
    
    public void sort(Comparator<Card> c) {
        Collections.sort(entireDeck, c);
    }
    
    public String deckToString() {
        return entireDeck.toString();
    }
    
    public Map<Integer, Deck> deal(int players, int numberOfCards) throws IllegalArgumentException {
        int totalCards = players * numberOfCards;
        if (totalCards > entireDeck.size()) {
            throw new IllegalArgumentException("Not enough cards in the deck to deal " + totalCards + " cards");
        }
        
        Map<Integer, Deck> hands = new HashMap<>();
        for (int i = 0; i < players; i++) {
            Deck hand = getDeckFactory();
            for (int j = 0; j < numberOfCards; j++) {
                hand.addCard(entireDeck.remove(0));
            }
            hands.put(i, hand);
        }
        
        return hands;
    }
}
