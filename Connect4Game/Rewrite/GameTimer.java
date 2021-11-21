package Rewrite;

import javax.swing.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Learned how to multi-thread from tutorialspoint.com.
 * Way way WAY simpler than I thought it'd be.
 */

public class GameTimer extends JLabel implements Runnable {
    private Thread timer;
    private GregorianCalendar timeElapsed;
    private boolean timerRunning;

    public GameTimer(){
        this.timeElapsed = new GregorianCalendar();
        this.timeElapsed.set(0,0,0,0,0,0);
    }

    public GregorianCalendar getTimeElapsed(){
        return timeElapsed;
    }

    public void setTimeElapsed(GregorianCalendar timeElapsed){
        this.timeElapsed = timeElapsed;
    }

    public void startTimer(){
        this.timerRunning = true;
        timer = new Thread(this);
        timer.start();
    }

    public void stopTimer(){
        this.timerRunning = false;
        timer.interrupt();
    }

    public void resetTimer(){
        stopTimer();
        this.timeElapsed.set(0,0,0,0,0,0);
    }

    public void tick(){
        this.timeElapsed.add(Calendar.SECOND, 1);
        setText(String.format("%02d:%02d", timeElapsed.get(Calendar.MINUTE), timeElapsed.get(Calendar.SECOND)));
    }

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
