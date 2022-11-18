package cards.minion;

import cards.Card;
import cards.CardType;
import cards.RowPositionForCard;
import game.GameTable;

public class Miraj extends MinionCard {
    public Miraj(Card card) {
        super(card);
        hasSpecialAbility = true;
        rowPosition = RowPositionForCard.FRONT;
    }

    /**
     * {@inheritDoc}
     */
    public void useAbility(MinionCard card, GameTable table, int row) {
        int enemyCardHealth = card.getHealth();
        card.setHealth(this.health);
        this.health = enemyCardHealth;

        // Kill card if health is 0
        table.checkForZeroHealth(row);

        hasJustAttacked();
    }
}

