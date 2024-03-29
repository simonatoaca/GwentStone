package cards.environment;

import cards.Card;
import cards.minion.MinionCard;
import game.GameTable;


public class HeartHound extends EnvironmentCard {
    public HeartHound(final Card card) {
        super(card);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useAbilityOnRow(final int row, final GameTable table) {
        int maxHealth = 0;
        int maxHealthPosition = 0;

        // Get the card with the most health
        for (int position = 0; position < GameTable.COLUMNS; position++) {
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
            default: newRow = 0;
        }

        MinionCard maxHealthCard = table.deleteFromRow(row, maxHealthPosition);
        table.addToRow(newRow, maxHealthCard);
    }
}
