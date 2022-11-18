package cards.minion;

import cards.Card;
import cards.CardType;
import cards.RowPositionForCard;
import fileio.Coordinates;
import game.GameTable;

public class TheRipper extends MinionCard {
    public TheRipper(Card card) {
        super(card);
        rowPosition = RowPositionForCard.FRONT;
        hasSpecialAbility = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useAbility(MinionCard card, GameTable table, int row) {
        card.subtractAttackDamage(2);

        // Kill card if health is 0
        table.checkForZeroHealth(row);

        hasJustAttacked();
    }
}
