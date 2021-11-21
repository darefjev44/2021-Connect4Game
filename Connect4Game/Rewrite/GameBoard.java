package Rewrite;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.GregorianCalendar;

public class GameBoard implements Serializable {
    private GameTile[][] gameTiles;
    private int player;
    private ImageIcon player1Icon, player2Icon;
    private int winner;
    private String player1Name, player2Name;
    private GregorianCalendar timeStarted, timeElapsed;
    private boolean aiToggle;
    private int aiDifficulty, lastAIMove;

    public GameBoard(){
        setPlayer(1);
        setWinner(0);
    }

    public GameBoard(int boardSize){
        initializeBoard(boardSize);
        setPlayer(1);
    }

    public void initializeBoard(int boardSize){
        setLastAIMove(boardSize/2);
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
    //ai
    public void setAIStuff(boolean aiToggle, int aiDifficulty){
        this.aiToggle = aiToggle;
        this.aiDifficulty = aiDifficulty; //0 = veasy, 1 = easy
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
                    System.out.println("Player1 Tile Placed at y " + i);
                } else {
                    getGameBoard()[column][i].setIcon(getPlayer2Icon());
                    System.out.println("Player2 Tile Placed at y " + i);
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
    }

    public void dehighlightColumn(int column){
        for(int i = 0; i < getGameBoard().length; i++){
            getGameBoard()[column][i].setBackground(Color.white);
        }
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
        boolean isDraw = true;
        for(int i = 0; i < getGameBoard().length; i++){
            if(getGameBoard()[0][i].getState() == 0){
                isDraw = false;
                break;
            }
        }
        if(isDraw){
            return 3;
        }

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

        //TODO check if these (diag) actually work in weird scenarios.
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
