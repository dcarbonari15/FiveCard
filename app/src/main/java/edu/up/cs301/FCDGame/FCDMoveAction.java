package edu.up.cs301.FCDGame;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created by carbonar19 on 4/11/2016.
 */
public abstract class FCDMoveAction extends GameAction {

    private static final long serialVersionUID = -3107100271012188849L;
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public FCDMoveAction(GamePlayer player) {
        super(player);
    }

    /**
     * @return
     * 		whether the move was a fold
     */

    public boolean isFold(){
        return false;
    }

    /**
     * @return
     * 		whether the move was a bet
     */

    public boolean isBet(){
        return false;
    }

    /**
     * @return
     * 		whether the move was a raise
     */

    public boolean isRaise(){
        return false;
    }

    /**
     * @return
     * 		whether the move was a throw
     */

    public boolean isThrow(){
        return false;
    }

    /**
     * @return
     * 		whether the move was a call
     */
    public boolean isCall(){
        return false;
    }

    /**
     * @return
     * 		whether the move was a check
     */
    public boolean isCheck(){
        return false;
    }
}
