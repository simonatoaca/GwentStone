package cards;

import cards.environment.Firestorm;
import cards.environment.HeartHound;
import cards.environment.Winterfell;
import cards.hero.*;
import cards.minion.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;

import java.util.ArrayList;
public class Card {
    protected int mana;
    protected int attackDamage;
    protected int health;
    protected String description;
    protected ArrayList<String> colors;
    protected String name;
    protected CardType type;

    public Card() { };

    public Card(final CardInput card) {
        mana = card.getMana();
        attackDamage = card.getAttackDamage();
        health = card.getHealth();
        description = card.getDescription();
        colors = card.getColors();
        name = card.getName();
    }

    public Card(final Card card) {
        mana = card.getMana();
        attackDamage = card.getAttackDamage();
        health = card.getHealth();
        description = card.getDescription();
        colors = card.getColors();
        name = card.getName();
    }

    /**
     * Instantiates a new card depending on its type.
     * @param card
     * @return - a subclass of Card
     */
    public static Card getCardByType(final Card card) {
        switch (card.name) {
            case "Sentinel" -> {
                return new Sentinel(card);
            }
            case "Berserker" -> {
                return new Berserker(card);
            }
            case "Goliath" -> {
                return new Goliath(card);
            }
            case "Warden" -> {
                return new Warden(card);
            }
            case "Disciple" -> {
                return new Disciple(card);
            }
            case "The Ripper" -> {
                return new TheRipper(card);
            }
            case "Miraj" -> {
                return new Miraj(card);
            }
            case "The Cursed One" -> {
                return new TheCursedOne(card);
            }
            case "Firestorm" -> {
                return new Firestorm(card);
            }
            case "Winterfell" -> {
                return new Winterfell(card);
            }
            case "Heart Hound" -> {
                return new HeartHound(card);
            }
            case "Lord Royce" -> {
                return new LordRoyce(card);
            }
            case "Empress Thorina" -> {
                return new EmpressThorina(card);
            }
            case "King Mudface" -> {
                return new KingMudface(card);
            }
            case "General Kocioraw" -> {
                return new GeneralKocioraw(card);
            }
            default -> {
                return new Card(card);
            }
        }
    }

    /**
     * Builds the format of the card for Json output.
     * @return - the ObjectNode containing the card print
     */
    public ObjectNode getCardPrint() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("mana", mana);
        objectNode.put("attackDamage", attackDamage);
        objectNode.put("health", health);
        objectNode.put("description", description);

        ArrayNode colors = objectMapper.createArrayNode();
        for (String color : this.colors) {
            colors.add(color);
        }

        objectNode.set("colors", colors);
        objectNode.put("name", name);

        return objectNode;
    }

    /**
     * Increases the health of the currrent card
     * @param addedHealth
     */
    public void addHealth(final int addedHealth) {
        health += addedHealth;
    }

    /**
     * Increases the attackDamage of the currrent card
     * @param attackDamageAdded - the attack to be added
     */
    public void addAttackDamage(final int attackDamageAdded) {
        attackDamage += attackDamageAdded;
    }

    /**
     * Decreases the attackDamage of the current card
     * @param attackDamageSubtracted - the attack to be subtracted
     */
    public void subtractAttackDamage(final int subtractedAttackDamage) {
        attackDamage -= subtractedAttackDamage;
        if (attackDamage < 0) {
            attackDamage = 0;
        }
    }

    /**
     * Decreases the health of the current card.
     * @param subtractedHealth - the health to be subtracted
     */
    public void subtractHealth(final int subtractedHealth) {
        health -= subtractedHealth;
    }

    /**
     * Get this card's mana.
     * @return - mana
     */
    public int getMana() {
        return mana;
    }

    /**
     * Set this card's mana.
     * @param mana
     */
    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     * Get the attackDamage of this card.
     * @return
     */
    public int getAttackDamage() {
        return attackDamage;
    }

    /**
     * Set the attackDamage of this card.
     * @param attackDamage
     */
    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    /**
     * Get the health of this card.
     * @return
     */
    public int getHealth() {
        return health;
    }

    /**
     * Set the health of this card.
     * @param health
     */
    public void setHealth(final int health) {
        this.health = health;
    }

    /**
     * Get the description of the current card.
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the current card.
     * @param description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Get the colors of the current card.
     * @return
     */
    public ArrayList<String> getColors() {
        return colors;
    }

    /**
     * Set the colors of the current card.
     * @param colors
     */
    public void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    /**
     * Get the name of the current card.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the current card.
     * @param name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Get the type of the current card.
     * @return - the type (MINION, HERO, ENVIRONMENT)
     */
    public CardType getType() {
        return type;
    }
}
