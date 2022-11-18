package cards.hero;

import cards.Card;
import cards.minion.MinionCard;

public class GeneralKocioraw extends HeroCard {
    public GeneralKocioraw(final Card card) {
        super(card);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useAbility(final MinionCard card) {
        card.addAttackDamage(1);
    }
}
