package Game;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.GregorianCalendar;

/**
 * An instantiable class which defines a Game Board.
 * Besides mutators, it contains methods for Connect Four game logic, such as adding a tile to a column or
 * checking if a player has won the game.
 *
 * @author Daniel Arefjev
 */
public class GameBoard {
    private GameTile[][] gameTiles;
    private int player;
    private ImageIcon player1Icon, player2Icon;
    private int winner;
    private String player1Name, player2Name;
    private GregorianCalendar timeStarted, timeElapsed;
    private boolean aiToggle;
    private int aiDifficulty, lastAIMove;

    /**
     * GameBoard 1 argument constructor. Calls the initializeBoard() method with a user-supplied value,
     * and the setPlayer() / setLastAiMove() mutators with some default values.
     * @param boardSize the width/height of the game board.
     */
    public GameBoard(int boardSize){
        initializeBoard(boardSize);
        setPlayer(1);
        setLastAIMove(boardSize/2);
    }

    /**
     * A method which creates a 2D array of a specified size, and fills it with GameTile objects.
     * It sets the name of the GameTile objects to the index of the column they're in, adds borders to the GameTile objects,
     * sets them to be opaque so that this doesn't need to be called later in highlightColumn/dehighlightColumn which
     * can be called multiple times per column, whereas this is only called once here.
     * @param boardSize the size of the first and second dimension of the GameTile[][] array.
     */
    public void initializeBoard(int boardSize){
        this.gameTiles = new GameTile[boardSize][boardSize];
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                this.gameTiles[i][j] = new GameTile();
                this.gameTiles[i][j].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
                this.gameTiles[i][j].setName(String.format("%s", i));
                getGameBoard()[i][j].setOpaque(true);
            }
        }
    }

    /**
     * A method which sets the icons of GameTile objects within a GameBoard object appropriate to their state.
     */
    public void loadIcons(){
        for(int i = 0; i<getGameBoard().length; i++){
            for(int j = 0; j<getGameBoard().length; j++){
                if(getGameBoard()[i][j].getState() == 1){
                    getGameBoard()[i][j].setIcon(player1Icon);
                } else if (getGameBoard()[i][j].getState() == 2){
                    getGameBoard()[i][j].setIcon(player2Icon);
                }
            }
        }
    }
    //ai

    /**
     * Method to set the AI toggle and AI difficulty of a GameBoard object.
     * @param aiToggle a boolean value indicating whether the AI is toggled on or off.
     * @param aiDifficulty the level of difficulty of the AI.
     */
    public void setAIStuff(boolean aiToggle, int aiDifficulty){
        this.aiToggle = aiToggle;
        this.aiDifficulty = aiDifficulty; //0 = v.easy, 1 = easy
    }

    /**
     * Method to set the last AI move in a GameBoard object.
     * @param lastAIMove the column last selected by the "AI" player.
     */
    public void setLastAIMove(int lastAIMove){
        this.lastAIMove = lastAIMove;
    }

    /**
     * Method to get the last AI move in a GameBoard object.
     * @return an integer representing the last column chosen by the "AI" player.
     */
    public int getLastAIMove(){
        return lastAIMove;
    }

    /**
     * Method to get the AI toggle of a GameBoard object.
     * @return a boolean value indicating whether the AI is toggled on or off.
     */
    public boolean getAIToggle(){
        return aiToggle;
    }

    /**
     * Method to get the AI difficulty of a GameBoard object.
     * @return an integer value representing the AI difficulty of a GameBoard object.
     */
    public int getAiDifficulty() {
        return aiDifficulty;
    }

    /**
     * Method which generates a number using different algorithms and calls addTile(), passing in that number as the parameter.
     */
    public void doAIMove(){
        switchPlayer();
        int currentAIMove;

        switch (getAiDifficulty()){
            case 0: //v.easy
                //ai places move randomly within the board's bounds.
                addTile((int)(Math.random() * getGameBoard().length));
                break;
            case 1: //easy

                //ai places move within 1 column of last move (first move is at the center of the board)
                currentAIMove = (int) (getLastAIMove() + Math.floor(Math.random()*3) - 1);

                //limit AI moves to board boundaries
                if(currentAIMove>getGameBoard().length){
                    currentAIMove = getGameBoard().length;
                } else if (currentAIMove<0){
                    currentAIMove = 0;
                }

                addTile(currentAIMove);
                setLastAIMove(currentAIMove);
        }
    }

    //gameTiles
    /**
     * Method to get the current GameTiles of a GameBoard object.
     * @return a 2D array of GameTile representing the state of a GameBoard object.
     */
    public GameTile[][] getGameBoard(){
        return Arrays.copyOf(gameTiles, gameTiles.length);
    }

    //player
    /**
     * Method to get the current player of a GameBoard object.
     * @return an Integer value representing the current player in a GameBoard object.
     */
    public int getPlayer(){
        return player;
    }

    /**
     * Method to set the current player of a GameBoard object.
     * @param player the player to set the current player of a GameBoard object to.
     */
    public void setPlayer(int player){
        this.player = player;
    }

    /**
     * Method to switch the current player of a GameBoard object.
     */
    public void switchPlayer(){
        if(getPlayer() == 1){
            setPlayer(2);
        } else {
            setPlayer(1);
        }
    }

    //playerIcons
    /**
     * Method to set the icons of the players in a GameBoard object.
     * @param player1Icon
     * @param player2Icon
     */
    public void setPlayerIcons(ImageIcon player1Icon, ImageIcon player2Icon){
        this.player1Icon = player1Icon;
        this.player2Icon = player2Icon;
    }
    /**
     * Method to get the Player 1 icon of a GameBoard object.
     * @return an ImageIcon object containing the icon of Player 21 in a GameBoard object.
     */
    public ImageIcon getPlayer1Icon(){
        return this.player1Icon;
    }
    /**
     * Method to get the Player 2 icon of a GameBoard object.
     * @return an ImageIcon object containing the icon of Player 2 in a GameBoard object.
     */
    public ImageIcon getPlayer2Icon(){
        return this.player2Icon;
    }

    //winner
    /**
     * Method to set the winner of a GameBoard object.
     * @param winner the winner of the GameBoard object.
     */
    public void setWinner(int winner){
        this.winner = winner;
    }
    /**
     * Method to get the winner of a GameBoard object.
     * @return an Integer value representing the player which won in the GameBoard object.
     */
    public int getWinner(){
        return winner;
    }

    //playerNames
    /**
     * Method to set the names of the players in a GameBoard object.
     * @param player1Name The name of the first player.
     * @param player2Name The name of the second player.
     */
    public void setPlayerNames(String player1Name, String player2Name){

        if(player1Name.equals("")){
            this.player1Name = "Player 1";
        } else {
            this.player1Name = player1Name;
        }

        if(!aiToggle) {
            if(player2Name.equals("")){
                this.player2Name = "Player 2";
            } else {
                this.player2Name = player2Name;
            }
        } else {
            this.player2Name = "Computer";
        }
    }
    /**
     * Method to get the Player 1 name of a GameBoard object.
     * @return a String value specifying the name of Player 1 in a GameBoard object.
     */
    public String getPlayer1Name(){
        return player1Name;
    }
    /**
     * Method to get the Player 2 name of a GameBoard object.
     * @return a String value specifying the name of Player 2 in a GameBoard object.
     */
    public String getPlayer2Name(){
        return player2Name;
    }

    //timeStarted
    /**
     * Method to set the timeStarted of a GameBoard object.
     * @param timeStarted the timeStarted of a GameBoard object.
     */
    public void setTimeStarted(GregorianCalendar timeStarted){
        this.timeStarted = timeStarted;
    }
    /**
     * Method to get the timeElapsed of a GameBoard object.
     * @return a GregorianCalender value specifying the timeStarted of a GameBoard object.
     */
    public GregorianCalendar getTimeStarted(){
        return timeStarted;
    }

    //timeElapsed

    /**
     * Method to set the timeElapsed of a GameBoard object.
     * @param timeElapsed the timeElapsed of a GameBoard object.
     */
    public void setTimeElapsed(GregorianCalendar timeElapsed){
        this.timeElapsed = timeElapsed;
    }

    /**
     * Method to get the timeElapsed of a GameBoard object.
     * @return a GregorianCalender value specifying the timeElapsed of a GameBoard object.
     */
    public GregorianCalendar getTimeElapsed(){
        return timeElapsed;
    }

    //game logic stuff
    /**
     * Method to add a tile to a column.
     * @param column the column to add a tile to.
     */
    public void addTile(int column){
        for(int i=getGameBoard().length-1; i!=-1; i--){
            if(getGameBoard()[column][i].getState() == 0){
                getGameBoard()[column][i].setState(getPlayer());
                if(getPlayer() == 1){
                    getGameBoard()[column][i].setIcon(getPlayer1Icon());
                } else {
                    getGameBoard()[column][i].setIcon(getPlayer2Icon());
                }
                break;
            }
        }
    }

    /**
     * Method which "highlights" a column by changing all background of the tiles to pink.
     * @param column the column to highlight.
     */
    public void highlightColumn(int column){
        for(int i = 0; i < getGameBoard().length; i++){
            getGameBoard()[column][i].setBackground(Color.PINK);
        }
    }

    /**
     * Method which sets the background of a column to back to normal.
     * @param column the column to de-highlight.
     */
    public void dehighlightColumn(int column){
        for(int i = 0; i < getGameBoard().length; i++){
            getGameBoard()[column][i].setBackground(Color.WHITE);
        }
    }

    /**
     * Method to return the lowest "available" tile in a given column.
     * @param column the column to check.
     * @return the tile index - or -1 if none are available.
     */
    public int getLowestAvailableTile(int column){
        for(int i=getGameBoard().length-1; i!=-1; i--){
            if(getGameBoard()[column][0].getState() != 0){
                return -1;
            }
            if(getGameBoard()[column][i].getState() == 0){
                return i;
            }
        }
        return -1;
    }

    /**
     * Method which checks the game board to see if there is a winner or a draw.
     * @return the winning player (0, for no winner/no draw and 3 for a draw.)
     */
    public int checkForWinner(){
        //System.out.println("checkForWinner() called");
        int player = getPlayer();
        /*
        0 - game still ongoing
        1 - red wins
        2 - blue wins
        3 - draw
        */

        //draw check
        /*
         * Checks each of the tiles along the top of the board -- if any of them are unoccupied we assume it's still possible
         * for there to be a winner, so we break out of the loop and continue checking for any winners.
         */
        boolean isDraw = true;
        for(int i = 0; i < getGameBoard().length; i++){
            if(getGameBoard()[i][0].getState() == 0){
                isDraw = false;
                break;
            }
        }
        if(isDraw){
            setWinner(3);
            return 3;
        }

        /*
         * the loops below scan through each tile (besides in the last 3 rows/columns, depending on which check it is)
         * in the grid and check the 3 tiles following to see if they match states. If they match states, the winner
         * is set to the current player, and the current player is returned.
         */
        //vertical check
        for(int i = 0; i < getGameBoard().length; i++){
            for(int j = 0; j < getGameBoard().length-3; j++){
                if(getGameBoard()[i][j].getState() == player && getGameBoard()[i][j+1].getState() == player && getGameBoard()[i][j+2].getState() == player && getGameBoard()[i][j+3].getState() == player){
                    setWinner(player);
                    return player;
                }
            }
        }

        //horizontal check
        for(int i = 0; i < getGameBoard().length; i++){
            for(int j = 0; j < getGameBoard().length-3; j++){
                if(getGameBoard()[j][i].getState() == player && getGameBoard()[j+1][i].getState() == player && getGameBoard()[j+2][i].getState() == player && getGameBoard()[j+3][i].getState() == player){
                    setWinner(player);
                    return player;
                }
            }
        }

        //asc diagonal
        for(int i = getGameBoard().length-1; i>4; i--){
            for(int j = 0; j < getGameBoard().length-3; j++){
                if(getGameBoard()[j][i].getState() == player && getGameBoard()[j+1][i-1].getState() == player && getGameBoard()[j+2][i-2].getState() == player && getGameBoard()[j+3][i-3].getState() == player){
                    setWinner(player);
                    return player;
                }
            }
        }

        //desc diagonal
        for(int i = 0; i< getGameBoard().length-3; i++){
            for(int j = 0; j < getGameBoard().length-3; j++){
                if(getGameBoard()[j][i].getState()==player && getGameBoard()[j+1][i+1].getState()==player && getGameBoard()[j+2][i+2].getState()==player && getGameBoard()[j+3][i+3].getState()==player){
                    setWinner(player);
                    return player;
                }
            }
        }
        return 0;
    }
}
