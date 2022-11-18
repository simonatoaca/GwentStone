package cards.environment;

import cards.Card;
import cards.CardType;
import cards.minion.MinionCard;
import fileio.CardInput;

import java.util.ArrayList;

public class Firestorm extends EnvironmentCard {
    public Firestorm(Card card) {
        super(card);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useAbility(MinionCard attackedCard) {
        attackedCard.setHealth(attackedCard.getHealth() - 1);
    }
}
