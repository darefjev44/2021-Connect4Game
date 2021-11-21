package Rewrite;

import javax.swing.*;
import java.io.Serializable;

public class GameTile extends JLabel {
    private int state;

    public GameTile(){
        setState(0);
    }

    public void setState(int state){
        this.state = state;
    }

    public int getState(){
        return state;
    }
}