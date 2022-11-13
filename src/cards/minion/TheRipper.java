package cards.minion;

import cards.Card;
import cards.CardType;
import cards.RowPositionForCard;

public class TheRipper extends MinionCard {

    private final RowPositionForCard rowPosition = RowPositionForCard.FRONT;
    private final CardType type = CardType.MINION;
    public TheRipper(Card card) {
        super(card);
    }

    public RowPositionForCard getRowPosition() {
        return rowPosition;
    }

    @Override
    public CardType getType() {
        return type;
    }
}
