package Rewrite2;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.GregorianCalendar;

public class GameBoard {
    private GameTile[][] gameTiles;
    private int player;
    private ImageIcon player1Icon, player2Icon;
    private int winner;
    private String player1Name, player2Name;
    private GregorianCalendar timeStarted, timeElapsed;
    private boolean aiToggle;
    private int aiDifficulty, lastAIMove;

    public GameBoard(){
        initializeBoard(7);
        setPlayer(1);
        setLastAIMove(4);
    }
    public GameBoard(int boardSize){
        initializeBoard(boardSize);
        setPlayer(1);
        setLastAIMove(boardSize/2);
    }

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
    public void doAIMove(){
        switchPlayer();
        int currentAIMove;

        switch (getAiDifficulty()){
            case 0: //v.easy
                addTile((int)(Math.random() * getGameBoard().length));
                break;
            case 1: //easy

                //ai places move within 1 column of last move (first move is initialized as the center of the board)
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
    public void setGameBoard(GameTile[][] gameTiles){
        this.gameTiles = gameTiles;
    }
    public GameTile[][] getGameBoard(){
        return Arrays.copyOf(gameTiles, gameTiles.length);
    }

    //player
    public int getPlayer(){
        return player;
    }
    public void setPlayer(int player){
        this.player = player;
    }
    public void switchPlayer(){
        if(getPlayer() == 1){
            setPlayer(2);
        } else {
            setPlayer(1);
        }
    }

    //playerIcons
    public void setPlayerIcons(ImageIcon player1Icon, ImageIcon player2Icon){
        this.player1Icon = player1Icon;
        this.player2Icon = player2Icon;
    }
    public ImageIcon getPlayer1Icon(){
        return this.player1Icon;
    }
    public ImageIcon getPlayer2Icon(){
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

    //game logic stuff
    public void addTile(int column){
        for(int i=getGameBoard().length-1; i!=-1; i--){
            if(getGameBoard()[column][0].getState() != 0){
                System.out.println("Invalid move! Column is full.");
                break;
            }
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
    public void previewPlacement(int column){
        for(int i=getGameBoard().length-1; i!=-1; i--){
            if(getGameBoard()[column][0].getState() != 0){
                break;
            }
            if(getGameBoard()[column][i].getState() == 0){
                if(getPlayer() == 1){
                    getGameBoard()[column][i].setBackground(Color.RED);
                } else {
                    getGameBoard()[column][i].setBackground(Color.BLUE);
                }
                break;
            }
        }
    }

    public void depreviewPlacement(int column){
        for(int i=getGameBoard().length-1; i!=-1; i--){
            if(getGameBoard()[column][0].getState() != 0){
                break;
            }
            if(getGameBoard()[column][i].getState() == 0){
                getGameBoard()[column][i].setBackground(Color.white);
                break;
            }
        }
    }

    public void highlightColumn(int column){
        for(int i = 0; i < getGameBoard().length; i++){
            getGameBoard()[column][i].setBackground(Color.gray);
        }
        getGameBoard()[column][getLowestAvailableTile(column)].setBackground(Color.red);
    }

    public void dehighlightColumn(int column){
        for(int i = 0; i < getGameBoard().length; i++){
            getGameBoard()[column][i].setBackground(Color.white);
        }
    }

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
        /**
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

        /**
         * the loops below scan through each tile in the grid and check the 3 tiles following to see if they match states.
         * it skips the checking last 3 columns/rows (depending on the check) as
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
