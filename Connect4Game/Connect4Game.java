import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Connect4Game extends JFrame implements MouseListener, ActionListener {

    private boolean gameOver=false;
    private int winner = 0;
    static GameBoard gameBoard = new GameBoard();
    JMenu fileMenu;

    public Connect4Game(int boardSize, GameBoard gameBoard){
        super("Connect 4 Game");
        GridLayout layout = new GridLayout(boardSize+1, boardSize, 0, 0);
        setLayout(layout);

        createFileMenu();
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        add(menuBar);

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

    private void createFileMenu(){
        JMenuItem item;
        fileMenu = new JMenu("File");
        item = new JMenuItem("First");
        item.addActionListener(this);
        fileMenu.add(item);
    }


    public static void main(String[] args){
        ImageIcon red = new ImageIcon("Connect4Game/RED.gif");
        ImageIcon blue = new ImageIcon("Connect4Game/BLUE.gif");
        int boardSize = 7;
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

    public void mouseClicked(MouseEvent e) {
        JLabel buttonClicked = (JLabel) e.getSource();
        int colClicked = Integer.parseInt(buttonClicked.getName());
        gameBoard.addTile(colClicked);
        if(gameBoard.checkForWinner() != 0){
            JOptionPane.showMessageDialog(null, "Winner! Player " + gameBoard.getPlayer());
        }
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    //highlight & dehighlight columns when moused over - todo maybe add a preview of your placement?
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

    public void actionPerformed(ActionEvent e) {

    }
}
