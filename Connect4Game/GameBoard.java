import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameBoard {
    private GameTile[][] gameTiles;
    private ImageIcon player1;
    private ImageIcon player2;
    private BufferedImage bufferedImage1;
    private BufferedImage bufferedImage2;
    private int player;
    private boolean gameOver;
    private int winner;

    public GameBoard(){
        setPlayer(1);
    }

    public GameBoard(int boardSize){
        initializeBoard(boardSize);
    }

    public void setGameColors(ImageIcon player1, ImageIcon player2){
        this.player1 = player1;
        this.player2 = player2;
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

    public void setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }

    public boolean getGameOver(){
        return gameOver;
    }

    public void setWinner(int winner){
        this.winner = winner;
        if(winner > 0){
            setGameOver(true);
        }
    }

    public int getWinner(){
        return winner;
    }

    public GameTile[][] getGameBoard(){
        return gameTiles;
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

    public void addTile(int column){
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
                switchPlayer();
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

    public void checkForWinner(){
        int player = getPlayer();
        /*
        0 - game still ongoing
        1 - red wins
        2 - blue wins
        3 - draw
        */

        //vertical check
        for(int i = 0; i < getGameBoard().length; i++){
            for(int j = 0; j < getGameBoard().length-3; j++){
                if(getGameBoard()[i][j].getState() == player && getGameBoard()[i][j+1].getState() == player && getGameBoard()[i][j+2].getState() == player && getGameBoard()[i][j+3].getState() == player){
                    setWinner(player);
                    System.out.println("Winner found");
                }
            }
        }

        //horizontal check
        for(int i = 0; i < getGameBoard().length; i++){
            for(int j = 0; j < getGameBoard().length-3; j++){
                if(getGameBoard()[j][i].getState() == player && getGameBoard()[j+1][i].getState() == player && getGameBoard()[j+2][i].getState() == player && getGameBoard()[j+3][i].getState() == player){
                    setWinner(player);
                }
            }
        }

        //TODO check if these (diag) actually work in weird scenarios.
        //asc diagonal
        for(int i = getGameBoard().length-1; i>4; i--){
            for(int j = 0; j < getGameBoard().length-3; j++){
                if(getGameBoard()[j][i].getState() == player && getGameBoard()[j+1][i-1].getState() == player && getGameBoard()[j+2][i-2].getState() == player && getGameBoard()[j+3][i-3].getState() == player){
                   setWinner(player);
                }
            }
        }

        //desc diagonal
        for(int i = 0; i< getGameBoard().length-3; i++){
            for(int j = 0; j < getGameBoard().length-3; j++){
                if(getGameBoard()[j][i].getState()==player && getGameBoard()[j+1][i+1].getState()==player && getGameBoard()[j+2][i+2].getState()==player && getGameBoard()[j+3][i+3].getState()==player){
                    setWinner(player);
                }
            }
        }
    }
}
