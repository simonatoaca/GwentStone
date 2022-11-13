package cards.minion;

import cards.Card;
import cards.CardType;
import cards.RowPositionForCard;

public class Disciple extends MinionCard {
    private final RowPositionForCard rowPosition = RowPositionForCard.BACK;
    private final CardType type = CardType.MINION;
    public Disciple(Card card) {
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
