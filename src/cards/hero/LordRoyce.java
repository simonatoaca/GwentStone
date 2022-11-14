package cards.hero;

import cards.Card;
import cards.minion.MinionCard;
import game.GameTable;

public class LordRoyce extends HeroCard {
    public LordRoyce(Card card) {
        super(card);
    }

    @Override
    public void useAbilityOnRow(GameTable table, int affectedRow) {
        int maxAttack = 0;
        int maxAttackPosition = 0;

        // Get the card with the most health
        for (int position = 0; position < 5; position++) {
            MinionCard attackedCard = table.getCardFrom(affectedRow, position);
            if (attackedCard != null) {
                if (attackedCard.getAttackDamage() > maxAttack) {
                    maxAttack = attackedCard.getAttackDamage();
                    maxAttackPosition = position;
                }
            }
        }

        MinionCard maxAttackCard = table.getCardFrom(affectedRow, maxAttackPosition);

        if (maxAttackCard == null)
            return;
        maxAttackCard.freezeCard();
        hasJustAttacked();
    }
}
