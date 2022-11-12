package game;

import cards.Card;
import cards.CardType;
import cards.RowPositionForCard;
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

    enum playerTurn {
        PLAYER1,
        PLAYER2
    }

    static private playerTurn currentTurn;

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
        if (inputData.getStartingPlayer() == 0) {
            currentTurn = playerTurn.PLAYER1;
        } else {
            currentTurn = playerTurn.PLAYER2;
        }
    }

    static public void startGame(Input inputData, ArrayNode output) {

        System.out.println("--------STARTED GAME-----------");
        // Created new instance so the game is new
        gameInstance = new Game(inputData, output);

        // Load the initial state of the game
        gameInstance.load();

        // Get through each round
        for (GameInput gameInput : inputData.getGames()) {
            gameInstance.setRoundInfo(gameInput.getStartGame());

            Iterator<ActionsInput> actions = gameInput.getActions().iterator();

            // When this is even, a new round has started and the players get one more card each
            int turnNumber = 0;

            while (actions.hasNext()) {
                if (turnNumber % 2 == 0) {
                    player1.addCardToHand();
                    player2.addCardToHand();

                    // Updadte mana
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
        Player currentPlayer = (currentTurn == playerTurn.PLAYER1) ? player1 : player2;
        System.out.println("PLAYER INDEX: " + currentTurn);

        // get actions until end turn or end of actions
        ActionsInput currentAction;
        do {
            currentAction = actions.next();
            if (Objects.equals(currentAction.getCommand(), "endPlayerTurn")) {
                break;
            }
            // testing purposes
            System.out.println(currentAction.getCommand() + currentAction.getPlayerIdx());

            // action handler
            actionHandler(currentAction);

        } while (actions.hasNext());
        System.out.println("-------END TURN--------");
        // De-freeze this player's cards

        // Switch turn
        currentTurn = (currentTurn == Game.playerTurn.PLAYER1) ?
                Game.playerTurn.PLAYER2 : Game.playerTurn.PLAYER1;
    }

    static public void actionHandler(ActionsInput currentAction) {
        switch (currentAction.getCommand()) {
            case "getPlayerDeck" -> getPlayerDeck(currentAction);
            case "getPlayerTurn" -> getPlayerTurn();
            case "getPlayerHero" -> getPlayerHero(currentAction);
            case "placeCard" -> placeCard(currentAction);
            case "getCardsInHand" -> getCardsInHand(currentAction);
            case "getCardsOnTable" -> getCardsOnTable();
            case "getPlayerMana" -> getPlayerMana(currentAction);
        }
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
        Player currentPlayer = (currentTurn == playerTurn.PLAYER1) ? player1 : player2;
        Card card = currentPlayer.getCardsInHand().get(currentAction.getHandIdx());

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "placeCard");
        objectNode.put("handIdx", currentAction.getHandIdx());

        if (card.getType() == CardType.ENVIRONMENT) {
            String message = "Cannot place environment card on table.";
            objectNode.put("error", message);
            output.add(objectNode);
            return;
        }

        if (card.getMana() > currentPlayer.getMana()) {
            String message = "Not enough mana to place card on table.";
            objectNode.put("error", message);
            output.add(objectNode);
            return;
        }

        int rowForCard;
        if (((MinionCard)card).getRowPosition() == RowPositionForCard.FRONT) {
            rowForCard = (currentTurn == playerTurn.PLAYER1) ? 2 : 1;
        } else {
            rowForCard = (currentTurn == playerTurn.PLAYER1) ? 3 : 0;
        }

        if (!table.isSpaceOnRow(rowForCard)) {
            String message = "Cannot place card on table since row is full.";
            objectNode.put("error", message);
            output.add(objectNode);
            return;
        }

        // Update table
        table.addToRow(rowForCard, (MinionCard) card);

        // Remove from hand
        currentPlayer.getCardsInHand().remove(currentAction.getHandIdx());

        // Update player mana
        currentPlayer.setMana(currentPlayer.getMana() - card.getMana());

        // Testing purposes
        //MinionCard minionCard = table.getCardFrom(rowForCard, 0);
       // System.out.println(minionCard);
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
        objectNode.put("output", (currentTurn == playerTurn.PLAYER1) ? 1 : 2);

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
