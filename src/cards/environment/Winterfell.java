package cards.environment;

import cards.Card;
import cards.minion.MinionCard;


public class Winterfell extends EnvironmentCard {
    public Winterfell(final Card card) {
        super(card);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useAbility(final MinionCard attackedCard) {
        attackedCard.freezeCard();
    }
}
