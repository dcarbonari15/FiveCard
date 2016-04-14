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
    private int bet;
    private int money;
    private int handValue;
    private int subHandValue;
    private int raiseAmount;

    private boolean fold;

    private Card[] hand;


    public Player(){
        this.bet = 0;
        this.money = 500;
        this.fold = false;
        this.raiseAmount = 0;
        this.hand = new Card[]{new Card(Rank.FIVE, Suit.Diamond), new Card(Rank.ACE, Suit.Club), new Card(Rank.THREE, Suit.Diamond),
                new Card(Rank.FOUR, Suit.Spade), new Card(Rank.TWO, Suit.Diamond)};
    }

    public Player(Player player, FCDState state){
        this.bet = player.getBet();
        this.money = player.getMoney();
        this.hand = player.getHand();
        this.handValue = state.handValue(player.getHand());
        this.subHandValue = state.subHandValue(player.getHand());
        this.fold = player.isFold();
        this.raiseAmount = player.getRaiseAmount();
    }

    public void fold(){
        this.fold = true;
    }

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
        return hand[index];
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
