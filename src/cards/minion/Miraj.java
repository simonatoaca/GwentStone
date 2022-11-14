package cards.minion;

import cards.Card;
import cards.CardType;
import cards.RowPositionForCard;
import game.GameTable;

public class Miraj extends MinionCard {
    private final RowPositionForCard  rowPosition = RowPositionForCard.FRONT;
    private final CardType type = CardType.MINION;
    private final boolean hasSpecialAbility = true;

    public Miraj(Card card) {
        super(card);
    }

    public RowPositionForCard getRowPosition() {
        return rowPosition;
    }

    @Override
    public CardType getType() {
        return type;
    }

    public void useAbility(MinionCard card, GameTable table, int row) {
        int enemyCardHealth = card.getHealth();
        card.setHealth(this.health);
        this.health = enemyCardHealth;

        // Kill card if health is 0
        table.checkForZeroHealth(row);

        hasJustAttacked();
    }
}

