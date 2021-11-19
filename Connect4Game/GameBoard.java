import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class GameBoard {
    private GameTile[][] gameTiles;
    private ImageIcon player1;
    private ImageIcon player2;
    private int player;
    private int winner;
    private int clickCounter = 0;

    public GameBoard(){
        setPlayer(1);
        setWinner(0);
    }

    public GameBoard(int boardSize){
        initializeBoard(boardSize);
    }

    public void initializeBoard(int boardSize){
        this.gameTiles = new GameTile[boardSize][boardSize];
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                this.gameTiles[i][j] = new GameTile();
                this.gameTiles[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                this.gameTiles[i][j].setName(String.format("%s", i));
                getGameBoard()[i][j].setOpaque(true);
            }
        }
    }

    public GameTile[][] getGameBoard(){
        return Arrays.copyOf(gameTiles, gameTiles.length);
    }

    public void addClick(){
        this.clickCounter = getClicks()+1;
    }

    public int getClicks(){
        return clickCounter;
    }

    //player related
    public void setGameColors(ImageIcon player1, ImageIcon player2){
        this.player1 = player1;
        this.player2 = player2;
    }

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

    public int getWinner(){
        return winner;
    }
    public void setWinner(int winner){
        this.winner = winner;
    }

    //board related
    public void printGameBoard(){
        String output = "";
        for(int x=0; x!= getGameBoard().length; x++){
            output+= String.format("%-1s %-1s ", "|", x+1);
        }
        output += "|\n";
        for(int i=0; i!= getGameBoard().length; i++){
            for(int j=0; j!= getGameBoard().length; j++){
                output += (String.format("%-1s %-1s ", "|", getGameBoard()[j][i].toString()));
            }
            output += "|\n";
        }
        System.out.println(output);
    }

    public void addTile(int column){
        addClick();
        //column = column-1;
        for(int i=getGameBoard().length-1; i!=-1; i--){
            if(getGameBoard()[column][0].getState() != 0){
                System.out.println("Invalid move! Column is full.");
                break;
            }
            if(getGameBoard()[column][i].getState() == 0){
                getGameBoard()[column][i].setState(getPlayer());
                if(getPlayer() == 1){
                    getGameBoard()[column][i].setIcon(player1);
                } else {
                    getGameBoard()[column][i].setIcon(player2);
                }
                //printGameBoard();
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
                    return player;
                }
            }
        }

        //horizontal check
        for(int i = 0; i < getGameBoard().length; i++){
            for(int j = 0; j < getGameBoard().length-3; j++){
                if(getGameBoard()[j][i].getState() == player && getGameBoard()[j+1][i].getState() == player && getGameBoard()[j+2][i].getState() == player && getGameBoard()[j+3][i].getState() == player){
                    return player;
                }
            }
        }

        //TODO check if these (diag) actually work in weird scenarios.
        //asc diagonal
        for(int i = getGameBoard().length-1; i>4; i--){
            for(int j = 0; j < getGameBoard().length-3; j++){
                if(getGameBoard()[j][i].getState() == player && getGameBoard()[j+1][i-1].getState() == player && getGameBoard()[j+2][i-2].getState() == player && getGameBoard()[j+3][i-3].getState() == player){
                    return player;
                }
            }
        }

        //desc diagonal
        for(int i = 0; i< getGameBoard().length-3; i++){
            for(int j = 0; j < getGameBoard().length-3; j++){
                if(getGameBoard()[j][i].getState()==player && getGameBoard()[j+1][i+1].getState()==player && getGameBoard()[j+2][i+2].getState()==player && getGameBoard()[j+3][i+3].getState()==player){
                    return player;
                }
            }
        }
        return 0;
    }
}
