package cards.minion;

import cards.Card;
import cards.CardType;
import cards.RowPositionForCard;
import game.GameTable;

public class Disciple extends MinionCard {
    public Disciple(Card card) {
        super(card);
        rowPosition = RowPositionForCard.BACK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useAbility(MinionCard card, GameTable table, int row) {
        card.addHealth(2);

        // Kill card if health is 0
        table.checkForZeroHealth(row);

        hasJustAttacked();
    }
}
