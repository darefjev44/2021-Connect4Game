package Game;

import javax.swing.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * An instantiable class which defines a Game Timer.
 * It runs on a separate thread to the rest of the game using Runnable, so that it can serve as essentially a
 * stopwatch to track how long a game has been running for. It extends JLabel so that it may be added to the UI
 * simpler, and while the timer is running the (JLabel) text is updated every time the timer "ticks".
 *
 * Learned how to use multithreading/Runnable at https://www.tutorialspoint.com/java/java_multithreading.htm aswell as
 * the java docs.
 *
 * @author Daniel Arefjev
 */
public class GameTimer extends JLabel implements Runnable {
    private Thread timer;
    private GregorianCalendar timeElapsed;
    private boolean timerRunning;

    /**
     * Default no-argument constructor for GameTimer().
     * It initializes timeElapsed with a GregorianCalendar() object using the default constructor and then sets all
     * the values to 0. (Mostly caring about the minutes/seconds)
     */
    public GameTimer(){
        this.timeElapsed = new GregorianCalendar();
        this.timeElapsed.set(0,0,0,0,0,0);
    }

    /**
     * Method which gets the time elapsed within a GameTimer object.
     * @return timeElapsed, an instance of GregorianCalendar - which stores the amount of time the GameTimer has been
     * "ticking" for.
     */
    public GregorianCalendar getTimeElapsed(){
        return timeElapsed;
    }

    /**
     * Method to set the time elapsed within a GameTimer object.
     * Mostly used when loading a game save file in the Connect4Game class.
     * @param timeElapsed the timeElapsed to set within a GameTimer object.
     */
    public void setTimeElapsed(GregorianCalendar timeElapsed){
        this.timeElapsed = timeElapsed;
    }

    /**
     * Method which sets timerRunning to true, creates a new thread called timer, and starts the thread.
     */
    public void startTimer(){
        this.timerRunning = true;
        timer = new Thread(this);
        timer.start();
    }

    /**
     * Method which sets timerRunning to false, and interrupts the running thread.
     */
    public void stopTimer(){
        this.timerRunning = false;
        timer.interrupt();
    }

    /**
     * @deprecated Not used as any timer objects being used in the Connect4Game class are instead set to null
     * and created again to reset the timer, as to avoid creating multiple threads so that the timer doesn't tick
     * x times as fast.
     */
    @Deprecated public void resetTimer(){
        stopTimer();
        this.timeElapsed.set(0,0,0,0,0,0);
    }

    /**
     * Method which adds 1 second to timeElapsed, and updates the (JLabel) text of a GameTimer object accordingly.
     */
    public void tick(){
        this.timeElapsed.add(Calendar.SECOND, 1);
        setText(String.format("%02d:%02d", timeElapsed.get(Calendar.MINUTE), timeElapsed.get(Calendar.SECOND)));
    }

    /**
     * Method which runs on a thread - calls the tick() method every second.
     */
    public void run() {
        try {
        while(timerRunning){
            tick();
            Thread.sleep(1000);
        }
        } catch (InterruptedException e) {
            //yep
        }
    }
}
