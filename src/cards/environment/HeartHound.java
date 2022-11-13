package cards.environment;

import cards.Card;
import cards.CardType;
import cards.minion.MinionCard;
import game.GameTable;

import java.util.ArrayList;

public class HeartHound extends EnvironmentCard {
    private int mana;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private final CardType type = CardType.ENVIRONMENT;
    public HeartHound(Card card) {
        super(card);
        mana = card.getMana();
        description = card.getDescription();
        colors = card.getColors();
        name = card.getName();
    }

    @Override
    public CardType getType() {
        return type;
    }

    @Override
    public void useAbilityOnRow(int row, GameTable table) {
        int maxHealth = 0;
        int maxHealthPosition = 0;

        // Get the card with the most health
        for (int position = 0; position < 5; position++) {
            MinionCard attackedCard = table.getCardFrom(row, position);
            if (attackedCard != null) {
                if (attackedCard.getHealth() > maxHealth) {
                    maxHealth = attackedCard.getHealth();
                    maxHealthPosition = position;
                }
            }
        }

        int newRow = 0;
        switch (row) {
            case 0: newRow = 3;
            case 1: newRow = 2;
            case 2: newRow = 1;
            case 3: newRow = 0;
        }

        MinionCard maxHealthCard = table.deleteFromRow(row, maxHealthPosition);
        table.addToRow(newRow, maxHealthCard);
    }

    @Override
    public int getMana() {
        return mana;
    }

    @Override
    public String getName() {
        return name;
    }
}
