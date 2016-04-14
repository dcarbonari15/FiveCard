package edu.up.cs301.FCDGame;

import edu.up.cs301.game.GamePlayer;

/**
 * Created by carbonar19 on 4/11/2016.
 */
public class FCDRaiseAction extends FCDMoveAction {
    private int amountRaised;

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public FCDRaiseAction(GamePlayer player, int raise) {

        super(player);
        amountRaised = raise;
    }
    /**
     * @return
     * 		whether this action is a "raise" move
     */
    public boolean isRaise(){
        return true;
    }

    public int getAmountRaised(){
        return amountRaised;
    }
}
