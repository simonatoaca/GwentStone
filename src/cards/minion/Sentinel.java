package cards.minion;

import cards.Card;
import cards.RowPositionForCard;

public class Sentinel extends MinionCard {

    public Sentinel(final Card card) {
        super(card);
        rowPosition = RowPositionForCard.BACK;
    }
}
