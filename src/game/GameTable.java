package game;

import cards.Card;
import cards.minion.MinionCard;

public class GameTable {
    private final MinionCard[][] table;
    private final int[] cardsOnRow;

    public GameTable() {
        table = new MinionCard[4][5];
        cardsOnRow = new int[4];
    }

    public boolean isSpaceOnRow(int numberOfRow) {
        return cardsOnRow[numberOfRow] < 5;
    }

    public void addToRow(int numberOfRow, MinionCard newCard) {
        table[numberOfRow][cardsOnRow[numberOfRow]] = newCard;
        //System.out.println("PUT " + newCard + "at coords " + numberOfRow + " " + cardsOnRow[numberOfRow]);
        cardsOnRow[numberOfRow]++;
    }

    public void deleteFromRow(int numberOfRow, int indexOfCard) {
        table[numberOfRow][indexOfCard] = null;
        for (int position = indexOfCard; position < cardsOnRow[numberOfRow] - 1; position++) {
            table[numberOfRow][position] = table[numberOfRow][position + 1];
        }
        cardsOnRow[numberOfRow]--;
    }

    public MinionCard getCardFrom(int row, int column) {
        return table[row][column];
    }
}
