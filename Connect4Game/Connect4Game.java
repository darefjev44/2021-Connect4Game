import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Scanner;

public class Connect4Game extends JFrame implements MouseListener {

    private boolean gameOver=false;
    static GameBoard gameBoard = new GameBoard();

    public Connect4Game(int boardSize, GameBoard gameBoard){
        super("Connect 4 Game");
        GridLayout layout = new GridLayout(boardSize, boardSize, 0, 0);
        setLayout(layout);

        //add listeners
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                add(gameBoard.getGameBoard()[j][i]);
                gameBoard.getGameBoard()[j][i].addMouseListener(this);
            }
        }

        setSize(boardSize * 72, boardSize * 72);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
    }

    public static void main(String[] args){
        ImageIcon red = new ImageIcon("Connect4Game/RED.gif");
        ImageIcon blue = new ImageIcon("Connect4Game/BLUE.gif");
        int boardSize = 15;
        gameBoard.initializeBoard(boardSize);
        gameBoard.setGameColors(red, blue);
        Connect4Game game = new Connect4Game(boardSize, gameBoard);
        /* OLD MAIN
        int player = 1;

        String playerColor = "RED";

        //TEMP, TODO make it an actual UI
        Scanner scan = new Scanner(System.in);
        TextArea textDisplay = new TextArea();
        Font displayFont = new Font("Monospaced", 12, Font.PLAIN);
        textDisplay.setFont(displayFont);

        //TODO allow player to select board size
        int boardSize = 7;
        GameBoard gameBoard = new GameBoard(boardSize);

        //gameplay loop
        displayGameBoard(gameBoard);
        do{
            System.out.println("Current player: " + player + "(" + playerColor + ")");
            System.out.print("Please choose a column (1-" + boardSize +"): ");
            gameBoard.addTile(player, scan.nextInt());
            displayGameBoard(gameBoard);
            if(gameBoard.checkForWinner(player) == player){
                JOptionPane.showMessageDialog(null, "Player " + player + " (" +playerColor+ ") wins!");
                break;
            }
            if(player == 1){
                player = 2;
                playerColor = "BLUE";
            } else {
                player = 1;
                playerColor = "RED";
            }
        }while(true);*/
    }

    //old method of displaying
    public static void displayGameBoard(GameBoard gameBoard){
        //displays game by adding it to text area row by row
        String output = "";
        for(int x=0; x!= gameBoard.getGameBoard().length; x++){
            output+= String.format("%-1s %-1s ", "|", x+1);
        }
        output += "|\n";
        for(int i=0; i!= gameBoard.getGameBoard().length; i++){
            for(int j=0; j!= gameBoard.getGameBoard().length; j++){
                output += (String.format("%-1s %-1s ", "|", gameBoard.getGameBoard()[j][i].toString()));
            }
            output += "|\n";
        }
        System.out.println(output);
    }

    public void mouseClicked(MouseEvent e) {
        JLabel buttonClicked = (JLabel) e.getSource();
        int colClicked = Integer.parseInt(buttonClicked.getName());
        System.out.println(colClicked);
        gameBoard.addTile(colClicked);
        gameBoard.checkForWinner();
        if(gameBoard.getGameOver()){
            JOptionPane.showMessageDialog(null, "Game over! Winner: Player" + gameBoard.getPlayer());
        }
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {
        JLabel buttonEntered = (JLabel) e.getSource();
        int colEntered = Integer.parseInt(buttonEntered.getName());
        gameBoard.highlightColumn(colEntered);
    }

    public void mouseExited(MouseEvent e) {
        JLabel buttonExited = (JLabel) e.getSource();
        int colExited = Integer.parseInt(buttonExited.getName());
        gameBoard.dehighlightColumn(colExited);
    }
}
