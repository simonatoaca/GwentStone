package cards.hero;

import cards.Card;
import cards.minion.MinionCard;

public class KingMudface extends HeroCard {

    public KingMudface(Card card) {
        super(card);
    }

    public void useAbility(MinionCard card) {
        card.addHealth(1);
    }
}
