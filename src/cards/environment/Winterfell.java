package cards.environment;

import cards.Card;
import cards.CardType;
import cards.minion.MinionCard;

import java.util.ArrayList;

public class Winterfell extends EnvironmentCard {
    public Winterfell(Card card) {
        super(card);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useAbility(MinionCard attackedCard) {
        attackedCard.freezeCard();
    }
}
