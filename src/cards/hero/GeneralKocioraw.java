package cards.hero;

import cards.Card;
import cards.minion.MinionCard;

public class GeneralKocioraw extends HeroCard {
    public GeneralKocioraw(Card card) {
        super(card);
    }

    public void useAbility(MinionCard card) {
        card.addAttackDamage(1);
    }
}
