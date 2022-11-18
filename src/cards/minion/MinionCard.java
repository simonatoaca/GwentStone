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
    protected boolean isFrozen;
    protected RowPositionForCard rowPosition;
    protected boolean hasSpecialAbility;
    protected boolean hasAttacked;
    protected boolean isTank;

    /**
     * Creates a basic minion card, without special abilities.
     * @param card
     */
    public MinionCard(Card card) {
        super(card);
        isFrozen = false;
        hasSpecialAbility = false;
        hasAttacked = false;
        isTank = false;
        type = CardType.MINION;
    }

    /**
     * The current card attacks another card.
     * The attacked card's health decreases by the attackDamage
     * of the current one. If it reaches 0, the card is killed
     * @param cardAttacked
     * @param table
     * @param row - the row where the attacked card is placed
     */
    public void useAttack(MinionCard cardAttacked, GameTable table, int row) {
        cardAttacked.subtractHealth(this.getAttackDamage());

        // Kill card if health is 0
        table.checkForZeroHealth(row);

        hasJustAttacked();
    }

    /**
     * The current card attacks the hero of the opponent.
     * @param heroAttacked
     */
    public void useAttackHero(HeroCard heroAttacked) {
        heroAttacked.subtractHealth(this.getAttackDamage());
        hasJustAttacked();
    }

    /**
     * Freezes the current card for one turn
     */
    public void freezeCard() {
        isFrozen = true;
    }

    /**
     * Unfreezes the card after one turn
     */
    public void unfreezeCard() {
        isFrozen = false;
    }

    /**
     * The current card uses its ability on the card specified
     * @param card
     * @param table
     * @param attackedCardCoordX
     */
    public void useAbility(MinionCard card, GameTable table, int attackedCardCoordX) {}

    /**
     * Checks the frozen status of the current card
     * @return - true is the card is frozen, false otherwise
     */
    public boolean isFrozen() {
        return isFrozen;
    }

    /**
     * Returns if the card should be placed on the front or on the back row
     * @return - FRONT or BACK
     */
    public RowPositionForCard getRowPosition() {
        return rowPosition;
    }

    /**
     * Checks if the current card has a special ability
     * @return - true if the card has ability, false otherwise
     */
    public boolean hasSpecialAbility() {
        return hasSpecialAbility;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void subtractAttackDamage(int subtractedAttackDamage) {
       super.subtractAttackDamage(subtractedAttackDamage);
    }

    /**
     * Verifies if the attacked card belongs to the enemy
     * @param attackerCardX
     * @param attackedCardX
     * @return
     */
    public static boolean attackedCardBelongsToEnemy(int attackerCardX, int attackedCardX) {
        PlayerTurn attacker = (attackerCardX == 2 || attackerCardX == 3) ? PlayerTurn.PLAYER1 : PlayerTurn.PLAYER2;
        if (attacker == PlayerTurn.PLAYER1) {
            return attackedCardX != 2 && attackedCardX != 3;
        } else {
            return attackedCardX != 0 && attackedCardX != 1;
        }
    }

    /**
     * Sets the hasAttacked field to true if the card has attacked
     * the current turn
     */
    public void hasJustAttacked() {
        hasAttacked = true;
    }

    /**
     * After the round is over, the card has the right to attack again
     */
    public void hasRighToAttackAgain() {
        hasAttacked = false;
    }

    /**
     * Returns whether the card can attack or not
     * @return - true if the card can attack, false otherwise
     */
    public boolean canAttack() {
        return !hasAttacked;
    }

    /**
     * @return - true if the card is of type tank, false otherwise
     */
    public boolean isTank() {
        return isTank;
    }
}
