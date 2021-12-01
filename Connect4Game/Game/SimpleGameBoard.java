package Game;

import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * An instantiable, simplified version of a GameBoard object which is used for save/load states,
 * since IOStream seems to have an issue with the regular GameBoard.
 * Instead of the game's state being stored as a 2D GameTile array,
 * it will be stored as a 2D Integer array and be converted when needed.
 *
 * @author Daniel Arefjev
 */
public class SimpleGameBoard implements Serializable {
    private int boardSize;
    private int[][] gameState;
    private int player1Icon;
    private int player2Icon;
    private String player1Name;
    private String player2Name;
    private int lastAIMove;
    private boolean aiToggle;
    private int aiDifficulty;
    private int player;
    private GregorianCalendar timeStarted;
    private GregorianCalendar timeElapsed;

    private int winner;

    /**
     * Default no-argument constructor for a SimpleGameBoard object. Doesn't do anything.
     */
    public SimpleGameBoard(){
    }

    /**
     * 2 argument constructor for a SimpleGameBoard object. Passes 2 arguments to setBoardSize() and setGameState().
     * @param boardSize the size (width/height) of the game board.
     * @param gameState the game state of the game board. Stored as a 2D integer array as opposed to a 2D GameTile array.
     */
    public SimpleGameBoard(int boardSize, int[][] gameState){
        setBoardSize(boardSize);
        setGameState(gameState);
    }

    /**
     * Method to get the game state of a SimpleGameBoard object.
     * @return Returns gameState, a 2D integer array containing the state of the game (more specifically the state
     * of a tile at a specific index)
     */
    public int[][] getGameState() {
        return gameState;
    }

    /**
     * Method to set the game state of a SimpleGameBoard object.
     * @param gameState
     */
    public void setGameState(int[][] gameState) {
        this.gameState = gameState;
    }

    /**
     * Method to set the board size of a SimpleGameBoard object.
     * @param boardSize
     */
    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    /**
     * Method to get the board size of a SimpleGameBoard object.
     * @return boardSize, an Integer value corresponding to the width/height of a game board.
     * (e.g boardSize = 7 means it's a 7x7 board.)
     */
    public int getBoardSize() {
        return boardSize;
    }

    //ai

    /**
     * Method to set AI related parameters of a SimpleGameBoard object.
     * @param aiToggle a boolean value, where false means the AI is toggled off, true is on.
     * @param aiDifficulty an integer value corrosponding to the difficulty of the AI, where 0 is the easiest and
     * higher values get harder. (Currently at a max of 2)
     */
    public void setAIStuff(boolean aiToggle, int aiDifficulty){
        this.aiToggle = aiToggle;
        this.aiDifficulty = aiDifficulty; //0 = v.easy, 1 = easy
    }

    /**
     * Method to set the last AI move made in a SimpleGameBoard object.
     * @param lastAIMove the column last selected by the AI.
     */
    public void setLastAIMove(int lastAIMove){
        this.lastAIMove = lastAIMove;
    }

    /**
     * Method to get the last AI move made in a SimpleGameBoard object.
     * @return an integer value corresponding to the column the AI last selected.
     */
    public int getLastAIMove(){
        return lastAIMove;
    }

    /**
     * Method to get the aiToggle of a SimpleGameBoard object.
     * @return a boolean value, where false means the AI is toggled off, and true means the AI is toggled on.
     */
    public boolean getAIToggle(){
        return aiToggle;
    }

    /**
     * Method to get the AI difficulty of a SimpleGameBoard object.
     * @return an integer value corresponding to the difficulty of the AI, where 0 is the easiest and
     * higher values get harder. (Currently at a max of 2)
     */
    public int getAiDifficulty() {
        return aiDifficulty;
    }

    //player

    /**
     * Method to get the current player in a SimpleGameBoard object.
     * @return an integer value, either 1 or 2.
     */
    public int getPlayer(){
        return player;
    }

    /**
     * Method to set the current player in a SimpleGameBoard object.
     * @param player the player to set as currently active in a SimpleGameBoard object.
     */
    public void setPlayer(int player){
        this.player = player;
    }

    //playerIcons

    /**
     * Method to set both player's icons in a SimpleGameBoard object.
     * @param player1Icon the icon index to set for player 1.
     * @param player2Icon the icon index to set for player 2.
     */
    public void setPlayerIcons(int player1Icon, int player2Icon){
        this.player1Icon = player1Icon;
        this.player2Icon = player2Icon;
    }

    /**
     * Methods to get each player's icon in a SimpleGameBoard object.
     * @return an integer value which corresponds to the index of an ImageIcon array in the Connect4Game class.
     */
    public int getPlayer1Icon(){
        return this.player1Icon;
    }
    public int getPlayer2Icon(){
        return this.player2Icon;
    }

    //winner

    /**
     * Method to set the winner of a SimpleGameBoard object.
     * @param winner the player that won the game.
     */
    public void setWinner(int winner){
        this.winner = winner;
    }

    /**
     * Method to get the winner of a SimpleGameBoard object.
     * @return an integer value corresponding to which player won the game - if there's no winners it returns 0.
     */
    public int getWinner(){
        return winner;
    }

    //playerNames

    /**
     * Method to set both players names in a SimpleGameBoard object.
     * @param player1Name the name of player 1.
     * @param player2Name the name of player 2.
     */
    public void setPlayerNames(String player1Name, String player2Name){
        this.player1Name = player1Name;
        this.player2Name = player2Name;
    }

    /**
     * Methods to get the names of the players in a SimpleGameBoard object.
     * @return a String containing either Player 1 or Player 2's name, depending on which method is called.
     */
    public String getPlayer1Name(){
        return player1Name;
    }
    public String getPlayer2Name(){
        return player2Name;
    }

    //timeStarted

    /**
     * Method to set the time at which a game was started in a SimpleGameBoard object.
     * @param timeStarted the time at which a game was started, as a GregorianCalendar object.
     */
    public void setTimeStarted(GregorianCalendar timeStarted){
        this.timeStarted = timeStarted;
    }

    /**
     * Method to get the time at which a game was started in a SimpleGameBoard object.
     * @return a GregorianCalendar object containing the system time at which a game was created.
     */
    public GregorianCalendar getTimeStarted(){
        return timeStarted;
    }

    //timeElapsed

    /**
     * Method to set the time elapsed within a SimpleGameBoard object.
     * @param timeElapsed a GregorianCalendar object containing the minutes/seconds a game elapsed.
     */
    public void setTimeElapsed(GregorianCalendar timeElapsed){
        this.timeElapsed = timeElapsed;
    }

    /**
     * Method to get the time elapsed within a SimpleGameBoard object.
     * @return a GregorianCalendar object containing how long a game elapsed.
     */
    public GregorianCalendar getTimeElapsed(){
        return timeElapsed;
    }

}
