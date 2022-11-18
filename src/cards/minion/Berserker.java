package cards.minion;

import cards.Card;
import cards.RowPositionForCard;

public class Berserker extends MinionCard {
    public Berserker(final Card card) {
        super(card);
        rowPosition = RowPositionForCard.BACK;
    }
}
