package defininginterfaces;

public class PlayingCard implements Card {
    private final Card.Rank rank;
    private final Card.Suit suit;

    public PlayingCard(Card.Rank rank, Card.Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Card.Suit getSuit() {
        return suit;
    }

    public Card.Rank getRank() {
        return rank;
    }

    public String toString() {
        return rank + " of " + suit;
    }

    @Override
    public int hashCode() {
        return ((suit.value() - 1) * 13) + rank.value();
    }

    @Override
    public int compareTo(Card o) {
        return this.hashCode() - o.hashCode();
    }
}
