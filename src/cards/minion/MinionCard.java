package cards.minion;

import cards.Card;
import cards.CardType;
import cards.RowPositionForCard;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;

public class MinionCard extends Card {

    private boolean isFrozen = false;
    private final CardType type = CardType.MINION;

    private RowPositionForCard rowPosition;

    public MinionCard(Card card) {
        super(card);
    }

    @Override
    public ObjectNode getCardPrint() {
        return super.getCardPrint();
    }

    public void freezeCard() {
        isFrozen = true;
    }

    public void unfreezeCard() {
        isFrozen = false;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public CardType getType() {
        return type;
    }

    public RowPositionForCard getRowPosition() {
        return rowPosition;
    }
}
