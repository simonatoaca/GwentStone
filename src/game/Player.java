package game;

import cards.Card;
import cards.Deck;
import cards.hero.HeroCard;
import cards.minion.MinionCard;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.CardInput;
import fileio.DecksInput;

import java.io.IOException;
import java.util.*;

public class Player {
    private int numberOfWins = 0;
    static private int numberOfGamesPlayed = 0;
    private int mana = 0;

    private ArrayList<Deck> decks;
    private Deck currentDeck;
    private ArrayList<Card> cardsInHand;
    private HeroCard heroCard;

    public void loadDecks(DecksInput inputDecks) {
        if (decks == null) {
            decks = new ArrayList<>();
        } else {
            decks.clear();
        }

        for (ArrayList<CardInput> cards : inputDecks.getDecks()) {
            decks.add(new Deck(cards));
        }

        if (cardsInHand != null) {
            cardsInHand.clear();
        }
    }

    public void addCardToHand() {
        if (cardsInHand == null)
            cardsInHand = new ArrayList<>();

        if (currentDeck.getCards().size() != 0) {
            Card newCard = currentDeck.getCards().remove(0);
            cardsInHand.add(newCard);
        }
    }

    public void setCurrentDeck(int numberOfDeck, int shuffleSeed) {
        // Deep copy so the modifications don t affect the original decks
        currentDeck = new Deck(decks.get(numberOfDeck));

        // shuffle cards in deck
        Collections.shuffle(currentDeck.getCards(), new Random(shuffleSeed));
    }

    public void addManaForNewRound() {
        if (numberOfGamesPlayed <= 10) {
            mana += numberOfGamesPlayed;
        }
    }

    public void addMana(int manaAdded) {
        mana += manaAdded;
    }

    public void subtractMana(int manaSubtracted) {
        mana -= manaSubtracted;
    }

    public void setHeroCard(CardInput card) {
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

    public static int getNumberOfGamesPlayed() {
        return numberOfGamesPlayed;
    }

    public int getNumberOfWins() {
        return numberOfWins;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public void setNumberOfWins(int numberOfWins) {
        this.numberOfWins = numberOfWins;
    }

    public static void setNumberOfGamesPlayed(int numberOfGamesPlayed) {
        Player.numberOfGamesPlayed = numberOfGamesPlayed;
    }

    public void setCardsInHand(ArrayList<Card> cardsInHand) {
        this.cardsInHand = cardsInHand;
    }

    public void setCurrentDeck(Deck currentDeck) {
        this.currentDeck = currentDeck;
    }

    public void setDecks(ArrayList<Deck> decks) {
        this.decks = decks;
    }
}
