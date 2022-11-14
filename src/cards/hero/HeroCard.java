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

    protected boolean hasAttacked = false;
    protected final CardType type = CardType.HERO;

    public HeroCard(Card card) {
        mana = card.getMana();
        description = card.getDescription();
        colors = card.getColors();
        name = card.getName();
        health = 30;
    }

    public void useAbility(MinionCard attackedCard) {}
    public void useAbilityOnRow(GameTable table, int affectedRow) {
        for (int position = 0; position < 5; position++) {
            MinionCard attackedCard = table.getCardFrom(affectedRow, position);
            if (attackedCard != null) {
                useAbility(attackedCard);
            }
        }

        hasJustAttacked();
    }

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

    public CardType getType() {
        return type;
    }

    public void hasJustAttacked() {
        hasAttacked = true;
    }

    public void hasRighToAttackAgain() {
        hasAttacked = false;
    }

    public boolean canAttack() {
        return !hasAttacked;
    }

    @Override
    public String toString() {
        return "{\n"
                +  "mana="
                + mana
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
}
