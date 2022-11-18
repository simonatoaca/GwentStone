package cards.environment;

import cards.Card;
import cards.minion.MinionCard;


public class Firestorm extends EnvironmentCard {
    public Firestorm(final Card card) {
        super(card);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useAbility(final MinionCard attackedCard) {
        attackedCard.setHealth(attackedCard.getHealth() - 1);
    }
}
