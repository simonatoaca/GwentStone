package cards.minion;

import cards.Card;
import cards.CardType;
import cards.RowPositionForCard;

public class Berserker extends MinionCard {
    public Berserker(Card card) {
        super(card);
        rowPosition = RowPositionForCard.BACK;
    }
}
