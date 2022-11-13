package cards.environment;

import cards.Card;
import cards.CardType;
import cards.minion.MinionCard;
import fileio.CardInput;

import java.util.ArrayList;

public class Firestorm extends EnvironmentCard {
    private int mana;
    private String description;
    private ArrayList<String> colors;
    private String name;

    private final CardType type = CardType.ENVIRONMENT;
    public Firestorm(Card card) {
        super(card);
        mana = card.getMana();
        description = card.getDescription();
        colors = card.getColors();
        name = card.getName();
    }

    @Override
    public CardType getType() {
        return type;
    }

    @Override
    public void useAbility(MinionCard attackedCard) {
        attackedCard.setHealth(attackedCard.getHealth() - 1);
    }

    @Override
    public int getMana() {
        return mana;
    }

    @Override
    public String getName() {
        return name;
    }
}
