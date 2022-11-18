package cards.minion;

import cards.Card;
import cards.CardType;
import cards.RowPositionForCard;

public class Goliath extends MinionCard {
    public Goliath(Card card) {
        super(card);
        rowPosition = RowPositionForCard.FRONT;
        isTank = true;
    }
}
