package edu.up.cs301.FCDGame;

import edu.up.cs301.game.GamePlayer;

/**
 * Created by carbonar19 on 4/11/2016.
 */
public class FCDBetAction extends FCDMoveAction {
    private int bet;
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public FCDBetAction(GamePlayer player, int bet) {

        super(player);
        this.bet = bet;
    }
    /**
     * @return
     * 		whether this action is a "bet" move
     */
    public boolean isBet(){
        return true;
    }

    public int getbet(){
        return bet;
    }
}
