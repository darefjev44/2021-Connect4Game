package Rewrite2;

import javax.swing.*;
import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * A simplified version of a GameBoard object which is used for save/load states, since IOStream seems to have an issue with the regular GameBoard.
 * Instead of the game's state being stored as a 2D GameTile array, it will be stored as a String and be converted when needed.
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

    public SimpleGameBoard(){
    }

    public SimpleGameBoard(int boardSize, int[][] gameState){
        setBoardSize(boardSize);
        setGameState(gameState);
    }

    public int[][] getGameState() {
        return gameState;
    }

    public void setGameState(int[][] gameState) {
        this.gameState = gameState;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public int getBoardSize() {
        return boardSize;
    }

    //ai
    public void setAIStuff(boolean aiToggle, int aiDifficulty){
        this.aiToggle = aiToggle;
        this.aiDifficulty = aiDifficulty; //0 = v.easy, 1 = easy
    }
    public void setLastAIMove(int lastAIMove){
        this.lastAIMove = lastAIMove;
    }
    public int getLastAIMove(){
        return lastAIMove;
    }
    public boolean getAIToggle(){
        return aiToggle;
    }
    public int getAiDifficulty() {
        return aiDifficulty;
    }

    //player
    public int getPlayer(){
        return player;
    }
    public void setPlayer(int player){
        this.player = player;
    }

    //playerIcons
    public void setPlayerIcons(int player1Icon, int player2Icon){
        this.player1Icon = player1Icon;
        this.player2Icon = player2Icon;
    }
    public int getPlayer1Icon(){
        return this.player1Icon;
    }
    public int getPlayer2Icon(){
        return this.player2Icon;
    }

    //winner
    public void setWinner(int winner){
        this.winner = winner;
    }
    public int getWinner(){
        return winner;
    }

    //playerNames
    public void setPlayerNames(String player1Name, String player2Name){
        this.player1Name = player1Name;
        this.player2Name = player2Name;
    }
    public String getPlayer1Name(){
        return player1Name;
    }
    public String getPlayer2Name(){
        return player2Name;
    }

    //timeStarted
    public void setTimeStarted(GregorianCalendar timeStarted){
        this.timeStarted = timeStarted;
    }
    public GregorianCalendar getTimeStarted(){
        return timeStarted;
    }

    //timeElapsed
    public void setTimeElapsed(GregorianCalendar timeElapsed){
        this.timeElapsed = timeElapsed;
    }
    public GregorianCalendar getTimeElapsed(){
        return timeElapsed;
    }

    public void setAIToggle(boolean aiToggle) {
        this.aiToggle = aiToggle;
    }
}
