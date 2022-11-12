package cards.minion;

import cards.Card;
import cards.RowPositionForCard;

public class TheRipper extends MinionCard {

    private final RowPositionForCard rowPosition = RowPositionForCard.FRONT;
    public TheRipper(Card card) {
        super(card);
    }

    public RowPositionForCard getRowPosition() {
        return rowPosition;
    }
}
