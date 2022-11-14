package cards.minion;

import cards.Card;
import cards.CardType;
import cards.RowPositionForCard;
import fileio.Coordinates;
import game.GameTable;

public class TheRipper extends MinionCard {

    private final RowPositionForCard rowPosition = RowPositionForCard.FRONT;
    private final CardType type = CardType.MINION;
    private final boolean hasSpecialAbility = true;
    public TheRipper(Card card) {
        super(card);
    }

    public RowPositionForCard getRowPosition() {
        return rowPosition;
    }

    @Override
    public CardType getType() {
        return type;
    }

    @Override
    public void useAbility(MinionCard card, GameTable table, int row) {
        card.subtractAttackDamage(2);

        // Kill card if health is 0
        table.checkForZeroHealth(row);

        hasJustAttacked();
    }
}
