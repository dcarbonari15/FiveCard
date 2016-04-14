package edu.up.cs301.FCDGame;

import android.util.Log;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created by carbonar19 on 3/16/2016.
 */
public class FCDLocal extends LocalGame implements FCDGame {

    public FCDState state;

    public FCDLocal(){
        state = new FCDState();
        state.shuffle();
        state.shuffle();
        state.dealCards();
    }



    @Override
    protected String checkIfGameOver() {
        if(!(state.getGameStage() == 6)){
            return null;
        }else{
            checkIfRoundOver();
            for(Player p:state.getLobby()){
                if(p.getMoney() == 500 * state.getLobby().size()){
                    return "player " + state.getLobby().indexOf(p) + " wins!";
                }
            }
        }
        return null;

    }

    protected String checkIfRoundOver(){
        if(!(state.getGameStage() == 6)){
            return null;
        }else{
            int nextPlayerHand;
            int bestHand = 0;
            int bestHandIndex = 0;
            for(Player p: state.getLobby()){
                nextPlayerHand = state.handValue(p.getHand());
                if(nextPlayerHand >= bestHand){
                    bestHand = nextPlayerHand;
                    bestHandIndex = state.getLobby().indexOf(p);
                }
            }
            int winningPlayerMoney = state.getLobby().get(bestHandIndex).getMoney();
            state.getLobby().get(bestHandIndex).setMoney(winningPlayerMoney + state.getPot());
            state.setPot(0);
            return "player "  + bestHandIndex + " wins $" + state.getPot() + "!!!";
        }
    }


    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        if(state == null){
            return;
        }

        FCDState stateForPlayer = new FCDState(state);

        p.sendInfo(stateForPlayer);

        Log.i("sendUpdatedState", "Sent the new state");

    }

    @Override
    protected boolean canMove(int playerIdx) {
        if(playerIdx < 0 || state.getLobby().size() < playerIdx){
            return false;
        }else{
            return state.getActivePlayer() == playerIdx;
        }
    }



    @Override
    protected boolean makeMove(GameAction action) {
        if(!(action instanceof FCDMoveAction)){
            return false;
        }
        FCDMoveAction move = (FCDMoveAction) action;

        int playerIndex = getPlayerIdx(move.getPlayer());
        if(playerIndex < 0){
            return false;
        }

        if(move.isFold()){
            state.getLobby().get(playerIndex).fold();
            if(playerIndex == 0) {
                state.setActivePlayer(1);
            }else{
                state.setActivePlayer(0);
            }
            for(Player p: state.getLobby()){
                sendUpdatedStateTo(p);
            }
            return true;
        }else if(move.isBet()){
            int playersMoney = state.getLobby().get(playerIndex).getMoney();
            int pot = state.getPot();
            if((playersMoney) - ((FCDBetAction)move).getbet() < 0){
                return false;
            }else {
                state.getLobby().get(playerIndex).setMoney(playersMoney - ((FCDBetAction) move).getbet());
                state.setPot(pot + ((FCDBetAction) move).getbet());
                if(playerIndex == 0) {
                    state.setActivePlayer(1);
                }else{
                    state.setActivePlayer(0);
                }
                for(Player p: state.getLobby()){
                    sendUpdatedStateTo(p);
                }
                return true;
            }

        }else if(move.isCall()){
            int tableBet = state.getLastBet();
            int playerMoney = state.getPlayerMoney(playerIndex);
            int pot = state.getPot();
            if (playerMoney <= 0){
                return false;
            } else if (playerMoney - tableBet < 0){
                state.setPot(state.getPot() + playerMoney);
                state.setPlayerMoney(0, playerIndex);
                if(playerIndex == 0) {
                    state.setActivePlayer(1);
                }else{
                    state.setActivePlayer(0);
                }
                for(Player p: state.getLobby()){
                    sendUpdatedStateTo(p);
                }
                return true;
            } else {
                state.setPot(pot + tableBet);
                state.setPlayerMoney(state.getPlayerMoney(playerIndex) - tableBet, playerIndex);
                if(playerIndex == 0) {
                    state.setActivePlayer(1);
                }else{
                    state.setActivePlayer(0);
                }
                for(Player p: state.getLobby()){
                    sendUpdatedStateTo(p);
                }
                return true;
            }
        }else if(move.isThrow()){
            if(state.getGameStage() != 2){
                return false;
            }
            int[] cardsToDiscard = ((FCDThrowAction)move).getIndexOfThrow();
            Card[] cards = new Card[5];
            for(int i = 0; i < cardsToDiscard.length; i++){
                cards[i] = state.getLobby().get(playerIndex).getCard(cardsToDiscard[i]);
            }
            state.playerDiscards(playerIndex, cards);
            if(playerIndex == 0) {
                state.setActivePlayer(1);
            }else{
                state.setActivePlayer(0);
            }
            for(Player p: state.getLobby()){
                sendUpdatedStateTo(p);
            }
            return true;
        }else if(move.isCheck()){
            if(state.getLastBet() > 0){
                return false;
            }else{
                if(playerIndex == 0) {
                    state.setActivePlayer(1);
                }else{
                    state.setActivePlayer(0);
                }
                for(Player p: state.getLobby()){
                    sendUpdatedStateTo(p);
                }
                return true;
            }
        }else if(move.isRaise()) {
            int tableBet = state.getLastBet();
            int playerMoney = state.getPlayerMoney(playerIndex);
            int pot = state.getPot();
            if ((playerMoney + ((FCDRaiseAction) move).getAmountRaised()) - tableBet < 0) {
                return false;
            } else {
                state.setPot(pot + tableBet + ((FCDRaiseAction) move).getAmountRaised());
                state.setPlayerMoney(playerMoney - tableBet - ((FCDRaiseAction) move).getAmountRaised()
                        , playerIndex);
                if(playerIndex == 0) {
                    state.setActivePlayer(1);
                }else{
                    state.setActivePlayer(0);
                }
                for(Player p: state.getLobby()){
                    sendUpdatedStateTo(p);
                }
                return true;
            }
        }

        return false;

    }
}
