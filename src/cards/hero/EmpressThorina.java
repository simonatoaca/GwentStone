package cards.hero;

import cards.Card;
import cards.minion.MinionCard;
import game.GameTable;

public class EmpressThorina extends HeroCard {
    public EmpressThorina(final Card card) {
        super(card);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useAbilityOnRow(final GameTable table, final int affectedRow) {
        int maxHealth = 0;
        int maxHealthPosition = 0;

        // Get the card with the most health
        for (int position = 0; position < GameTable.COLUMNS; position++) {
            MinionCard attackedCard = table.getCardFrom(affectedRow, position);
            if (attackedCard != null) {
                if (attackedCard.getHealth() > maxHealth) {
                    maxHealth = attackedCard.getHealth();
                    maxHealthPosition = position;
                }
            }
        }

        MinionCard maxHealthCard = table.deleteFromRow(affectedRow, maxHealthPosition);
        hasJustAttacked();
    }
}
