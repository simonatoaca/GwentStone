package cards.minion;

import cards.Card;
import cards.CardType;
import cards.RowPositionForCard;
import game.GameTable;

public class Disciple extends MinionCard {
    private final RowPositionForCard rowPosition = RowPositionForCard.BACK;
    private final CardType type = CardType.MINION;
    public Disciple(Card card) {
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
        card.addHealth(2);

        // Kill card if health is 0
        table.checkForZeroHealth(row);

        hasJustAttacked();
    }
}
