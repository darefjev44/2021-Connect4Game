package Game;

import javax.swing.*;

/**
 * An instantiable class which defines a Game Tile.
 * Doesn't contain that much here as its main purpose is to just store an integer corresponding to the tile's state.
 * It extends JLabel so that each GameTile may also have an associated name and ImageIcon, and be added directly to
 * UI without needing to create extra objects.
 *
 * @author Daniel Arefjev
 */
public class GameTile extends JLabel {
    private int state;

    /**
     * Default constructor for GameTile. It calls the setState() method, with a value of 0.
     */
    public GameTile(){
        setState(0);
    }

    /**
     * Method to set the state of a GameTile object.
     * @param state the state to set the GameTIle to.
     */
    public void setState(int state){
        this.state = state;
    }

    /**
     * Method to get the state of a GameTile object.
     * @return Returns state, an integer value which corrosponds to the state of the GameTile. Where:
     * 0 is "unoccupied"
     * 1 is "taken" by Player 1.
     * 2 is "taken" by Player 2.
     */
    public int getState(){
        return state;
    }
}