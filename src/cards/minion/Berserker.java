package cards.minion;

import cards.Card;
import cards.RowPositionForCard;

public class Berserker extends MinionCard {
    private final RowPositionForCard rowPosition = RowPositionForCard.BACK;

    public Berserker(Card card) {
        super(card);
    }
    public RowPositionForCard getRowPosition() {
        return rowPosition;
    }
}
