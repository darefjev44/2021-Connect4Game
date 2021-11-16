import javax.swing.*;
import java.awt.*;

public class GameBoard {
    private GameTile[][] gameTiles;

    public GameBoard(){
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
            }
        }
    }

    public GameTile[][] getGameBoard(){
        return gameTiles;
    }

    public void addTile(int player, int column){
        //column = column-1;
        for(int i=getGameBoard().length-1; i!=-1; i--){
            if(getGameBoard()[column][0].getState() != 0){
                System.out.println("Invalid move! Column is full.");
                break;
            }
            if(getGameBoard()[column][i].getState() == 0){
                getGameBoard()[column][i].setState(player);
                getGameBoard()[column][i].setBackground(Color.BLUE);
                getGameBoard()[column][i].setOpaque(true);
                break;
            }
        }
    }

    public int checkForWinner(int player){
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
