package edu.up.cs301.FCDGame;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.card.Suit;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * The player class. This the class that contains all the pertinent information for all the players
 * Both the computer players and the human players will extend from this class.
 *
 * @author David Carbonari
 * @author Ryan Dehart
 * @author Gabe Hall
 * @version March 2016
 */
public class Player implements GamePlayer{

    //instance variables
    private int bet; //the player's bet
    private int money; //the player's money
    private int handValue; //hand rank
    private int subHandValue; //
    private int raiseAmount; //the amount the player is raising

    private boolean fold; //holds whether player has folded

    private Card[] hand; //player's hand

    /**
     * ctor
     *
     * basic ctor
     */
    public Player(){
        this.bet = 0;
        this.money = 500;
        this.fold = false;
        this.raiseAmount = 0;
        this.hand = new Card[]{new Card(Rank.FIVE, Suit.Diamond), new Card(Rank.ACE, Suit.Club), new Card(Rank.THREE, Suit.Diamond),
                new Card(Rank.FOUR, Suit.Spade), new Card(Rank.TWO, Suit.Diamond)};
    }

    /**
     * ctor
     *
     * @param player
     *      the player we are copying
     * @param state
     *      the state holding the player
     */
    public Player(Player player, FCDState state){
        this.bet = player.getBet();
        this.money = player.getMoney();
        this.hand = player.getHand();
        this.handValue = state.handValue(player.getHand());
        this.subHandValue = state.subHandValue(player.getHand());
        this.fold = player.isFold();
        this.raiseAmount = player.getRaiseAmount();
    }

    //fold
    public void fold(){
        this.fold = true;
    }


    //getters and setters

    public void setMoney(int money){
        this.money = money;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public void setHand(Card[] cards){
        int index = 0;
        for(Card c:cards){
            hand[index] = c;
            index++;
        }
    }

    public int getRaiseAmount() {
        return raiseAmount;
    }

    public void setRaiseAmount(int raiseAmount) {
        this.raiseAmount = raiseAmount;
    }

    public void setCard(int index, Card card){
        hand[index] = card;
    }

    public Card getCard(int index){
        if (index >= 0) {
            return hand[index];
        } else {
            return null;
        }
    }

    public int getBet() {
        return bet;
    }

    public int getMoney() {
        return money;
    }

    public boolean isFold() {
        return fold;
    }

    public Card[] getHand() {
        return hand;
    }

    public int getHandValue() {
        return handValue;
    }


    //basic functions
    public void gameSetAsGui(GameMainActivity activity) {

    }

    public void setAsGui(GameMainActivity activity) {

    }

    public void sendInfo(GameInfo info) {

    }

    public void start() {

    }

    public boolean requiresGui() {
        return false;
    }

    public boolean supportsGui() {
        return false;
    }
}
