package Rewrite;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Connect4Game extends JFrame implements MouseListener, ActionListener {
    GameBoard gameBoard;
    JMenu fileMenu;
    GameTimer timer;
    JFrame settingsMenu;
    GridLayout mainLayout;

    //imageIcons
    ImageIcon red = new ImageIcon("Connect4Game/RED.gif");
    ImageIcon blue = new ImageIcon("Connect4Game/BLUE.gif");
    ImageIcon cyan = new ImageIcon("Connect4Game/CYAN.gif");
    ImageIcon green = new ImageIcon("Connect4Game/GREEN.gif");
    ImageIcon magenta = new ImageIcon("Connect4Game/MAGENTA.gif");
    ImageIcon yellow = new ImageIcon("Connect4Game/YELLOW.gif");
    ImageIcon orange = new ImageIcon("Connect4Game/ORANGE.gif");
    ImageIcon[] imageIcons = {red, blue, cyan, green, magenta, yellow, orange};

    //settings fields and stuff
    JCheckBox aiToggle;
    JComboBox aiDifficulty;
    String aiDifficultyOptions[] = {"Very Easy", "Easy"};
    JTextField p1Name;
    JTextField p2Name;
    String pColorOptions[] = {"Red", "Blue", "Cyan", "Green", "Magenta", "Yellow", "Orange"};
    JComboBox p1Colour;
    JComboBox p2Colour;
    SpinnerNumberModel bSizeModel;
    JSpinner bSize;
    JButton startButton;

    //settings panels and stuff and that kinda thing and etc
    TitledBorder title;
    JPanel panel;

    //main UI
    public Connect4Game(){
        super("Connect 4 Game");
        mainLayout = new GridLayout();
        setLayout(mainLayout);

        createFileMenu();
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        menuBar.add(new JSeparator(SwingConstants.VERTICAL));

        timer = new GameTimer();
        menuBar.add(timer);

        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void createFileMenu(){
        JMenuItem item;
        fileMenu = new JMenu("Menu");

        item = new JMenuItem("New Game");
        item.addActionListener(this);
        fileMenu.add(item);

        item = new JMenuItem("Save Game");
        item.addActionListener(this);
        fileMenu.add(item);

        item = new JMenuItem("Load Game");
        item.addActionListener(this);
        fileMenu.add(item);

        JSeparator separator = new JSeparator();
        fileMenu.add(separator);

        item = new JMenuItem("View History");
        item.addActionListener(this);
        fileMenu.add(item);
    }

    public void createStartArea(){

    }
    public void createGameArea(){
        timer.setVisible(true);
        timer.startTimer();
        int p1index = p1Colour.getSelectedIndex();
        int p2index = p2Colour.getSelectedIndex();
        if(p1index == p2index){
            while(p1index == p2index){
                p2index = (int)(Math.random()*(imageIcons.length - 1));
            }
            JOptionPane.showMessageDialog(null, "Players cannot be the same colour. Setting player 2 to " + pColorOptions[p2index] + ".");
        }
        int boardSize = (int)bSize.getValue();
        gameBoard = new GameBoard(boardSize);
        gameBoard.setPlayerNames(p1Name.getText(), p2Name.getText());
        gameBoard.setTimeStarted(new GregorianCalendar());
        gameBoard.setPlayerIcons(imageIcons[p1index], imageIcons[p2index]);
        gameBoard.setAIStuff(aiToggle.isSelected(), aiDifficulty.getSelectedIndex());
        mainLayout.setColumns(gameBoard.getGameBoard().length);
        mainLayout.setRows(gameBoard.getGameBoard().length);

        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                this.add(gameBoard.getGameBoard()[j][i]);
                gameBoard.getGameBoard()[j][i].addMouseListener(this);
            }
        }

        settingsMenu.setVisible(false);

        /*
        need to poke the main window for it to update for some reason - otherwise borders aren't displayed
        goal with size is for the 7x7 (default) board size to result in window being roughly 500x500
        this might be too big on standard resolutions though as I'm running in 3440*1440
        needs further testing - could possibly grab the user's monitor resolution and do something with that if necessary.
         */
        this.setSize(72*boardSize, 72 * boardSize + getJMenuBar().getHeight());
    }

    public void removeGameArea(){
        timer.setVisible(false);
        for(int i = 0; i < gameBoard.getGameBoard().length; i++){
            for(int j = 0; j < gameBoard.getGameBoard().length; j++){
                gameBoard.getGameBoard()[j][i].removeMouseListener(this); //not really necessary but i'm sure it helps with memory at least, if playing a few games in a row.
                this.remove(gameBoard.getGameBoard()[j][i]);
            }
        }
    }

    public void createSettingsMenu(){
        settingsMenu = new JFrame("Game Settings");

        GridBagLayout layout = new GridBagLayout();
        settingsMenu.setLayout(layout);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 10, 0, 10);
        settingsMenu.setSize(300, 550);
        /*
        JLabel sizeLabel = new JLabel("Game Size");
        settingsMenu.add(sizeLabel);

        sizeField = new JTextField();
        sizeField.setSize(100, 30);
        settingsMenu.add(sizeField);*/

        c.weighty = 1;
        c.weightx = 1;
        //ai
        c.gridx = 0;
        c.gridy = 0;
        createAISettings();
        settingsMenu.add(panel, c);

        //players
        c.gridx = 0;
        c.gridy = 1;
        createPlayerSettings();
        settingsMenu.add(panel, c);

        //board settings
        c.gridx = 0;
        c.gridy = 2;
        createBoardSettings();
        settingsMenu.add(panel, c);

        c.gridx = 0;
        c.gridy = 3;
        startButton = new JButton("Start Game");
        startButton.addActionListener(new ButtonEventHandler());
        settingsMenu.add(startButton, c);
    }
    public void createBoardSettings(){
        /* found out about JSpinner and looked it up on the java docs, which also lead me to SpinnerModel/SpinnerNumberModel */
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        title = BorderFactory.createTitledBorder("Board");
        panel.setBorder(title);

        JLabel label;
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 0, 10, 0);

        bSizeModel = new SpinnerNumberModel(7, 4, 24, 1);
        //board size
        //label
        label = new JLabel("Board Size");
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(label, c);
        //spinner
        bSize = new JSpinner(bSizeModel);
        c.weightx = 2;
        c.gridx = 1;
        c.gridy = 0;
        panel.add(bSize, c);
        bSize.addChangeListener(e -> {
            /** ensures the board size input doesn't exceed mins/maxes and removes the need for validation elsewhere.
             * for some reason getMaximum must be cast to a Number before it can be cast to an int, discovered this solution
             * after seeing a code snippet here: https://www.programcreek.com/java-api-examples/?api=javax.swing.SpinnerNumberModel
             * (example 19) */
            int value, max, min;
            Number maxAsNum, minAsNum;
            if(e.getSource() == bSize){
                for(int i = 0; i < bSize.getValue().toString().length(); i++){
                    if(!Character.isDigit(bSize.getValue().toString().charAt(i))){
                        bSize.setValue(7);
                    }
                }
                value = (int)bSize.getValue();
                maxAsNum = (Number)bSizeModel.getMaximum();
                minAsNum = (Number)bSizeModel.getMinimum();
                max = (int)maxAsNum;
                min = (int)minAsNum;
                if(value > max){
                    bSize.setValue(max);
                } else if(value < min){
                    bSize.setValue(min);
                }
            }
        });
    }

    public void createAISettings(){
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        title = BorderFactory.createTitledBorder("AI");
        panel.setBorder(title);

        JLabel label;
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 0, 10, 0);

        label = new JLabel("Play VS AI?");
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(label, c);
        aiToggle = new JCheckBox();
        c.weightx = 2;
        c.gridx = 1;
        c.gridy = 0;
        panel.add(aiToggle, c);
        aiToggle.addActionListener(this);

        label = new JLabel("AI Difficulty");
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(label, c);
        aiDifficulty = new JComboBox(aiDifficultyOptions);
        c.weightx = 2;
        c.gridx = 1;
        c.gridy = 1;
        panel.add(aiDifficulty, c);
        aiDifficulty.addActionListener(this);
    }

    public void createPlayerSettings(){
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        title = BorderFactory.createTitledBorder("Players");
        panel.setBorder(title);

        JLabel label;
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 0, 10, 0);

        label = new JLabel("Player 1 Name");
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(label, c);
        p1Name = new JTextField();
        c.weightx = 2;
        c.gridx = 1;
        c.gridy = 0;
        panel.add(p1Name, c);
        p1Name.addActionListener(this);

        label = new JLabel("Player 2 Name");
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(label, c);
        p2Name = new JTextField();
        c.weightx = 2;
        c.gridx = 1;
        c.gridy = 1;
        panel.add(p2Name, c);
        p2Name.addActionListener(this);

        c.insets = new Insets(0, 0, 0, 0);
        c.weightx = 1;
        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(220,1));
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        panel.add(separator, c);

        c.insets = new Insets(10, 0, 10, 0);
        c.gridwidth = 1;
        label = new JLabel("Player 1 Colour");
        c.gridx = 0;
        c.gridy = 3;
        panel.add(label, c);
        p1Colour = new JComboBox(pColorOptions);
        c.weightx = 2;
        c.gridx = 1;
        c.gridy = 3;
        panel.add(p1Colour, c);
        p1Colour.addActionListener(this);

        label = new JLabel("Player 2 Colour");
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 4;
        panel.add(label, c);
        p2Colour = new JComboBox(pColorOptions);
        c.weightx = 2;
        c.gridx = 1;
        c.gridy = 4;
        panel.add(p2Colour, c);
        p2Colour.addActionListener(this);
    }
    public static void main(String[] args){
        Connect4Game game = new Connect4Game();
    }

    //listeners
    public void actionPerformed(ActionEvent e) {
        String menuName;
        menuName = e.getActionCommand();

        if(menuName.equals("New Game")){
            createSettingsMenu();
            settingsMenu.setVisible(true);
        } else if (menuName.equals("Load Game")){
            try {
                loadGame();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        } else if (menuName.equals("Save Game")){
            try {
                saveGame();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        JLabel buttonClicked = (JLabel) e.getSource();
        int colClicked = Integer.parseInt(buttonClicked.getName());
        System.out.println("Clicked: " + colClicked);
        gameBoard.addTile(colClicked);
        if(gameBoard.checkForWinner() != 0){
            gameEnded();
        }
        if(!gameBoard.getAIToggle()){
            gameBoard.switchPlayer();
        } else {
            gameBoard.doAIMove();
            if(gameBoard.checkForWinner() != 0){
                gameEnded();
            } else {
                gameBoard.switchPlayer();
            }
        }
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {
    }

    //load/save game
    public void loadGame() throws IOException, ClassNotFoundException {
        File savedGame = new File("saved_game.dat");
        FileInputStream inStream = new FileInputStream(savedGame);
        ObjectInputStream objectInStream = new ObjectInputStream(inStream);
        gameEnded();
        gameBoard = (GameBoard) objectInStream.readObject();
        timer.setTimeElapsed(gameBoard.getTimeElapsed());
        timer.startTimer();
    }
    public void saveGame() throws IOException {
        File savedGame = new File("saved_game.dat");
        FileOutputStream outputStream = new FileOutputStream(savedGame);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        timer.stopTimer();
        gameBoard.setTimeElapsed(timer.getTimeElapsed());
        objectOutputStream.writeObject(gameBoard);
    }
    //game stuff
    public void gameEnded(){
        timer.stopTimer();
        gameBoard.setTimeElapsed(timer.getTimeElapsed());
        timer.resetTimer();
        if(gameBoard.getWinner() > 0){
            JOptionPane.showMessageDialog(null, "Winner! Player " + gameBoard.getPlayer());
        }
        //save to file
        removeGameArea();
        //need to poke main window again to redraw it I guess
        setSize(500, 500 + getJMenuBar().getHeight());
    }
    //menu button handlers
    private class ButtonEventHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == startButton){
                System.out.println("GAME SETTINGS: " +
                        "\nPlay VS AI?: " + aiToggle.isSelected() +
                        "\nAI Difficulty: " + aiDifficulty.getSelectedItem() +
                        "\nPlayer 1 Name: " + p1Name.getText() +
                        "\nPlayer 2 Name: " + p2Name.getText() +
                        "\nPlayer 1 Colour: " + p1Colour.getSelectedItem() +
                        "\nPlayer 2 Colour: " + p1Colour.getSelectedItem() +
                        "\nBoard Size: " + bSize.getValue());
                if(gameBoard != null){
                    removeGameArea();
                }
                createGameArea();
            }
        }
    }
}
