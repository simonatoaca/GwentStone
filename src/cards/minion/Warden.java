package cards.minion;

import cards.Card;
import cards.CardType;
import cards.RowPositionForCard;

public class Warden extends MinionCard {
    private final RowPositionForCard rowPosition = RowPositionForCard.FRONT;
    private final CardType type = CardType.MINION;

    public Warden(Card card) {
        super(card);
        isTank = true;
    }
    public RowPositionForCard getRowPosition() {
        return rowPosition;
    }

    @Override
    public CardType getType() {
        return type;
    }
}
