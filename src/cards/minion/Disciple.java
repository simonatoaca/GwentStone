package cards.minion;

import cards.Card;
import cards.RowPositionForCard;

public class Disciple extends MinionCard {
    private final RowPositionForCard rowPosition = RowPositionForCard.BACK;

    public Disciple(Card card) {
        super(card);
    }

    public RowPositionForCard getRowPosition() {
        return rowPosition;
    }
}
