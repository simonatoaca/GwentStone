package cards.minion;

import cards.Card;
import cards.CardType;
import cards.RowPositionForCard;
import game.GameTable;

public class TheCursedOne extends MinionCard {
    public TheCursedOne(Card card) {
        super(card);
        rowPosition = RowPositionForCard.BACK;
        hasSpecialAbility = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useAbility(MinionCard card, GameTable table, int row) {
        int cardHealth = card.getHealth();
        card.setHealth(card.getAttackDamage());
        card.setAttackDamage(cardHealth);

        // Kill card if health is 0
        table.checkForZeroHealth(row);

        hasJustAttacked();
    }
}
