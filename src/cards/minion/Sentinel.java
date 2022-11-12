package cards.minion;

import cards.Card;
import cards.RowPositionForCard;
import fileio.CardInput;

public class Sentinel extends MinionCard {
    private final RowPositionForCard rowPosition = RowPositionForCard.BACK;

    public Sentinel(Card card) {
        super(card);
    }

    public RowPositionForCard getRowPosition() {
        return rowPosition;
    }
}
