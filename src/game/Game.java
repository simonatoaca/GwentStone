package game;

import cards.Card;
import cards.CardType;
import cards.RowPositionForCard;
import cards.environment.EnvironmentCard;
import cards.hero.HeroCard;
import cards.minion.MinionCard;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class Game {
    private static Game gameInstance;
    private int totalRoundsPlayed;
    private int totalGamesPlayed;
    private static Player player1;
    private static Player player2;
    private static GameTable table;
    private final Input inputData;
    private static ArrayNode output;
    private static PlayerTurn currentTurn;
    private static boolean gameEnd;

    private Game(Input inputData, ArrayNode output) {
        player1 = new Player();
        player2 = new Player();
        this.inputData = inputData;
        Game.output = output;
        gameEnd = false;
        totalGamesPlayed = 0;
    }

    /**
     * Loads the game state described in inputData into gameInstance
     */
    private void load() {
        totalRoundsPlayed = 0;
        player1.loadDecks(inputData.getPlayerOneDecks());
        player2.loadDecks(inputData.getPlayerTwoDecks());
    }

    /**
     * Clears info from the previous game and loads the player's current decks and
     * heroes, and sets which player has the first turn
     * @param inputData
     */
    private void setRoundInfo(StartGameInput inputData) {
        player1.reset();
        player2.reset();

        table = new GameTable();
        totalRoundsPlayed = 0;
        gameEnd = false;

        player1.setCurrentDeck(inputData.getPlayerOneDeckIdx(), inputData.getShuffleSeed());
        player2.setCurrentDeck(inputData.getPlayerTwoDeckIdx(), inputData.getShuffleSeed());

        player1.setHeroCard(inputData.getPlayerOneHero());
        player2.setHeroCard(inputData.getPlayerTwoHero());

        // Set turn
        if (inputData.getStartingPlayer() == 1) {
            currentTurn = PlayerTurn.PLAYER1;
        } else {
            currentTurn = PlayerTurn.PLAYER2;
        }
    }

    /**
     * The entry point for the game
     * Handles loading the game and going through the game's rounds (stored in GameInput)
     * @param inputData
     * @param output
     */
    public static void startGame(Input inputData, ArrayNode output) {
        // Created new instance so the game is new
        gameInstance = new Game(inputData, output);

        // Load the initial state of the game
        gameInstance.load();

        // Get through each round
        for (GameInput gameInput : inputData.getGames()) {
            gameInstance.setRoundInfo(gameInput.getStartGame());
            gameInstance.totalGamesPlayed++;

            Iterator<ActionsInput> actions = gameInput.getActions().iterator();

            // When this is even, a new round has started
            int turnNumber = 0;

            while (actions.hasNext()) {
                if (turnNumber % 2 == 0) {
                    player1.addCardToHand();
                    player2.addCardToHand();

                    Player.setNumberOfGamesPlayed(++gameInstance.totalRoundsPlayed);

                    // Update mana
                    player1.addManaForNewRound();
                    player2.addManaForNewRound();
                }
                playTurn(actions);
                turnNumber++;
            }
        }
    }

    /**
     * Handles the actions during a player's turn
     * When "endPlayerTurn" is encountered, it is time for the other player's turn
     * @param actions - the actions in the round, starting where the last turn ended
     */
    public static void playTurn(Iterator<ActionsInput> actions) {
        Player currentPlayer = (currentTurn == PlayerTurn.PLAYER1) ? player1 : player2;

        // get actions until end turn or end of actions
        ActionsInput currentAction;
        do {
            currentAction = actions.next();
            if (Objects.equals(currentAction.getCommand(), "endPlayerTurn")) {
                table.unfreezeCardsOfPlayer(currentTurn);
                table.cardsOfPlayerCanAttackAgain(currentTurn);
                currentPlayer.getHeroCard().hasRighToAttackAgain();

                // Switch turn
                currentTurn = (currentTurn == PlayerTurn.PLAYER1) ? PlayerTurn.PLAYER2 : PlayerTurn.PLAYER1;
                break;
            }

            // action handler
            actionHandler(currentAction);
        } while (actions.hasNext());
    }

    /**
     * Takes the appropiate action depending on the currentAction
     * @param currentAction - the action to be handled
     */
    public static void actionHandler(ActionsInput currentAction) {
        switch (currentAction.getCommand()) {
            case "getPlayerDeck" -> getPlayerDeck(currentAction);
            case "getPlayerTurn" -> getPlayerTurn();
            case "getPlayerHero" -> getPlayerHero(currentAction);
            case "placeCard" -> placeCard(currentAction);
            case "getCardsInHand" -> getCardsInHand(currentAction);
            case "getEnvironmentCardsInHand" -> getEnvironmentCardsInHand(currentAction);
            case "getCardsOnTable" -> getCardsOnTable();
            case "getPlayerMana" -> getPlayerMana(currentAction);
            case "getCardAtPosition" -> getCardAtPosition(currentAction);
            case "useEnvironmentCard" -> useEnvironmentCard(currentAction);
            case "getFrozenCardsOnTable" -> getFrozenCardsOnTable(currentAction);
            case "cardUsesAttack" -> cardUsesAttack(currentAction);
            case "cardUsesAbility" -> cardUsesAbility(currentAction);
            case "useAttackHero" -> useAttackHero(currentAction);
            case "useHeroAbility" -> useHeroAbility(currentAction);
            case "getTotalGamesPlayed" -> getTotalGamesPlayed();
            case "getPlayerOneWins" -> getPlayerOneWins();
            case "getPlayerTwoWins" -> getPlayerTwoWins();
        }
    }

    /**
     *  The hero of the current player uses its ability on the row of cards
     *  specified in currentAction
     *  Errors are printed to output
     * @param currentAction - the action that triggered this response
     */
    public static void useHeroAbility(ActionsInput currentAction) {
        if (gameEnd) { return; }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", "useHeroAbility");
        objectNode.put("affectedRow", currentAction.getAffectedRow());

        Player player = (currentTurn == PlayerTurn.PLAYER1) ? player1 : player2;
        HeroCard heroCard = player.getHeroCard();

        if (player.getMana() < heroCard.getMana()) {
            objectNode.put("error", "Not enough mana to use hero's ability.");
            output.add(objectNode);
            return;
        }

        if (!heroCard.canAttack()) {
            objectNode.put("error", "Hero has already attacked this turn.");
            output.add(objectNode);
            return;
        }

        int affectedRow = currentAction.getAffectedRow();

        if (Objects.equals(heroCard.getName(), "Lord Royce") ||
                Objects.equals(heroCard.getName(), "Empress Thorina")) {
            if ((currentTurn == PlayerTurn.PLAYER1 && affectedRow > 1) ||
                    ((currentTurn == PlayerTurn.PLAYER2 && affectedRow < 2))) {
                objectNode.put("error", "Selected row does not belong to the enemy.");
                output.add(objectNode);
                return;
            }
        } else {
            if ((currentTurn == PlayerTurn.PLAYER1 && affectedRow < 2) ||
                    ((currentTurn == PlayerTurn.PLAYER2 && affectedRow > 1))) {
                objectNode.put("error", "Selected row does not belong to the current player.");
                output.add(objectNode);
                return;
            }
        }

        heroCard.useAbilityOnRow(table, affectedRow);
        player.subtractMana(heroCard.getMana());
    }

    /**
     * The hero of the player specified in currentAction is attacked
     * by one of the opponent's cards, also specified in currentAction
     * Errors are printed to output
     * @param currentAction - the action that triggered this response
     */
    public static void useAttackHero(ActionsInput currentAction) {
        if (gameEnd) return;
        Coordinates cardAttackerCoords = currentAction.getCardAttacker();
        MinionCard cardAttacker = table.getCardFrom(cardAttackerCoords.getX(), cardAttackerCoords.getY());
        Player attackedPlayer = (cardAttackerCoords.getX() < 2) ? player1 : player2;

        HeroCard heroAttacked = attackedPlayer.getHeroCard();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();


        objectNode.put("command", "useAttackHero");

        ObjectNode objectNode1 = objectMapper.createObjectNode();
        objectNode1.put("x", cardAttackerCoords.getX());
        objectNode1.put("y", cardAttackerCoords.getY());

        objectNode.set("cardAttacker", objectNode1);

        if (cardAttacker.isFrozen()) {
            objectNode.put("error", "Attacker card is frozen.");
            output.add(objectNode);
            return;
        }

        if (!cardAttacker.canAttack()) {
            objectNode.put("error", "Attacker card has already attacked this turn.");
            output.add(objectNode);
            return;
        }

        if (table.attackedPlayerHasTankCard(cardAttackerCoords.getX())) {
            objectNode.put("error", "Attacked card is not of type 'Tank'.");
            output.add(objectNode);
            return;
        }

        cardAttacker.useAttackHero(heroAttacked);

        if (heroAttacked.getHealth() <= 0) {
            ObjectNode objectNode2 = objectMapper.createObjectNode();
            String message = "Player " + ((cardAttackerCoords.getX() < 2) ? "two" : "one") + " killed the enemy hero.";
            objectNode2.put("gameEnded", message);
            output.add(objectNode2);
            gameEnd = true;
            Player winner = (cardAttackerCoords.getX() < 2) ? player2 : player1;
            winner.incrementWins();
        }
    }

    /**
     * The specified card uses its ability on another card (also specified)
     * Errors are printed to output
     * @param currentAction - the action that triggered this response
     */
    public static void cardUsesAbility(ActionsInput currentAction) {
        if (gameEnd) return;
        Coordinates cardAttackerCoords = currentAction.getCardAttacker();
        Coordinates cardAttackedCoords = currentAction.getCardAttacked();
        MinionCard cardAttacker = table.getCardFrom(cardAttackerCoords.getX(), cardAttackerCoords.getY());
        MinionCard cardAttacked = table.getCardFrom(cardAttackedCoords.getX(), cardAttackedCoords.getY());

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", "cardUsesAbility");

        ObjectNode objectNode1 = objectMapper.createObjectNode();
        objectNode1.put("x", cardAttackerCoords.getX());
        objectNode1.put("y", cardAttackerCoords.getY());

        objectNode.set("cardAttacker", objectNode1);

        ObjectNode objectNode2 = objectMapper.createObjectNode();
        objectNode2.put("x", cardAttackedCoords.getX());
        objectNode2.put("y", cardAttackedCoords.getY());

        objectNode.set("cardAttacked", objectNode2);

        if (cardAttacker.isFrozen()) {
            objectNode.put("error", "Attacker card is frozen.");
            output.add(objectNode);
            return;
        }

        if (!cardAttacker.canAttack()) {
            objectNode.put("error", "Attacker card has already attacked this turn.");
            output.add(objectNode);
            return;
        }

        if (Objects.equals(cardAttacker.getName(), "Disciple")) {
            if (MinionCard.attackedCardBelongsToEnemy(cardAttackerCoords.getX(), cardAttackedCoords.getX())) {
                objectNode.put("error", "Attacked card does not belong to the current player.");
                output.add(objectNode);
                return;
            }
        } else {
            if (!MinionCard.attackedCardBelongsToEnemy(cardAttackerCoords.getX(), cardAttackedCoords.getX())) {
                objectNode.put("error", "Attacked card does not belong to the enemy.");
                output.add(objectNode);
                return;
            }
            if (table.attackedPlayerHasTankCard(cardAttackerCoords.getX()) && !cardAttacked.isTank()) {
                objectNode.put("error", "Attacked card is not of type 'Tank'.");
                output.add(objectNode);
                return;
            }
        }

        if (cardAttacked == null) {
            return;
        }

        cardAttacker.useAbility(cardAttacked, table, cardAttackedCoords.getX());
    }

    /**
     * The specified card attacks another card (also specified)
     * Errors are printed to output
     * @param currentAction - the action that triggered this response
     */
    public static void cardUsesAttack(ActionsInput currentAction) {
        if (gameEnd) return;
        Coordinates cardAttackerCoords = currentAction.getCardAttacker();
        Coordinates cardAttackedCoords = currentAction.getCardAttacked();
        MinionCard cardAttacker = table.getCardFrom(cardAttackerCoords.getX(), cardAttackerCoords.getY());
        MinionCard cardAttacked = table.getCardFrom(cardAttackedCoords.getX(), cardAttackedCoords.getY());

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", "cardUsesAttack");

        ObjectNode objectNode1 = objectMapper.createObjectNode();
        objectNode1.put("x", cardAttackerCoords.getX());
        objectNode1.put("y", cardAttackerCoords.getY());

        objectNode.set("cardAttacker", objectNode1);

        ObjectNode objectNode2 = objectMapper.createObjectNode();
        objectNode2.put("x", cardAttackedCoords.getX());
        objectNode2.put("y", cardAttackedCoords.getY());

        objectNode.set("cardAttacked", objectNode2);

        if (!MinionCard.attackedCardBelongsToEnemy(cardAttackerCoords.getX(), cardAttackedCoords.getX())) {
            objectNode.put("error", "Attacked card does not belong to the enemy.");
            output.add(objectNode);
            return;
        }

        if (!cardAttacker.canAttack()) {
            objectNode.put("error", "Attacker card has already attacked this turn.");
            output.add(objectNode);
            return;
        }

        if (cardAttacker.isFrozen()) {
            objectNode.put("error", "Attacker card is frozen.");
            output.add(objectNode);
            return;
        }

        if (table.attackedPlayerHasTankCard(cardAttackerCoords.getX()) && !cardAttacked.isTank()) {
            objectNode.put("error", "Attacked card is not of type 'Tank'.");
            output.add(objectNode);
            return;
        }

        if (cardAttacked == null) {
            return;
        }

        cardAttacker.useAttack(cardAttacked, table, cardAttackedCoords.getX());
    }

    /**
     * Prints to output the frozen cards on the table
     * @param currentAction - the action that triggered this response
     */
    public static void getFrozenCardsOnTable(ActionsInput currentAction) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", "getFrozenCardsOnTable");

        ArrayNode cards = objectMapper.createArrayNode();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 5; col++) {
                MinionCard card = table.getCardFrom(row, col);
                if (card != null && card.isFrozen()) {
                    cards.add(card.getCardPrint());
                }
            }
        }

        objectNode.set("output", cards);
        output.add(objectNode);
    }

    /**
     * The current player uses the environment card at handIdx on the specified row
     * Errors are printed to output
     * @param currentAction - the action that triggered this response
     */
    public static void useEnvironmentCard(ActionsInput currentAction) {
        if (gameEnd) return;
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        int handIdx = currentAction.getHandIdx();
        int affectedRow = currentAction.getAffectedRow();

        objectNode.put("command", "useEnvironmentCard");
        objectNode.put("handIdx", handIdx);
        objectNode.put("affectedRow", affectedRow);

        Player player = (currentTurn == PlayerTurn.PLAYER1) ? player1 : player2;
        Card cardInHand = player.getCardsInHand().get(handIdx);

        if (cardInHand.getType() != CardType.ENVIRONMENT) {
            objectNode.put("error", "Chosen card is not of type environment.");
            output.add(objectNode);
            return;
        }

        if (player.getMana() < cardInHand.getMana()) {
            objectNode.put("error", "Not enough mana to use environment card.");
            output.add(objectNode);
            return;
        }

        if ((currentTurn == PlayerTurn.PLAYER1 && (affectedRow == 2 || affectedRow == 3)) ||
                (currentTurn == PlayerTurn.PLAYER2 && (affectedRow == 0 || affectedRow == 1))) {
            objectNode.put("error", "Chosen row does not belong to the enemy.");
            output.add(objectNode);
            return;
        }

        int mirroringRow = 0;
        switch (affectedRow) {
            case 0 -> mirroringRow = 3;
            case 1 -> mirroringRow = 2;
            case 2 -> mirroringRow = 1;
        }

        if (Objects.equals(cardInHand.getName(), "Heart Hound")) {
            if (!table.isSpaceOnRow(mirroringRow)) {
                objectNode.put("error", "Cannot steal enemy card since the player's row is full.");
                output.add(objectNode);
                return;
            }
        }

        ((EnvironmentCard) cardInHand).useAbilityOnRow(affectedRow, table);

        player.subtractMana(cardInHand.getMana());
        player.getCardsInHand().remove(handIdx);
    }

    /**
     * Prints to output the card found on the table at the specified position
     * Prints error if no card is found
     * @param currentAction - the action that triggered this response
     */
    public static void getCardAtPosition(ActionsInput currentAction) {
        int x = currentAction.getX();
        int y = currentAction.getY();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        int playerIdx = currentAction.getPlayerIdx();
        objectNode.put("command", "getCardAtPosition");
        MinionCard card = table.getCardFrom(x, y);
        if (card != null)
            objectNode.set("output", card.getCardPrint());
        else
            objectNode.put("output", "No card at that position.");

        output.add(objectNode);
    }

    /**
     * Prints to output the environment cards that the player specified has in hand
     * @param currentAction - the action that triggered this response
     */
    public static void getEnvironmentCardsInHand(ActionsInput currentAction) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        int playerIdx = currentAction.getPlayerIdx();
        objectNode.put("command", "getEnvironmentCardsInHand");
        objectNode.put("playerIdx", playerIdx);

        ArrayNode cards = objectMapper.createArrayNode();
        Player player = (playerIdx == 1) ? player1 : player2;
        for (Card card : player.getCardsInHand()) {
            if (card.getType() == CardType.ENVIRONMENT)
                cards.add(card.getCardPrint());
        }
        objectNode.set("output", cards);
        output.add(objectNode);
    }

    /**
     * Prints to output the specified player's mana
     * @param currentAction - the action that triggered this response
     */
    public static void getPlayerMana(ActionsInput currentAction) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        int playerIdx = currentAction.getPlayerIdx();
        objectNode.put("command", "getPlayerMana");
        objectNode.put("playerIdx", playerIdx);

        Player currentPlayer = (playerIdx == 1) ? player1 : player2;
        objectNode.put("output", currentPlayer.getMana());
        output.add(objectNode);
    }

    /**
     * Prints to output the cards on the table, from (0, 0) to (4, 5)
     */
    public static void getCardsOnTable() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", "getCardsOnTable");

        ArrayNode cardsInTable = objectMapper.createArrayNode();
        for (int row = 0; row < 4; row++) {
            ArrayList<Card> cardsOnRow = new ArrayList<>();
            for (int col = 0; col < 5; col++) {
                cardsOnRow.add(table.getCardFrom(row, col));
            }
            ArrayNode cards = objectMapper.createArrayNode();
            for (Card card : cardsOnRow) {
                if (card != null) {
                    cards.add(card.getCardPrint());
                }
            }
            cardsInTable.add(cards);
        }

        objectNode.set("output", cardsInTable);
        output.add(objectNode);
    }

    /**
     * Prints to output the cards in the specified player's hand
     * @param currentAction - the action that triggered this response
     */
    public static void getCardsInHand(ActionsInput currentAction) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        int playerIdx = currentAction.getPlayerIdx();
        objectNode.put("command", "getCardsInHand");
        objectNode.put("playerIdx", playerIdx);

        ArrayNode cards = objectMapper.createArrayNode();
        Player player = (playerIdx == 1) ? player1 : player2;
        for (Card card : player.getCardsInHand()) {
            cards.add(card.getCardPrint());
        }
        objectNode.set("output", cards);
        output.add(objectNode);
    }

    /**
     * Places the specified card on the table, taking into account
     * its type and who owns it
     * Error are printed to output
     * @param currentAction - the action that triggered this response
     */
    public static void placeCard(ActionsInput currentAction) {
        if (gameEnd) return;
        Player currentPlayer = (currentTurn == PlayerTurn.PLAYER1) ? player1 : player2;
        Card card;
        if (currentPlayer.getCardsInHand().size() > currentAction.getHandIdx()) {
            card = currentPlayer.getCardsInHand().get(currentAction.getHandIdx());
        } else {
            System.out.println("out of bounds");
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "placeCard");
        objectNode.put("handIdx", currentAction.getHandIdx());

        if (card.getType() == CardType.ENVIRONMENT) {
            objectNode.put("error", "Cannot place environment card on table.");
            output.add(objectNode);
            return;
        }

        if (card.getMana() > currentPlayer.getMana()) {
            objectNode.put("error", "Not enough mana to place card on table.");
            output.add(objectNode);
            return;
        }

        int rowForCard;
        if (((MinionCard) card).getRowPosition() == RowPositionForCard.FRONT) {
            rowForCard = (currentTurn == PlayerTurn.PLAYER1) ? 2 : 1;
        } else {
            rowForCard = (currentTurn == PlayerTurn.PLAYER1) ? 3 : 0;
        }

        if (!table.isSpaceOnRow(rowForCard)) {
            objectNode.put("error", "Cannot place card on table since row is full.");
            output.add(objectNode);
            return;
        }

        // Update table
        table.addToRow(rowForCard, (MinionCard) card);

        // Update player mana
        currentPlayer.subtractMana(card.getMana());

        // Remove from hand
        currentPlayer.getCardsInHand().remove(currentAction.getHandIdx());
    }

    /**
     * Prints the specified player's hero card to output
     * @param currentAction - the action that triggered this response
     */
    public static void getPlayerHero(ActionsInput currentAction) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        int playerIdx = currentAction.getPlayerIdx();
        Player currentPlayer = (playerIdx == 1) ? player1 : player2;


        objectNode.put("command", "getPlayerHero");
        objectNode.put("playerIdx", playerIdx);

        objectNode.set("output", currentPlayer.getHeroCard().getCardPrint());
        output.add(objectNode);
    }

    /**
     * Prints the specified player's current deck to output
     * @param currentAction - the action that triggered this response
     */
    public static void getPlayerDeck(ActionsInput currentAction) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        int playerIdx = currentAction.getPlayerIdx();
        objectNode.put("command", "getPlayerDeck");
        objectNode.put("playerIdx", playerIdx);

        ArrayNode cards = objectMapper.createArrayNode();
        Player player = (playerIdx == 1) ? player1 : player2;
        for (Card card : player.getCurrentDeck().getCards()) {
            cards.add(card.getCardPrint());
        }
        objectNode.set("output", cards);
        output.add(objectNode);
    }

    /**
     * Prints to output the player who is active this turn
     */
    public static  void getPlayerTurn() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", "getPlayerTurn");
        objectNode.put("output", (currentTurn == PlayerTurn.PLAYER1) ? 1 : 2);

        output.add(objectNode);
    }

    /**
     * Prints to output the total number of rounds played
     */
    public static  void getTotalGamesPlayed() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", "getTotalGamesPlayed");
        objectNode.put("output", gameInstance.totalGamesPlayed);

        output.add(objectNode);
    }

    public static void getPlayerOneWins() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", "getPlayerOneWins");
        objectNode.put("output", player1.getNumberOfWins());

        output.add(objectNode);
    }

    public static void getPlayerTwoWins() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", "getPlayerTwoWins");
        objectNode.put("output", player2.getNumberOfWins());

        output.add(objectNode);
    }
}
