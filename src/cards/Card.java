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

    private CardType type;

    public Card() {};

    public Card(CardInput card) {
        mana = card.getMana();
        attackDamage = card.getAttackDamage();
        health = card.getHealth();
        description = card.getDescription();
        colors = card.getColors();
        name = card.getName();
    }

    public Card(Card card) {
        mana = card.getMana();
        attackDamage = card.getAttackDamage();
        health = card.getHealth();
        description = card.getDescription();
        colors = card.getColors();
        name = card.getName();
    }

    static public Card getCardByType(Card card) {
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
        }
        return null;
    }

    @Override
    public String toString() {
        return "{\n"
                +  "mana="
                + mana
                +  ",\n attackDamage="
                + attackDamage
                + ",\n health="
                + health
                +  ",\n description='"
                + description
                + '\''
                + ",\n colors="
                + colors
                + ",\n name='"
                +  "\n"
                + name
                + '\''
                + '}';
    }

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

    public void addHealth(int addedHealth) {
        health += addedHealth;
    }

    public void subtractHealth(int subtractedHealth) {
        health -= subtractedHealth;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(final int health) {
        this.health = health;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public CardType getType() {
        return type;
    }
}
