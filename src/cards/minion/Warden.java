package cards.minion;

import cards.Card;
import cards.RowPositionForCard;

public class Warden extends MinionCard {
    private final RowPositionForCard rowPosition = RowPositionForCard.FRONT;

    public Warden(Card card) {
        super(card);
    }
    public RowPositionForCard getRowPosition() {
        return rowPosition;
    }
}
