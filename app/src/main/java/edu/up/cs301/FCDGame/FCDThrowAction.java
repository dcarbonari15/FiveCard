package edu.up.cs301.FCDGame;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created by carbonar19 on 4/11/2016.
 */
public class FCDThrowAction extends FCDMoveAction {
    private int[] indexOfThrow = new int[5];
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public FCDThrowAction(GamePlayer player, int[] indecies) {

        super(player);
        for(int i = 0; i < indecies.length; i++){
            indexOfThrow[i] = indecies[i];
        }
    }
    /**
     * @return
     * 		whether this action is a "Throw" move
     */
    
    public boolean isThrow(){
        return true;
    }

    public int[] getIndexOfThrow(){
        return indexOfThrow;
    }
}
