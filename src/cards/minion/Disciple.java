package cards.minion;

import cards.Card;
import cards.RowPositionForCard;
import game.GameTable;

public class Disciple extends MinionCard {
    public Disciple(final Card card) {
        super(card);
        rowPosition = RowPositionForCard.BACK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useAbility(final MinionCard card, final GameTable table, final int row) {
        card.addHealth(2);

        // Kill card if health is 0
        table.checkForZeroHealth(row);

        hasJustAttacked();
    }
}
