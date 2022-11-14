package cards.minion;

import cards.Card;
import cards.CardType;
import cards.RowPositionForCard;
import cards.hero.HeroCard;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;
import fileio.Coordinates;
import game.Game;
import game.GameTable;
import game.PlayerTurn;

public class MinionCard extends Card {

    protected boolean isFrozen = false;
    protected final CardType type = CardType.MINION;

    protected RowPositionForCard rowPosition;
    protected boolean hasSpecialAbility = false;
    protected boolean hasAttacked = false;
    protected boolean isTank = false;

    public MinionCard(Card card) {
        super(card);
    }

    @Override
    public ObjectNode getCardPrint() {
        return super.getCardPrint();
    }

    public void useAttack(MinionCard cardAttacked, GameTable table, int row) {
        cardAttacked.subtractHealth(this.getAttackDamage());

        // Kill card if health is 0
        table.checkForZeroHealth(row);

        hasJustAttacked();
    }

    public void useAttackHero(HeroCard heroAttacked) {
        heroAttacked.subtractHealth(this.getAttackDamage());
        hasJustAttacked();
    }

    public void freezeCard() {
        isFrozen = true;
    }

    public void unfreezeCard() {
        isFrozen = false;
    }

    public void useAbility(MinionCard card, GameTable table, int attackedCardCoordX) {}

    public boolean isFrozen() {
        return isFrozen;
    }

    public CardType getType() {
        return type;
    }

    public RowPositionForCard getRowPosition() {
        return rowPosition;
    }

    public boolean hasSpecialAbility() {
        return hasSpecialAbility;
    }

    public void subtractAttackDamage(int subtractedAttackDamage) {
        attackDamage -= subtractedAttackDamage;
        if (attackDamage < 0)
            attackDamage = 0;
    }

    static public boolean attackedCardBelongsToEnemy(int attackerCardX, int attackedCardX) {
        PlayerTurn attacker = (attackerCardX == 2 || attackerCardX == 3) ? PlayerTurn.PLAYER1 : PlayerTurn.PLAYER2;
        if (attacker == PlayerTurn.PLAYER1) {
            return attackedCardX != 2 && attackedCardX != 3;
        } else {
            return attackedCardX != 0 && attackedCardX != 1;
        }
    }

    public void hasJustAttacked() {
        hasAttacked = true;
    }

    public void hasRighToAttackAgain() {
        hasAttacked = false;
    }

    public boolean canAttack() {
        return !hasAttacked;
    }

    public boolean isTank() {
        return isTank;
    }
}
