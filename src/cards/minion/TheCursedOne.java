package cards.minion;

import cards.Card;
import cards.CardType;
import cards.RowPositionForCard;
import game.GameTable;

public class TheCursedOne extends MinionCard {
    private final RowPositionForCard rowPosition = RowPositionForCard.BACK;
    private final boolean hasSpecialAbility = true;
    private final CardType type = CardType.MINION;
    public TheCursedOne(Card card) {
        super(card);
    }

    public RowPositionForCard getRowPosition() {
        return rowPosition;
    }

    @Override
    public CardType getType() {
        return type;
    }

    public void useAbility(MinionCard card, GameTable table, int row) {
        int cardHealth = card.getHealth();
        card.setHealth(card.getAttackDamage());
        card.setAttackDamage(cardHealth);

        // Kill card if health is 0
        table.checkForZeroHealth(row);

        hasJustAttacked();
    }
}
