package cards.minion;

import cards.Card;
import cards.CardType;
import cards.RowPositionForCard;
import fileio.CardInput;

public class Sentinel extends MinionCard {

    public Sentinel(Card card) {
        super(card);
        rowPosition = RowPositionForCard.BACK;
    }
}
