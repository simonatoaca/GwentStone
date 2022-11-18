package cards;

import fileio.CardInput;
import java.util.ArrayList;
import java.util.List;

public final class Deck {
    private final int nrCardsInDeck;
    private final List<Card> cards;

    /**
     * Creates new deck from array of CardInput
     * @param deck
     */
    public Deck(final ArrayList<CardInput> deck) {
        nrCardsInDeck = deck.size();
        cards = new ArrayList<>();
        for (CardInput card : deck) {
            cards.add(new Card(card));
        }
    }

    /**
     * Deep copies a deck
     * @param deck
     */
    public Deck(final Deck deck) {
        nrCardsInDeck = deck.nrCardsInDeck;
        cards = new ArrayList<>();
        for (Card card : deck.getCards()) {
            cards.add(Card.getCardByType(new Card(card)));
        }
    }

    /**
     * Gets the cards in the current deck
     * @return - list of cards
     */
    public List<Card> getCards() {
        return cards;
    }
}
