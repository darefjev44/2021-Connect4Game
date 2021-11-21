package Rewrite2;

import javax.swing.*;
import java.util.GregorianCalendar;

/**
 * An object which contains just the results of a finished game (along with the player information), to be stored in the game history file.
 */

public class FinishedGameBoard {
    private int winner;
    private String player1Name, player2Name;
    private GregorianCalendar timeStarted, timeElapsed;
    private boolean aiToggle;
    private int aiDifficulty;

    public FinishedGameBoard(int winner, String player1Name, String player2Name, GregorianCalendar timeStarted, GregorianCalendar timeElapsed){

    }

    public String getPlayer1Name(){
        return player1Name;
    }
    public String getPlayer2Name(){
        return player2Name;
    }
}
