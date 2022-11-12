package cards.environment;

import cards.Card;
import cards.CardType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;

import java.util.ArrayList;

public class EnvironmentCard extends Card {
    private int mana;
    private final String description;
    private final ArrayList<String> colors;
    private final String name;
    private final CardType type = CardType.ENVIRONMENT;

    public EnvironmentCard(Card card) {
        mana = card.getMana();
        description = card.getDescription();
        colors = card.getColors();
        name = card.getName();
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

        return objectNode;
    }

    public CardType getType() {
        return type;
    }
}
