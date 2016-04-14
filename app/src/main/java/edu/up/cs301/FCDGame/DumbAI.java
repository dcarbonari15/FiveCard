package edu.up.cs301.FCDGame;

import android.util.Log;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;

/**
 * The dumb computerized game player player. This the calss that will contain the "dumb" AI
 * that will be hard to lose to.
 *
 * @author David Carbonari
 * @author Ryan Dehart
 * @author Gabe Hall
 * @version March 2016
 */
public  class DumbAI extends GameComputerPlayer{
    private double pause;
    private GameAction lastTriedMove;

    private FCDState savedState;
    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public DumbAI(String name) {


        this(name, 2.0);
    }

    public DumbAI(String name, Double pause){
        super(name);

        this.pause = pause * 1000;
    }

//    @Override
//    protected void timerTicked(){
//
//    }
//

    @Override
    protected void receiveInfo(GameInfo info) {
        if(!(info instanceof FCDState)){
            if(info instanceof IllegalMoveInfo){
                if(lastTriedMove instanceof FCDCallAction){
                    game.sendAction(new FCDCheckAction(this));
                }else if(lastTriedMove instanceof FCDCheckAction){
                    game.sendAction(new FCDCallAction(this));
                }
            }else{
                return;
            }
        }

        savedState = (FCDState)info;


        Card[] hand = savedState.getPlayer1Hand(savedState.getActivePlayer());
        int whatToDo = (int)(Math.random() * 3);
        int bet = (int)(Math.random() * (savedState.getLobby().get(savedState.getActivePlayer()).getMoney()))
                + savedState.getLastBet();
        if(savedState.getGameStage() == 1){
            if(whatToDo == 0){
                savedState.playerFolds(savedState.getActivePlayer());
                game.sendAction(new FCDFoldAction(this));
                lastTriedMove = new FCDFoldAction(this);
                Log.i("AI move", "AI folds");
            }else if(whatToDo == 1){
                savedState.playerCalls(savedState.getActivePlayer(), savedState.getLastBet());
                game.sendAction(new FCDCallAction(this));
                lastTriedMove = new FCDCallAction(this);
                Log.i("AI move", "AI calls");
            }else if(whatToDo == 2){
                savedState.playerRaises(savedState.getActivePlayer(), savedState.getLastBet(), bet);
                game.sendAction(new FCDRaiseAction(this, bet));
                lastTriedMove = new FCDRaiseAction(this, bet);
                Log.i("AI move", "AI raises");
            }else{
                game.sendAction(new FCDCheckAction(this));
                lastTriedMove = new FCDCheckAction(this);
                Log.i("AI move", "AI checks");
            }
        }else if(savedState.getGameStage() == 2){
            int[] indexToThrow = new int[5];
            int numCardsToThrow = (int)(Math.random() * 5) + 1;
            for(int i = 1; i <= numCardsToThrow; i++){
                indexToThrow[i] = i;
            }
            Card[] playerhand = savedState.getPlayer1Hand(savedState.getActivePlayer());
            Card[] handToThrow = savedState.getPlayer1Hand(savedState.getActivePlayer());
            for(int i = 0; i < 5; i++){
                if(i == indexToThrow[i]){
                    handToThrow[i] = playerhand[i];
                }
            }
            savedState.playerDiscards(savedState.getActivePlayer(), handToThrow);
            game.sendAction(new FCDThrowAction(this,indexToThrow));
        }else if(savedState.getGameStage() == 3){
            if(whatToDo == 0){
                savedState.playerFolds(savedState.getActivePlayer());
                game.sendAction(new FCDFoldAction(this));
                lastTriedMove = new FCDFoldAction(this);
            }else if(whatToDo == 1){
                savedState.playerCalls(savedState.getActivePlayer(), savedState.getLastBet());
                game.sendAction(new FCDCallAction(this));
                lastTriedMove = new FCDCallAction(this);
            }else if(whatToDo == 2){
                savedState.playerRaises(savedState.getActivePlayer(), savedState.getLastBet(), bet);
                game.sendAction(new FCDRaiseAction(this, bet));
                lastTriedMove = new FCDRaiseAction(this, bet);
            }else{
                game.sendAction(new FCDCheckAction(this));
                lastTriedMove = new FCDCheckAction(this);
            }
        }
    }
}
