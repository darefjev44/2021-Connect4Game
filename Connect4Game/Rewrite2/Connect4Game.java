package Rewrite2;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Connect4Game extends JFrame implements MouseListener, ActionListener {
    GameBoard gameBoard;
    JMenu fileMenu;
    GameTimer timer;
    JFrame settingsMenu;
    GridLayout mainLayout;
    ArrayList<SimpleGameBoard> gameHistory = new ArrayList<>();
    File gameHistoryFile = new File("game_history.dat");
    File file = new File("saved_state.dat");

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
    JButton settingsStartButton;

    //other UI elements
    TitledBorder title;
    JPanel panel;
    JPanel gamePanel;
    JLabel currentPlayerName;
    JButton mainStartButton;

    //main UI
    public Connect4Game(){
        super("Connect 4 Game");
        mainLayout = new GridLayout();
        setLayout(mainLayout);

        createFileMenu();
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.setLayout(new BorderLayout());
        menuBar.add(fileMenu, BorderLayout.WEST);

        currentPlayerName = new JLabel();
        menuBar.add(currentPlayerName, BorderLayout.EAST);
        currentPlayerName.setVisible(false);
        //not the cleanest solution, but JSeparator was acting very strangely with the menu bar. this looks nice anyways.
        currentPlayerName.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        timer = new GameTimer();
        menuBar.add(timer);

        createStartArea();

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
        //getting rid of any now unnecessary objects
        if(gamePanel != null){
            remove(gamePanel);
        }
        if(gameBoard != null){
            gameBoard = null;
        }
        if(timer != null){
            timer.setVisible(false);
        }

        /* I didn't intend for the button to take up the full area, but I actually like the look of it. */
        gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout());

        mainStartButton = new JButton("Start Game");
        mainStartButton.addActionListener(new ButtonEventHandler());
        gamePanel.add(mainStartButton, BorderLayout.CENTER);

        add(gamePanel);
    }
    public void createGameArea(){
        remove(gamePanel);

        int boardSize = (int)bSize.getValue();

        if(timer!=null){
            getJMenuBar().remove(timer);
            timer = null;
            timer = new GameTimer();
            getJMenuBar().add(timer);
        }

        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(boardSize, boardSize));

        //ensuring players don't get the same colours
        int p1index = p1Colour.getSelectedIndex();
        int p2index = p2Colour.getSelectedIndex();
        if(p1index == p2index){
            while(p1index == p2index){
                p2index = (int)(Math.random()*(imageIcons.length - 1));
            }
            p2Colour.setSelectedIndex(p2index);
            JOptionPane.showMessageDialog(null, "Players cannot be the same colour. Setting player 2 to " + pColorOptions[p2index] + ".");
        }

        timer.setVisible(true);
        timer.startTimer();

        gameBoard = new GameBoard(boardSize);
        gameBoard.setPlayerNames(p1Name.getText(), p2Name.getText());
        gameBoard.setTimeStarted(new GregorianCalendar());
        gameBoard.setPlayerIcons(imageIcons[p1index], imageIcons[p2index]);
        gameBoard.setAIStuff(aiToggle.isSelected(), aiDifficulty.getSelectedIndex());

        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                gamePanel.add(gameBoard.getGameBoard()[j][i]);
                gameBoard.getGameBoard()[j][i].addMouseListener(this);
            }
        }

        settingsMenu.setVisible(false);

        updatePlayerLabel();
        currentPlayerName.setVisible(true);
        /*
        need to poke the main window for it to update for some reason - otherwise borders aren't displayed
        goal with size is for the 7x7 (default) board size to result in window being roughly 500x500
        this might be too big on standard resolutions though as I'm running in 3440*1440
        needs further testing - could possibly grab the user's monitor resolution and do something with that if necessary.
         */
        add(gamePanel);
        this.setSize(72*boardSize, 72 * boardSize + getJMenuBar().getHeight());
    }

    public void createSettingsMenu(){
        settingsMenu = new JFrame("Game Settings");

        GridBagLayout layout = new GridBagLayout();
        settingsMenu.setLayout(layout);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 10, 0, 10);
        settingsMenu.setSize(300, 550);

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
        settingsStartButton = new JButton("Start Game");
        settingsStartButton.addActionListener(new ButtonEventHandler());
        settingsMenu.add(settingsStartButton, c);
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
             * (example 19)
             * */
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

        //just making sure all the bigger fields are the same size and it looks neat :-)
        p1Name.setPreferredSize(p1Colour.getPreferredSize());
        p2Name.setPreferredSize(p1Colour.getPreferredSize());
    }

    public static void main(String[] args){
        Connect4Game game = new Connect4Game();
    }

    //listeners
    public void actionPerformed(ActionEvent e) {
        String menuName;
        menuName = e.getActionCommand();

        switch (menuName) {
            case "New Game":
                createSettingsMenu();
                settingsMenu.setVisible(true);
                break;
            case "Load Game":
                try {
                    loadGame();
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                break;
            case "Save Game":
                try {
                    saveGame();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case "View History":
                try {
                    loadHistory();
                } catch (EOFException ex){
                    //nothing to do here - works as intended?
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
        }
    }

    public void mouseClicked(MouseEvent e) {
        JLabel buttonClicked = (JLabel) e.getSource();
        int colClicked = Integer.parseInt(buttonClicked.getName());
        gameBoard.addTile(colClicked);
        if(gameBoard.checkForWinner() != 0){
            gameEnded();
        }
        if(gameBoard!=null){
            if(!gameBoard.getAIToggle()){
                gameBoard.switchPlayer();
                updatePlayerLabel();
            } else {
                gameBoard.doAIMove();
                if(gameBoard.checkForWinner() != 0){
                    gameEnded();
                } else {
                    gameBoard.switchPlayer();
                    updatePlayerLabel();
                }
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
        if(timer!=null){
            getJMenuBar().remove(timer);
            timer = null;
            timer = new GameTimer();
            getJMenuBar().add(timer);
        }

        SimpleGameBoard simpleGameBoard = new SimpleGameBoard();

        FileInputStream inputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        simpleGameBoard = (SimpleGameBoard) objectInputStream.readObject();

        remove(gamePanel);

        int boardSize = simpleGameBoard.getBoardSize();

        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(boardSize, boardSize));

        timer.setVisible(true);
        timer.setTimeElapsed(simpleGameBoard.getTimeElapsed());
        timer.startTimer();

        gameBoard = new GameBoard(boardSize);
        gameBoard.setPlayerIcons(imageIcons[simpleGameBoard.getPlayer1Icon()], imageIcons[simpleGameBoard.getPlayer2Icon()]);
        gameBoard.setAIStuff(aiToggle.isSelected(), aiDifficulty.getSelectedIndex());
        gameBoard.setPlayerNames(simpleGameBoard.getPlayer1Name(), simpleGameBoard.getPlayer2Name());
        gameBoard.setLastAIMove(simpleGameBoard.getLastAIMove());
        gameBoard.setAIStuff(simpleGameBoard.getAIToggle(), simpleGameBoard.getAiDifficulty());
        gameBoard.setPlayer(simpleGameBoard.getPlayer());
        gameBoard.setTimeStarted(simpleGameBoard.getTimeStarted());
        gameBoard.setPlayerIcons(imageIcons[simpleGameBoard.getPlayer1Icon()], imageIcons[simpleGameBoard.getPlayer2Icon()]);

        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                gamePanel.add(gameBoard.getGameBoard()[j][i]);
                gameBoard.getGameBoard()[i][j].setState(simpleGameBoard.getGameState()[i][j]);
                gameBoard.getGameBoard()[j][i].addMouseListener(this);
            }
        }

        gameBoard.loadIcons();
        settingsMenu.setVisible(false);


        updatePlayerLabel();
        currentPlayerName.setVisible(true);

        add(gamePanel);
        this.setSize(72*boardSize, 72 * boardSize + getJMenuBar().getHeight());
    }

    public void saveGame() throws IOException {
        int boardSize = gameBoard.getGameBoard().length;

        int[][] boardStateAsInt = new int[boardSize][boardSize];
        for(int i = 0; i<boardSize; i++){
            for(int j = 0; j<boardSize; j++){
                boardStateAsInt[i][j] = gameBoard.getGameBoard()[i][j].getState();
            }
        }
        SimpleGameBoard simpleGameBoard = new SimpleGameBoard(gameBoard.getGameBoard().length, boardStateAsInt);
        simpleGameBoard.setAIStuff(gameBoard.getAIToggle(), gameBoard.getAiDifficulty());
        simpleGameBoard.setPlayerIcons(p1Colour.getSelectedIndex(), p2Colour.getSelectedIndex());
        simpleGameBoard.setPlayerNames(gameBoard.getPlayer1Name(), gameBoard.getPlayer2Name());
        simpleGameBoard.setLastAIMove(gameBoard.getLastAIMove());
        simpleGameBoard.setPlayer(gameBoard.getPlayer());
        simpleGameBoard.setTimeStarted(gameBoard.getTimeStarted());
        simpleGameBoard.setTimeElapsed(timer.getTimeElapsed());

        FileOutputStream outputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(simpleGameBoard);
    }

    public void saveToHistory() throws IOException {
        int boardSize = gameBoard.getGameBoard().length;

        int[][] boardStateAsInt = new int[boardSize][boardSize];
        for(int i = 0; i<boardSize; i++){
            for(int j = 0; j<boardSize; j++){
                boardStateAsInt[i][j] = gameBoard.getGameBoard()[i][j].getState();
            }
        }
        SimpleGameBoard simpleGameBoard = new SimpleGameBoard(gameBoard.getGameBoard().length, boardStateAsInt);
        simpleGameBoard.setAIStuff(gameBoard.getAIToggle(), gameBoard.getAiDifficulty());
        simpleGameBoard.setPlayerIcons(p1Colour.getSelectedIndex(), p2Colour.getSelectedIndex());
        simpleGameBoard.setPlayerNames(gameBoard.getPlayer1Name(), gameBoard.getPlayer2Name());
        simpleGameBoard.setLastAIMove(gameBoard.getLastAIMove());
        simpleGameBoard.setPlayer(gameBoard.getPlayer());
        simpleGameBoard.setTimeStarted(gameBoard.getTimeStarted());
        simpleGameBoard.setTimeElapsed(timer.getTimeElapsed());

        gameHistory.add(simpleGameBoard);

        FileOutputStream outputStream = new FileOutputStream(gameHistoryFile);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(gameHistory);
        outputStream.close();
    }

    public void loadHistory() throws IOException, ClassNotFoundException {
        FileInputStream inputStream = new FileInputStream(gameHistoryFile);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        gameHistory = (ArrayList<SimpleGameBoard>) objectInputStream.readObject();
        inputStream.close();

        JFrame historyWindow = new JFrame();
        ArrayList<JPanel> historyPanels = new ArrayList<>();
        historyWindow.setLayout(new GridLayout(gameHistory.size(), 1));
        JPanel panel;
        JLabel jLabel;
        for(int i = 0; i < gameHistory.size(); i++){
            panel = new JPanel();
            jLabel = new JLabel(gameHistory.get(i).getPlayer2Name());
            panel.add(jLabel);
            historyPanels.add(panel);
            historyWindow.add(historyPanels.get(i));
            historyWindow.add(new JSeparator());
        }
        historyWindow.setVisible(true);
    }

    public void updatePlayerLabel(){
        if(gameBoard.getPlayer() == 1){
            currentPlayerName.setText(gameBoard.getPlayer1Name());
        } else if (gameBoard.getPlayer() == 2){
            currentPlayerName.setText(gameBoard.getPlayer2Name());
        }
    }
    //game stuff
    public void gameEnded() {
        currentPlayerName.setVisible(false);

        timer.stopTimer();
        gameBoard.setTimeElapsed(timer.getTimeElapsed());
        timer.resetTimer();
        if(gameBoard.getWinner() == 1 || gameBoard.getWinner() == 2){
            JOptionPane.showMessageDialog(null, "Winner! Player " + gameBoard.getPlayer());
        } else if (gameBoard.getWinner() == 3){
            JOptionPane.showMessageDialog(null, "Draw, you both suck!");
        } else {
            JOptionPane.showMessageDialog(null, "Descriptive error message");
        }
        try{
            saveToHistory();
        } catch (IOException e) {
            e.printStackTrace();
        }

        createStartArea();
        //need to poke main window again to redraw it I guess
        setSize(500, 500 + getJMenuBar().getHeight());
    }
    //menu button handlers
    private class ButtonEventHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == settingsStartButton){
                    System.out.println("GAME SETTINGS: " +
                            "\nPlay VS AI?: " + aiToggle.isSelected() +
                            "\nAI Difficulty: " + aiDifficulty.getSelectedItem() +
                            "\nPlayer 1 Name: " + p1Name.getText() +
                            "\nPlayer 2 Name: " + p2Name.getText() +
                            "\nPlayer 1 Colour: " + p1Colour.getSelectedItem() +
                            "\nPlayer 2 Colour: " + p1Colour.getSelectedItem() +
                            "\nBoard Size: " + bSize.getValue());
                    createGameArea();
            } else if (e.getSource() == mainStartButton){
            createSettingsMenu();
            settingsMenu.setVisible(true);
            }
        }
    }
}
