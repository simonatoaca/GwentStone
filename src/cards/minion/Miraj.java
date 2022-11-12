package cards.minion;

import cards.Card;
import cards.RowPositionForCard;

public class Miraj extends MinionCard {
    private final RowPositionForCard  rowPosition = RowPositionForCard.FRONT;

    public Miraj(Card card) {
        super(card);
    }

    public RowPositionForCard getRowPosition() {
        return rowPosition;
    }
}
