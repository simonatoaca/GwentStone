package cards.environment;

import cards.Card;
import cards.CardType;
import cards.minion.MinionCard;

import java.util.ArrayList;

public class Winterfell extends EnvironmentCard {

    private int mana;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private final CardType type = CardType.ENVIRONMENT;
    public Winterfell(Card card) {
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
        attackedCard.freezeCard();
        System.out.println("FROZE CARD\n" + attackedCard);
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
