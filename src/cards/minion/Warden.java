package cards.minion;

import cards.Card;
import cards.RowPositionForCard;

public class Warden extends MinionCard {
    public Warden(Card card) {
        super(card);
        rowPosition = RowPositionForCard.FRONT;
        isTank = true;
    }
}
