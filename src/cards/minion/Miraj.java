package cards.minion;

import cards.Card;
import cards.RowPositionForCard;
import game.GameTable;

public class Miraj extends MinionCard {
    public Miraj(final Card card) {
        super(card);
        hasSpecialAbility = true;
        rowPosition = RowPositionForCard.FRONT;
    }

    /**
     * {@inheritDoc}
     */
    public void useAbility(final MinionCard card, final GameTable table,
                           final int row) {
        int enemyCardHealth = card.getHealth();
        card.setHealth(this.health);
        this.health = enemyCardHealth;

        // Kill card if health is 0
        table.checkForZeroHealth(row);

        hasJustAttacked();
    }
}

