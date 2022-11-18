package cards.minion;

import cards.Card;
import cards.RowPositionForCard;

public class Goliath extends MinionCard {
    public Goliath(final Card card) {
        super(card);
        rowPosition = RowPositionForCard.FRONT;
        isTank = true;
    }
}
