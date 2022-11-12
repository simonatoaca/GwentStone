package cards;

import fileio.CardInput;
import fileio.DecksInput;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private final int nrCardsInDeck;
    private final List<Card> cards;

    public Deck(ArrayList<CardInput> deck) {
        nrCardsInDeck = deck.size();
        cards = new ArrayList<>();
        for (CardInput card : deck) {
            cards.add(new Card(card));
        }
    }

    public Deck(Deck deck) {
        nrCardsInDeck = deck.nrCardsInDeck;
        cards = new ArrayList<>();
        for (Card card : deck.getCards()) {
            cards.add(Card.getCardByType(new Card(card)));
        }
    }

    public List<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return "{\n"
                + cards
                + '}';
    }
}
