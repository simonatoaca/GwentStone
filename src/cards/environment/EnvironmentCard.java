package cards.environment;

import cards.Card;
import cards.CardType;
import cards.minion.MinionCard;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;
import game.GameTable;

import java.nio.charset.MalformedInputException;
import java.util.ArrayList;

public class EnvironmentCard extends Card {

    public EnvironmentCard(Card card) {
        mana = card.getMana();
        description = card.getDescription();
        colors = card.getColors();
        name = card.getName();
        type = CardType.ENVIRONMENT;
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

        return objectNode;
    }

    /**
     * The current card uses its ability on the card specified
     * @param attackedCard
     */
    public void useAbility(MinionCard attackedCard) {}

    /**
     * The card uses its ability on the row specified
     * @param row
     * @param table
     */
    public void useAbilityOnRow(int row, GameTable table) {
        for (int position = 0; position < 5; position++) {
            MinionCard attackedCard = table.getCardFrom(row, position);
            if (attackedCard != null) {
                useAbility(attackedCard);
            }
        }
        table.checkForZeroHealth(row);
    }
}
