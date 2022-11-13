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
        System.out.println("PUT " + newCard + "at coords " + numberOfRow + " " + cardsOnRow[numberOfRow]);
        cardsOnRow[numberOfRow]++;
    }

    public MinionCard deleteFromRow(int numberOfRow, int indexOfCard) {
        System.out.println("DELETE\n" + table[numberOfRow][indexOfCard]);
        MinionCard card = table[numberOfRow][indexOfCard];
        for (int position = indexOfCard; position < cardsOnRow[numberOfRow] - 1; position++) {
            table[numberOfRow][position] = table[numberOfRow][position + 1];
        }
        table[numberOfRow][cardsOnRow[numberOfRow] - 1] = null;
        cardsOnRow[numberOfRow]--;
        return card;
    }

    public void checkForZeroHealth(int row) {
        int position = 0;
        while (position < 5) {
            if (table[row][position] != null) {
                if (table[row][position].getHealth() <= 0) {
                    deleteFromRow(row, position);
                    continue;
                }
            }
            position++;
        }
    }

    public MinionCard getCardFrom(int row, int column) {
        return table[row][column];
    }

    public void unfreezeCardsOfPlayer(PlayerTurn playerTurn) {
        for (int col = 0; col < 5; col++) {
            if (playerTurn == PlayerTurn.PLAYER1) {
                if (table[2][col] != null) table[2][col].unfreezeCard();
                if (table[3][col] != null) table[3][col].unfreezeCard();
            } else {
                if (table[0][col] != null) table[0][col].unfreezeCard();
                if (table[1][col] != null) table[1][col].unfreezeCard();
            }
        }
    }
}
