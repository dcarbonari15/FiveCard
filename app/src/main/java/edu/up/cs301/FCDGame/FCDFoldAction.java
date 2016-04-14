package edu.up.cs301.FCDGame;

import edu.up.cs301.game.GamePlayer;

/**
 * Created by carbonar19 on 4/11/2016.
 */
public class FCDFoldAction extends FCDMoveAction {
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public FCDFoldAction(GamePlayer player) {
        super(player);
    }
    /**
     * @return
     * 		whether this action is a "fold" move
     */

    public boolean isFold(){
        return true;
    }
}
