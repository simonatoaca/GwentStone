package cards.minion;

import cards.Card;
import cards.CardType;
import cards.RowPositionForCard;
import fileio.CardInput;

public class Sentinel extends MinionCard {
    private final RowPositionForCard rowPosition = RowPositionForCard.BACK;
    private final CardType type = CardType.MINION;

    public Sentinel(Card card) {
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
