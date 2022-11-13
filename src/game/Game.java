package game;

import cards.Card;
import cards.CardType;
import cards.RowPositionForCard;
import cards.environment.EnvironmentCard;
import cards.minion.MinionCard;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.GameInput;
import fileio.Input;
import fileio.StartGameInput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class Game {
    static private Game gameInstance;
    private int totalGamesPlayed = 0;
    private static Player player1;
    private static Player player2;
    private static GameTable table;
    private final Input inputData;
    private static ArrayNode output;
    static private PlayerTurn currentTurn;

    private Game(Input inputData, ArrayNode output) {
        player1 = new Player();
        player2 = new Player();
        table = new GameTable();
        this.inputData = inputData;
        Game.output = output;
    }

    /**
     * Loads the game state described in inputData into gameInstance
     */
    private void load() {
        totalGamesPlayed = 0;
        player1.loadDecks(inputData.getPlayerOneDecks());
        player2.loadDecks(inputData.getPlayerTwoDecks());
    }

    private void setRoundInfo(StartGameInput inputData) {
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

    static public void startGame(Input inputData, ArrayNode output) {
        System.out.println("--------STARTED GAME-----------\n------------------");
        // Created new instance so the game is new
        gameInstance = new Game(inputData, output);

        // Load the initial state of the game
        gameInstance.load();

        // Get through each round
        for (GameInput gameInput : inputData.getGames()) {
            gameInstance.setRoundInfo(gameInput.getStartGame());

            Iterator<ActionsInput> actions = gameInput.getActions().iterator();

            // When this is even, a new round has started
            int turnNumber = 0;

            while (actions.hasNext()) {
                System.out.println("PLAYER INDEX: " + currentTurn);
                if (turnNumber % 2 == 0) {
                    player1.addCardToHand();
                    player2.addCardToHand();

                    // Update mana
                    Player.setNumberOfGamesPlayed(++gameInstance.totalGamesPlayed);
                    player1.addManaForNewRound();
                    player2.addManaForNewRound();
                }
                playTurn(actions);
                turnNumber++;
            }
        }
    }

    public static void playTurn(Iterator<ActionsInput> actions) {
        Player currentPlayer = (currentTurn == PlayerTurn.PLAYER1) ? player1 : player2;

        // get actions until end turn or end of actions
        ActionsInput currentAction;
        do {
            currentAction = actions.next();
            if (Objects.equals(currentAction.getCommand(), "endPlayerTurn")) {
                // Switch turn
                currentTurn = (currentTurn == PlayerTurn.PLAYER1) ? PlayerTurn.PLAYER2 : PlayerTurn.PLAYER1;
                break;
            }

            // action handler
            actionHandler(currentAction);

        } while (actions.hasNext());
        System.out.println("-------END TURN--------");

        // De-freeze this player's cards
        table.unfreezeCardsOfPlayer(currentTurn);
    }

    static public void actionHandler(ActionsInput currentAction) {
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
        }
    }

    static public void getFrozenCardsOnTable(ActionsInput currentAction) {
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
    static public void useEnvironmentCard(ActionsInput currentAction) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        int handIdx = currentAction.getHandIdx();
        int affectedRow = currentAction.getAffectedRow();

        objectNode.put("command", "useEnvironmentCard");
        objectNode.put("handIdx", handIdx);
        objectNode.put("affectedRow", affectedRow);

        Player player = (currentTurn == PlayerTurn.PLAYER1) ? player1 : player2;
        Card cardInHand = player.getCardsInHand().get(handIdx);
        System.out.println("USE ENVIRONMENT CARD" +  cardInHand);
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

        ((EnvironmentCard)cardInHand).useAbilityOnRow(affectedRow, table);

        player.subtractMana(cardInHand.getMana());
        player.getCardsInHand().remove(handIdx);
    }
    static public void getCardAtPosition(ActionsInput currentAction) {
        int x = currentAction.getX();
        int y = currentAction.getY();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        int playerIdx = currentAction.getPlayerIdx();
        objectNode.put("command", "getCardAtPosition");
        MinionCard card = table.getCardFrom(x,y);
        if (card != null)
            objectNode.set("output", card.getCardPrint());
        else
            objectNode.put("output", "No card at that position.");

        output.add(objectNode);
    }

    static public void getEnvironmentCardsInHand(ActionsInput currentAction) {
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

    static public void getPlayerMana(ActionsInput currentAction) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        int playerIdx = currentAction.getPlayerIdx();
        objectNode.put("command", "getPlayerMana");
        objectNode.put("playerIdx", playerIdx);

        Player currentPlayer = (playerIdx == 1) ? player1 : player2;
        objectNode.put("output", currentPlayer.getMana());
        output.add(objectNode);
    }

    static public void getCardsOnTable() {
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

    static public void getCardsInHand(ActionsInput currentAction) {
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

    static public void placeCard(ActionsInput currentAction) {
        Player currentPlayer = (currentTurn == PlayerTurn.PLAYER1) ? player1 : player2;
        Card card;
        if (currentPlayer.getCardsInHand().size() > currentAction.getHandIdx()) {
            card = currentPlayer.getCardsInHand().get(currentAction.getHandIdx());
        } else {
            System.out.println("out of bounds");
            return;
        }

        System.out.println("PLACE CARD from index " + currentAction.getHandIdx() +"\n" + currentPlayer.getCardsInHand());

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
        System.out.println(((MinionCard)card).getRowPosition());
        if (((MinionCard)card).getRowPosition() == RowPositionForCard.FRONT) {
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

    static public void getPlayerHero(ActionsInput currentAction) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        int playerIdx = currentAction.getPlayerIdx();
        Player currentPlayer = (playerIdx == 1) ? player1 : player2;


        objectNode.put("command", "getPlayerHero");
        objectNode.put("playerIdx", playerIdx);

        objectNode.set("output", currentPlayer.getHeroCard().getCardPrint());
        output.add(objectNode);
    }

    static public void getPlayerDeck(ActionsInput currentAction) {
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

    static public void getPlayerTurn() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", "getPlayerTurn");
        objectNode.put("output", (currentTurn == PlayerTurn.PLAYER1) ? 1 : 2);

        output.add(objectNode);
    }

    static public void getTotalGamesPlayed() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", "getTotalGamesPlayed");
        objectNode.put("output", gameInstance.totalGamesPlayed);

        output.add(objectNode);
    }

    static public void getPLayerOneWins() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", "getPLayerOneWins");
        objectNode.put("output", player1.getNumberOfWins());

        output.add(objectNode);
    }

    static public void getPLayerTwoWins() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", "getPLayerTwoWins");
        objectNode.put("output", player2.getNumberOfWins());

        output.add(objectNode);
    }
}
