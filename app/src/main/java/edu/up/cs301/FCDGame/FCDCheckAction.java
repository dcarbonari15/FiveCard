package edu.up.cs301.FCDGame;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created by carbonar19 on 4/12/2016.
 */
public class FCDCheckAction extends GameAction {

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public FCDCheckAction(GamePlayer player) {
        super(player);
    }

    public boolean isCheck(){
        return true;
    }
}
