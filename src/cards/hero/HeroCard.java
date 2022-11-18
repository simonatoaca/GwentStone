package cards.hero;

import cards.Card;
import cards.CardType;
import cards.minion.MinionCard;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;
import game.GameTable;

import java.util.ArrayList;

public class HeroCard extends Card {
    protected boolean hasAttacked;

    public HeroCard(Card card) {
        mana = card.getMana();
        description = card.getDescription();
        colors = card.getColors();
        name = card.getName();
        health = 30;
        hasAttacked = false;
        type = CardType.HERO;
    }

    /**
     * {@inheritDoc}
     */
    public void useAbility(MinionCard attackedCard) { }

    /**
     * The hero card uses its ability on the row specified
     * @param table
     * @param affectedRow
     */
    public void useAbilityOnRow(GameTable table, int affectedRow) {
        for (int position = 0; position < 5; position++) {
            MinionCard attackedCard = table.getCardFrom(affectedRow, position);
            if (attackedCard != null) {
                useAbility(attackedCard);
            }
        }

        hasJustAttacked();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectNode getCardPrint() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("mana", mana);
        objectNode.put("description", description);

        ArrayNode colors = objectMapper.createArrayNode();
        for (String color : this.colors) {
            colors.add(color);
        }

        objectNode.set("colors", colors);
        objectNode.put("name", name);
        objectNode.put("health", health);

        return objectNode;
    }

    /**
     * Sets the hasAttacked field to true if the card has attacked
     * the current turn
     */
    public void hasJustAttacked() {
        hasAttacked = true;
    }

    /**
     * After the round is over, the card has the right to attack again
     */
    public void hasRighToAttackAgain() {
        hasAttacked = false;
    }

    /**
     * Returns whether the card can attack or not
     * @return - true if the card can attack, false otherwise
     */
    public boolean canAttack() {
        return !hasAttacked;
    }
}
