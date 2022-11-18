package game;

import cards.Card;
import cards.Deck;
import cards.hero.HeroCard;
import fileio.CardInput;
import fileio.DecksInput;
import java.util.*;

public final class Player {
    private int numberOfWins = 0;
    private static int numberOfGamesPlayed = 0;
    private int mana = 0;
    private ArrayList<Deck> decks;
    private Deck currentDeck;
    private ArrayList<Card> cardsInHand;
    private HeroCard heroCard;

    /**
     * Loads the decks given in the input to this player
     * @param inputDecks
     */
    public void loadDecks(final DecksInput inputDecks) {
        decks = new ArrayList<>();

        for (ArrayList<CardInput> cards : inputDecks.getDecks()) {
            decks.add(new Deck(cards));
        }

        if (cardsInHand != null) {
            cardsInHand.clear();
        }
    }

    /**
     * Resets the player status before the beginning of a new game.
     * The decks loaded at the very start stay the same
     */
    public void reset() {
        mana = 0;
        currentDeck = null;
        heroCard = null;
        if (cardsInHand != null) {
            cardsInHand.clear();
        }
    }

    /**
     * Adds the first card available in the current deck of
     * the player to its hand, removing it from the deck.
     */
    public void addCardToHand() {
        if (cardsInHand == null) {
            cardsInHand = new ArrayList<>();
        }

        if (currentDeck.getCards().size() != 0) {
            Card newCard = currentDeck.getCards().remove(0);
            cardsInHand.add(newCard);
        }
    }

    /**
     * Loads the player's current deck using deep copy
     * so the changes don't reflect in the initial decks.
     * @param numberOfDeck
     * @param shuffleSeed
     */
    public void setCurrentDeck(final int numberOfDeck, final int shuffleSeed) {
        // Deep copy so the modifications don t affect the original decks
        currentDeck = new Deck(decks.get(numberOfDeck));

        // shuffle cards in deck
        Collections.shuffle(currentDeck.getCards(), new Random(shuffleSeed));
    }

    /**
     * Adds mana to this player at the start of a new round,
     * if the number of rounds already played is under 10.
     */
    public void addManaForNewRound() {
        if (numberOfGamesPlayed <= 10) {
            mana += numberOfGamesPlayed;
        }
    }

    /**
     * Used when a player wins to increment the number of wins.
     */
    public void incrementWins() {
        numberOfWins++;
    }

    /**
     * Subtract mana from this player.
     * Used after the player uses a card
     * @param manaSubtracted
     */
    public void subtractMana(final int manaSubtracted) {
        mana -= manaSubtracted;
    }

    public void setHeroCard(final CardInput card) {
        heroCard = (HeroCard) Card.getCardByType(new Card(card));
    }

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    public Deck getCurrentDeck() {
        return currentDeck;
    }

    public int getMana() {
        return mana;
    }

    public HeroCard getHeroCard() {
        return heroCard;
    }

    public int getNumberOfWins() {
        return numberOfWins;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public static void setNumberOfGamesPlayed(final int numberOfGamesPlayed) {
        Player.numberOfGamesPlayed = numberOfGamesPlayed;
    }

    public void setDecks(final ArrayList<Deck> decks) {
        this.decks = decks;
    }
}
