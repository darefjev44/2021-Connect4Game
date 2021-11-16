import javax.swing.*;

public class GameTile extends JLabel {
    private int state;

    public GameTile(int state){
        setState(state);
    }

    public GameTile(){
        setState(0);
    }

    public String toString(){
        String str;
        switch (getState()){
            case 0:
                str = " ";
                break;
            case 1:
                str = "R";
                break;
            case 2:
                str = "B";
                break;
            default:
                str = "?";
                break;
        }
        return str;
    }

    public void setState(int state){
        this.state = state;
    }

    public int getState(){
        return state;
    }
}
