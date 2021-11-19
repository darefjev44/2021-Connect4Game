import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Connect4Game extends JFrame implements MouseListener, ActionListener {
    static GameBoard gameBoard = new GameBoard();
    JMenu fileMenu;
    GameTimer timer;

    public Connect4Game(int boardSize, GameBoard gameBoard){
        super("Connect 4 Game");
        GridLayout layout = new GridLayout(boardSize, boardSize);
        setLayout(layout);

        //menu bar
        createFileMenu();
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        menuBar.add(new JSeparator(SwingConstants.VERTICAL));

        timer = new GameTimer();
        timer.startTimer();
        menuBar.add(timer);

        //game tiles
        for(int i = 0; i < gameBoard.getGameBoard().length; i++){
            for(int j = 0; j < gameBoard.getGameBoard().length; j++){
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
    }

    public void mouseClicked(MouseEvent e) {
        JLabel buttonClicked = (JLabel) e.getSource();
        int colClicked = Integer.parseInt(buttonClicked.getName());
        gameBoard.addTile(colClicked);
        if(gameBoard.checkForWinner() != 0){
            timer.stopTimer();
            JOptionPane.showMessageDialog(null, "Winner! Player " + gameBoard.getPlayer());
        } else {
            gameBoard.switchPlayer();
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
