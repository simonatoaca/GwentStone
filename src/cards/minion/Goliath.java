package cards.minion;

import cards.Card;
import cards.RowPositionForCard;

public class Goliath extends MinionCard {
    private final RowPositionForCard rowPosition = RowPositionForCard.FRONT;

    public Goliath(Card card) {
        super(card);
    }

    public RowPositionForCard getRowPosition() {
        return rowPosition;
    }
}
