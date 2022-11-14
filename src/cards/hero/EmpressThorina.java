package cards.hero;

import cards.Card;
import cards.minion.MinionCard;
import game.GameTable;

public class EmpressThorina extends HeroCard {
    public EmpressThorina(Card card) {
        super(card);
    }

    @Override
    public void useAbilityOnRow(GameTable table, int affectedRow) {
        int maxHealth = 0;
        int maxHealthPosition = 0;

        // Get the card with the most health
        for (int position = 0; position < 5; position++) {
            MinionCard attackedCard = table.getCardFrom(affectedRow, position);
            if (attackedCard != null) {
                if (attackedCard.getHealth() > maxHealth) {
                    maxHealth = attackedCard.getHealth();
                    maxHealthPosition = position;
                }
            }
        }

        MinionCard maxHealthCard = table.deleteFromRow(affectedRow, maxHealthPosition);
        hasJustAttacked();
    }
}
