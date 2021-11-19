import javax.swing.*;

/**
 * Learned how to multi-thread from tutorialspoint.com.
 * Way way WAY simpler than I thought it'd be.
 */

public class GameTimer extends JLabel implements Runnable{
    private Thread timer;
    private int minutes;
    private int seconds;

    public void startTimer(){
        timer = new Thread(this);
        timer.start();
    }

    public void stopTimer(){
        timer.interrupt();
    }

    public void tick(){
        if(seconds==59){
            minutes++;
            seconds=0;
        } else {
            seconds++;
        }
        setText(String.format("%d : %d", seconds, minutes));
    }

    public void run() {
        try {
        for(int i = 0; minutes<59; i++){
            tick();
                Thread.sleep(1000);
        }
        } catch (InterruptedException e) {
            //yep
        }
    }
}
