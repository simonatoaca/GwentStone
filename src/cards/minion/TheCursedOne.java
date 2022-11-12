package cards.minion;

import cards.Card;
import cards.RowPositionForCard;

public class TheCursedOne extends MinionCard {
    private final RowPositionForCard rowPosition = RowPositionForCard.BACK;
    public TheCursedOne(Card card) {
        super(card);
    }

    public RowPositionForCard getRowPosition() {
        return rowPosition;
    }
}
