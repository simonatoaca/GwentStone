package game;

import cards.minion.MinionCard;

public final class GameTable {
    private final MinionCard[][] table;
    private final int[] cardsOnRow;

    public GameTable() {
        table = new MinionCard[4][5];
        cardsOnRow = new int[4];
    }

    /**
     * Checks if there is space for one more card on a row
     * @param numberOfRow
     * @return - true if there is space, false otherwise
     */
    public boolean isSpaceOnRow(int numberOfRow) {
        return cardsOnRow[numberOfRow] < 5;
    }

    /**
     * Adds a new card at the end of the specified row.
     * It must be previously checked that there is space for that
     * @param numberOfRow
     * @param newCard
     */
    public void addToRow(int numberOfRow, MinionCard newCard) {
        table[numberOfRow][cardsOnRow[numberOfRow]] = newCard;
        cardsOnRow[numberOfRow]++;
    }

    /**
     * Deletes a card from a certain position and rearranges
     * the cards on that row by moving them to the left
     * @param numberOfRow
     * @param indexOfCard
     * @return - the card removed from the row
     */
    public MinionCard deleteFromRow(int numberOfRow, int indexOfCard) {
        MinionCard card = table[numberOfRow][indexOfCard];
        for (int position = indexOfCard; position < cardsOnRow[numberOfRow] - 1; position++) {
            table[numberOfRow][position] = table[numberOfRow][position + 1];
        }
        table[numberOfRow][cardsOnRow[numberOfRow] - 1] = null;
        cardsOnRow[numberOfRow]--;
        return card;
    }

    /**
     * Goes through a row in the table and deletes
     * the cards that have 0 health.
     * Usually called after an attack/use of ability
     * @param row
     */
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

    /**
     * Gets the card at a given position in the table
     * @param row
     * @param column
     * @return - The card at the specified position
     */
    public MinionCard getCardFrom(int row, int column) {
        return table[row][column];
    }

    /**
     * Unfreezes the cards on the rows belonging to the current player.
     * Usually called after a round is over
     * @param playerTurn
     */
    public void unfreezeCardsOfPlayer(PlayerTurn playerTurn) {
        for (int col = 0; col < 5; col++) {
            if (playerTurn == PlayerTurn.PLAYER1) {
                if (table[2][col] != null) {
                    table[2][col].unfreezeCard();
                }
                if (table[3][col] != null) {
                    table[3][col].unfreezeCard();
                }
            } else {
                if (table[0][col] != null) {
                    table[0][col].unfreezeCard();
                }
                if (table[1][col] != null) {
                    table[1][col].unfreezeCard();
                }
            }
        }
    }

    /**
     * Gives the cards on the rows belonging to the current player
     * the right to attack again.
     * Usually called after a round is over
     * @param playerTurn
     */
    public void cardsOfPlayerCanAttackAgain(PlayerTurn playerTurn) {
        for (int col = 0; col < 5; col++) {
            if (playerTurn == PlayerTurn.PLAYER1) {
                if (table[2][col] != null) {
                    table[2][col].hasRighToAttackAgain();
                }
                if (table[3][col] != null) {
                    table[3][col].hasRighToAttackAgain();
                }
            } else {
                if (table[0][col] != null) {
                    table[0][col].hasRighToAttackAgain();
                }
                if (table[1][col] != null) {
                    table[1][col].hasRighToAttackAgain();
                }
            }
        }
    }

    /**
     * Checks for a tank card on the front row of the enemy
     * @param attackerRow
     * @return - true if a tank card is found, false otherwise
     */
    public boolean attackedPlayerHasTankCard(int attackerRow) {
        // Look at the enemy front row
        int enemyFrontRow = (attackerRow == 2 || attackerRow == 3) ? 1 : 2;
        for (int col = 0; col < cardsOnRow[enemyFrontRow]; col++) {
            if (table[enemyFrontRow][col].isTank())
                return true;
        }
        return false;
    }
}
