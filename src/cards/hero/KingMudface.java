package cards.hero;

import cards.Card;
import cards.minion.MinionCard;

public class KingMudface extends HeroCard {

    public KingMudface(final Card card) {
        super(card);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useAbility(final MinionCard card) {
        card.addHealth(1);
    }
}
