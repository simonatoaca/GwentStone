package cards.minion;

import cards.Card;
import cards.RowPositionForCard;
import game.GameTable;

public class TheRipper extends MinionCard {
    public TheRipper(final Card card) {
        super(card);
        rowPosition = RowPositionForCard.FRONT;
        hasSpecialAbility = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useAbility(final MinionCard card, final GameTable table,
                           final int row) {
        card.subtractAttackDamage(2);

        // Kill card if health is 0
        table.checkForZeroHealth(row);

        hasJustAttacked();
    }
}
