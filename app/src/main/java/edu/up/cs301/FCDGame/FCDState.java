package edu.up.cs301.FCDGame;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.Rank;
import edu.up.cs301.card.Suit;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.infoMsg.GameState;

/**
 * This is the class that will contain the gameState. It will hold all the methods to edit the
 * gameState and move the game forward.
 *
 * @author David Carbonari
 * @author Ryan Dehart
 * @author Gabe Hall
 * @version March 2016
 */
public class FCDState extends GameState {
    //instance Variables

    private ArrayList<Card> deck = new ArrayList<Card>();
    private ArrayList<Card> dealtCards = new ArrayList<Card>();
    private Card cardBack;
    private int lastBet = -1;
    private int pot;
    private int activePlayer;
    private int gameStage;
    private int moveCount = 1;
    private ArrayList<Player> lobby = new ArrayList<Player>();

    private Rank[] cardVals= {Rank.ACE, Rank.TWO, Rank.THREE, Rank.FOUR, Rank.FIVE, Rank.SIX, Rank.SEVEN,
            Rank.EIGHT, Rank.NINE, Rank.TEN, Rank.JACK, Rank.QUEEN, Rank.KING};
    private Suit[] cardSuits = {Suit.Club, Suit.Diamond, Suit.Heart, Suit.Spade};


    /*
     *Creates the default GameState with a lobby of players and a 52 card deck
     * and other essential variables
     */

    public FCDState(){
        for(int i = 0; i < 2; i++){
            lobby.add(i, new Player());
            lobby.get(i).setMoney(500);
        }
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 13; j++){
                Card card = new Card(cardVals[j], cardSuits[i]);
                deck.add(card);
            }
        }
        gameStage = 1;
        pot = 0;
        activePlayer = 0;
        lastBet = -1;
    }

    /**
     * Creates a gamestate with the given inputs
     *
     * @param gameState <-- The new gameState that will be copied to the current gameState
     */

    public FCDState(FCDState gameState){
        int index = 0;
        for(int i = 0; i < 2; i++){
            lobby.add(i, new Player());
        }
        for(Player p:gameState.getLobby()){
            this.lobby.get(index).setMoney(p.getMoney());
            this.lobby.get(index).setBet(p.getBet());
            this.lobby.get(index).setHand(p.getHand());
            if(p.isFold()){
                this.lobby.get(index).fold();
            }
            index++;
        }
        this.pot = gameState.getPot();
        this.activePlayer = gameState.getActivePlayer();
        this.cardBack = gameState.getCardBack();
        this.gameStage = gameState.getGameStage();
        this.lastBet = gameState.getLastBet();
        this.deck = gameState.getDeck();
    }

    /**
     * remakes the deck with 1 of each of the 52 cards
     * (used after each round is played)
     */

    public void remakeDeck(){
        for(int i = 0; i < deck.size(); i++){
            deck.remove(i);
        }
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 13; j++){
                Card card = new Card(cardVals[j], cardSuits[i]);
                deck.add(card);
            }
        }
    }

    /*
     *This method is to be called at the end of each gamestage. After someone calls, the last
     *player checks, or the players finish throwing this will advance the game to the next stage.
     *If the game is over and the gamestage is beyond 3 it will reset to the start of a new game.
     */
    public void advanceGameStage(){
        this.gameStage++;
        if(this.gameStage > 3){
            this.gameStage = 1;
        }
        Log.i("AdvancedStage:",Integer.toString(gameStage));
    }


    /**
     * This method takes in the input of the players hand and determines the numerical value of the
     * hand.
     *
     * @param playersHand --> an array for cards that are currently in the player's hand
     *
     * @return --> returns the 0-9 integer value of the players hand based on the following hierarchy
     * 0 --> High Card
     * 1 --> Single Pair
     * 2 --> Two Pair
     * 3 --> Three of a kind
     * 4 --> Straight
     * 5 --> Flush
     * 6 --> Full house
     * 7 --> 4 of a kind
     * 8 --> Straight Flush
     * 9 --> Royal Flush
     *
     */
    public int handValue(Card[] playersHand){
        Rank[] cardVals = new Rank[5];
        Suit[] cardSuit = new Suit[5];
        int handVal = 0;
        //separates the ranks and suits of the cards into separate arrays
        for(int i = 0; i < 5; i++){
            cardVals[i] = playersHand[i].getRank();
            cardSuit[i] = playersHand[i].getSuit();
        }
        int matchCount = 0;
        //counts the amount of pairs there are in the hand
        for(int i = 1; i < 5; i++){
            //the cards that match with the first card in the hand
            if (cardVals[0].equals(cardVals[i])) {
                matchCount++;
            }
            //the cards that match with the second card in the hand
            if(i > 1){
                if(cardVals[1].equals(cardVals[i])){
                    matchCount++;
                }
            }
            //the cards that match with the third card in the hand
            if(i > 2){
                if(cardVals[2].equals(cardVals[i])){
                    matchCount++;
                }
            }
            //the cards that match with the fourth card in the hand
            if(i > 3){
                if(cardVals[3].equals(cardVals[i])){
                    matchCount++;
                }
            }
        }
        //determines which hand the player has and sets the value based on
        //if they have a single pair
        if (matchCount == 1){
            handVal = 1;
            //if they have two pair
        }else if (matchCount == 2){
            handVal = 2;
            //if they have three of a kind
        }else if (matchCount == 3){
            handVal = 3;
            //if they have a full house
        }else if (matchCount == 4){
            handVal = 6;
            //if they have 4 of a kind
        }else if (matchCount == 6){
            handVal = 7;
        }
        //if they have a flush
        if(allSameSuit(cardSuit)){
            //if their flush is a royal flush
            if(Royalty(cardVals)){
                handVal = 9;
                //if their hand is a straight flush
            }else if(isStraight(cardVals)){
                handVal = 8;
            }
            handVal = 5;
        }
        //if their hand is a straight
        if(isStraight(cardVals)){
            handVal = 4;
        }
        return handVal;
    }

    /**
     * This method is a helper method to the handValue method that return whether the hand is a
     * royal flush or not
     *
     * @param ranks --> the Rank values of the cards in the players hand
     *
     * @return
     * returns true when the player has the highest value straight
     */
    private boolean Royalty(Rank[] ranks){
        int[] sorted = orderHand(ranks);
        return isStraight(ranks) && sorted[0] == 10;
    }

    /**
     * This method is a helper method to the handValue method that returns wherther the hand is a
     * straight or not
     *
     * @param ranks --> the Rank values of the cards in the players hand
     *
     * @return
     * returns true when the player has a straight
     */

    private boolean isStraight(Rank[] ranks){
        int[] sorted = orderHand(ranks);
        int count = 0;
        for(int i = 1; i < 5; i++){
            if(sorted[0] == sorted[i] - i){
                count++;
            }
            if((sorted[0] == 10) && (i == 4) ){
                if(sorted[i] == 1){
                    count++;
                }
            }
        }
        return count == 4;
    }

    /**
     * This method is a helper method to the isStraight method. It takes an array of ranks and then
     * converts them to numerical values and return them in ascending order.
     *
     * @param ranks --> the Rank values of the cards in the players hand
     *
     * @return
     * returns an array of integers in ascending order that represent the ranks in the player's hand
     */

    private int[] orderHand(Rank[] ranks){
        int[] handVals = new int[5];
        for(int i = 0; i < 5; i++){
            handVals[i] = ranksToInts(ranks[i]);
        }
        Arrays.sort(handVals);
        return handVals;
    }

    public int cardToInt(Card card){
        if(card.getSuit() == Suit.Club){
            return ranksToInts(card.getRank());
        }else if(card.getSuit() == Suit.Diamond){
            return 13 + ranksToInts(card.getRank());
        }else if(card.getSuit() == Suit.Heart){
            return 26 + ranksToInts(card.getRank());
        }else{
            return 39 + ranksToInts(card.getRank());
        }
    }

    /**
     * This method is a helper method that changed the ranks into numerical values
     *
     * @param rank --> the Rank values of the cards in the players hand
     *
     * @return
     * returns the integer value for the corresponding rank
     */

    private int ranksToInts(Rank rank){
        if(rank.equals(Rank.ACE)){
            return 0;
        }else if(rank.equals(Rank.TWO)){
            return 1;
        }else if(rank.equals(Rank.THREE)){
            return 2;
        }else if(rank.equals(Rank.FOUR)){
            return 3;
        }else if(rank.equals(Rank.FIVE)){
            return 4;
        }else if(rank.equals(Rank.SIX)){
            return 5;
        }else if(rank.equals(Rank.SEVEN)){
            return 6;
        }else if(rank.equals(Rank.EIGHT)){
            return 7;
        }else if(rank.equals(Rank.NINE)){
            return 8;
        }else if(rank.equals(Rank.TEN)){
            return 9;
        }else if(rank.equals(Rank.JACK)){
            return 10;
        }else if(rank.equals(Rank.QUEEN)){
            return 11;
        }else{
            return 12;
        }
    }

    /**
     * This method is a helper method that checks if all the suits are the same
     *
     * @param suits --> the Suit values of the cards in the players hand
     *
     * @return
     * returns true if the players hand consists of all the same suit
     */
    private boolean allSameSuit(Suit[] suits){
        int sameSuitCount = 0;
        for(int i = 1; i < 5; i++){
            if(suits[0].equals(suits[i])){
                sameSuitCount++;
            }
        }
        if(sameSuitCount == 4){
            return true;
        }else{
            return false;
        }
    }

    /**
     * method that determines the value of each hand based on the other factors such as the strength
     * of the pair, the highest card, the higher straight ect...
     *
     * @param cards --> The array of cards that make up the player's hand
     *
     * @return
     * returns the integer value of the hand based on the value of the important cards
     */
    public int subHandValue(Card[] cards){
        //figures out what type of hand is being dealt with
        int handVal = handValue(cards);
        //if its a high card
        if(handVal == 0){
            int highestCard = 0;
            for(int i = 0; i < 5; i++){
                if(highestCard < ranksToInts(cards[i].getRank())){
                    if(highestCard != 1) {
                        highestCard = ranksToInts(cards[i].getRank());
                    }
                }
            }
            return highestCard;
            //if its a pair
        }else if(handVal == 1){
            Rank[] ranks = new Rank[5];
            for(int i = 0; i < 5; i++){
                ranks[i] = cards[i].getRank();
            }
            return pairVal(ranks);
            //if its a two pair
        }else if(handVal == 2){
            Rank[] ranks = new Rank[5];
            for(int i = 0; i < 5; i++){
                ranks[i] = cards[i].getRank();
            }
            return twoPairVal(ranks);
            //if its a trip
        }else if(handVal == 3){
            Rank[] ranks = new Rank[5];
            for(int i = 0; i < 5; i++){
                ranks[i] = cards[i].getRank();
            }
            return pairVal(ranks);
            //if its a straight
        }else if(handVal == 4){
            Rank[] ranks = new Rank[5];
            for(int i = 0; i < 5; i++){
                ranks[i] = cards[i].getRank();
            }
            int[] sorted = orderHand(ranks);
            return sorted[0];
            //if its a flush
        }else if(handVal == 5){
            int highestCard = 0;
            for(int i = 0; i < 5; i++){
                if(highestCard < ranksToInts(cards[i].getRank())){
                    if(highestCard != 1) {
                        highestCard = ranksToInts(cards[i].getRank());
                    }
                }
            }
            return highestCard;
            //if its a full house
        }else if(handVal == 6){
            Rank[] ranks = new Rank[5];
            for(int i = 0; i < 5; i++){
                ranks[i] = cards[i].getRank();
            }
            return fullHouseSubVal(ranks);
            //if its a quad
        }else if(handVal == 7){
            Rank[] ranks = new Rank[5];
            for(int i = 0; i < 5; i++){
                ranks[i] = cards[i].getRank();
            }
            return pairVal(ranks);
            //if its a straight flush
        }else if(handVal == 8){
            Rank[] ranks = new Rank[5];
            for(int i = 0; i < 5; i++){
                ranks[i] = cards[i].getRank();
            }
            int[] sorted = orderHand(ranks);
            return sorted[0];
        }
        return 0;
    }

    /**
     * This method is a helper method that returns the numerical value of the pair in the hand
     *
     * @param ranks --> the Rank values of the cards in the players hand
     *
     * @return
     * returns the integer value of the card that is a part of the pair in the hand
     */

    private int pairVal(Rank[] ranks){
        for(int i = 1; i < 5; i++){
            //the cards that match with the first card in the hand
            if (ranks[0].equals(ranks[i])) {
                return ranksToInts(ranks[0]);
            }
            //the cards that match with the second card in the hand
            if(i > 1){
                if(ranks[1].equals(ranks[i])){
                    return ranksToInts(ranks[1]);
                }
            }
            //the cards that match with the third card in the hand
            if(i > 2){
                if(ranks[2].equals(ranks[i])){
                    return ranksToInts(ranks[2]);
                }
            }
            //the cards that match with the fourth card in the hand
            if(i > 3){
                if(ranks[3].equals(ranks[i])){
                    return ranksToInts(ranks[3]);
                }
            }
        }
        return -1;
    }

    /**
     * this method is a helper method that returns the numerical value of the high pair in a two
     * pair hand
     *
     * @param ranks --> the Rank values of the cards in the players hand
     *
     * @return
     * returns the integer value of the high pair in the hand
     */
    private int twoPairVal(Rank[] ranks){
        int pair1val = -1;
        int pair2val = -1;
        for(int i = 1; i < 5; i++){
            //the cards that match with the first card in the hand
            if (ranks[0].equals(ranks[i])) {
                if(pair1val == -1){
                    pair1val = ranksToInts(ranks[0]);
                }else{
                    pair2val = ranksToInts(ranks[0]);
                }
            }
            //the cards that match with the second card in the hand
            if(i > 1){
                if(ranks[1].equals(ranks[i])){
                    if(pair1val == -1){
                        pair1val = ranksToInts(ranks[1]);
                    }else{
                        pair2val = ranksToInts(ranks[1]);
                    }
                }
            }
            //the cards that match with the third card in the hand
            if(i > 2){
                if(ranks[2].equals(ranks[i])){
                    if(pair1val == -1){
                        pair1val = ranksToInts(ranks[2]);
                    }else{
                        pair2val = ranksToInts(ranks[2]);
                    }
                }
            }
            //the cards that match with the fourth card in the hand
            if(i > 3){
                if(ranks[3].equals(ranks[i])){
                    if(pair1val == -1){
                        pair1val =ranksToInts(ranks[3]);
                    }else{
                        pair2val =ranksToInts(ranks[3]);
                    }
                }
            }
        }
        if(pair1val == 1){
            return pair1val;
        }else if(pair2val == 1){
            return pair2val;
        }else if(pair1val > pair2val){
            return pair1val;
        }
        return pair2val;
    }

    /**
     * method is a helper method that determines the numerical value of the three of a kind in a
     * full house
     *
     * @param ranks --> the Rank values of the cards in the players hand
     *
     * @return
     * returns the integer value of the card that is a part of the triple in the full house
     */

    private int fullHouseSubVal(Rank[] ranks){
        int pair1val = -1;
        int pair2val = -1;
        for(int i = 1; i < 5; i++){
            //the cards that match with the first card in the hand
            if (ranks[0].equals(ranks[i])) {
                if(ranks[0].equals(ranks[i])){
                    if(pair1val == -1){
                        pair1val = ranksToInts(ranks[0]);
                    }else if(!(pair1val == ranksToInts(ranks[0]))){
                        if(!(pair2val == ranksToInts(ranks[0]))) {
                            pair2val = ranksToInts(ranks[0]);
                        }
                        return ranksToInts(ranks[0]);
                    }
                    return ranksToInts(ranks[0]);
                }
            }
            //the cards that match with the second card in the hand
            if(i > 1){
                if(ranks[1].equals(ranks[i])){
                    if(pair1val == -1){
                        pair1val = ranksToInts(ranks[1]);
                    }else if(!(pair1val == ranksToInts(ranks[1]))){
                        if(!(pair2val == ranksToInts(ranks[1]))) {
                            pair2val = ranksToInts(ranks[1]);
                        }
                        return ranksToInts(ranks[1]);
                    }
                    return ranksToInts(ranks[1]);
                }
            }
            //the cards that match with the third card in the hand
            if(i > 2){
                if(ranks[2].equals(ranks[i])){
                    if(pair1val == -1){
                        pair1val = ranksToInts(ranks[2]);
                    }else if(!(pair1val == ranksToInts(ranks[2]))){
                        if(!(pair2val == ranksToInts(ranks[2]))) {
                            pair2val = ranksToInts(ranks[2]);
                        }
                        return ranksToInts(ranks[2]);
                    }
                    return ranksToInts(ranks[2]);
                }
            }
            //the cards that match with the fourth card in the hand
            if(i > 3){
                if(ranks[3].equals(ranks[i])){
                    if(pair1val == -1){
                        pair1val = ranksToInts(ranks[3]);
                    }else if(!(pair1val == ranksToInts(ranks[3]))){
                        if(!(pair2val == ranksToInts(ranks[3]))) {
                            pair2val = ranksToInts(ranks[3]);
                        }
                        return ranksToInts(ranks[3]);
                    }
                    return ranksToInts(ranks[3]);
                }
            }
        }
        return -1;
    }

    /**
     * modifies the pot value via sum
     *
     * @param mod
     *      the amount to add (if +) or subtract (if -)
     */
    public void modifyPot(int mod){
        this.pot += mod;
    }

    /**
     * modifies the players money
     *
     * @param targetPlayer
     *      the player to be modified
     * @param mod
     *      the amount to modify it by
     */
    public void modifyPlayerMoney(int targetPlayer, int mod){
        if (getPlayerMoney(targetPlayer) + mod >= 0) {
            lobby.get(targetPlayer).setMoney(getPlayerMoney(targetPlayer) + mod);
        }
    }

    /**
     * modify the given player's bet
     *
     * @param targetPlayer
     *      player's bet to be modified
     * @param mod
     *      amount to modify the bet
     */
    public void modifyPlayerBet(int targetPlayer, int mod){
        lobby.get(targetPlayer).setBet(getPlayerBet(targetPlayer) + mod);
        lastBet = mod;
    }

    /**
     * handles a player's victory
     *
     * @param sourcePlayer
     *      the player that won
     */
    public void playerWins(int sourcePlayer){
        lobby.get(sourcePlayer).setMoney(getPlayerMoney(sourcePlayer) + pot);
    }

    public void setCount(){
        moveCount++;
        if(moveCount > 3 * lobby.size()){
            moveCount = 1;
        }
    }

    /**
     * handles a player folding
     *
     * @param sourcePlayer
     *      the player that is folding
     */
    public void playerFolds(int sourcePlayer){
        lobby.get(sourcePlayer).fold();
    }

    /**
     * handles a player calling
     *
     * @param sourcePlayer
     *      the player that is calling
     * @param base
     *      the call amount
     */
    public void playerCalls(int sourcePlayer, int base){
        lobby.get(sourcePlayer).setBet(getPlayerBet(sourcePlayer) + base);
        lobby.get(sourcePlayer).setMoney(getPlayerMoney(sourcePlayer) - getPlayerBet(sourcePlayer));
    }

    /**
     * handles a player raising
     *
     * @param sourcePlayer
     *      the player doing the raising
     * @param base
     *      the call amount
     * @param amount
     *      the amount the player is raising the call by
     */
    public void playerRaises(int sourcePlayer, int base, int amount){
        lobby.get(sourcePlayer).setBet(getPlayerBet(sourcePlayer) + base + amount);
        lobby.get(sourcePlayer).setMoney(getPlayerMoney(sourcePlayer) - getPlayerBet(sourcePlayer));
        pot += getPlayerBet(sourcePlayer);
        lastBet = base + amount;
    }

    /**
     * shuffles the deck
     */
    public void shuffle(){
        ArrayList<Card> newDeck = new ArrayList<Card>(); //the new deck after shuffling
        boolean[] cardUsed = new boolean[52]; //holds whether that card has been added
        //to the new deck

        //for each card
        for(int i = 0; i < 52; i++){
            //set each cardused to false
            cardUsed[i] = false;
        }

        //for each card (in random order)
        for(int i = 0; i < 52; i++){
            Random random = new Random();//random
            int cardPlace = 0;//the place of the card in the current deck that will be
            //added to the new deck
            do {
                cardPlace = random.nextInt(52); //try a random card number
            } while(cardUsed[cardPlace] == true); //while card has not already been placed
            //in new deck

            //set the card used to true
            cardUsed[cardPlace] = true;
            //add the card to the deck (in order)
            newDeck.add(deck.get(cardPlace));
        }
        //set the deck to the new shuffled deck
        deck = newDeck;
    }

    /**
     * converts a Card array to a Card ArrayList
     *
     * @param card
     *      the array of Cards to be converted
     * @return
     *      the converted ArrayList
     */
    private ArrayList<Card> cardArrayToArrayList(Card[] card){
        int i = 0;//iterator
        ArrayList<Card> cardList = new ArrayList<Card>();//list to be returned

        //for each card
        for (Card c: card){
            //add the next card to the list
            cardList.add(card[i]);
            //iterator
            i++;
        }

        //return
        return cardList;
    }

    public void removePlayer(int index){
        if(index > lobby.size()){
            return;
        }else{
            lobby.remove(index);
        }
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public boolean isPlayerFold(int index){
        return lobby.get(index).isFold();
    }

    public int getPlayerMoney(int index) {
        return lobby.get(index).getMoney();
    }

    public Card getCardBack() {
        return cardBack;
    }

    public Card[] getPlayer1Hand(int index) {
        return lobby.get(index).getHand();
    }

    public int getActivePlayer() {
        return activePlayer;
    }

    public int getGameStage() {
        return gameStage;
    }

    public int getHandValue(int index) {
        return handValue(lobby.get(index).getHand());
    }

    public int getPlayerBet(int index) {
        return lobby.get(index).getBet();
    }

    public int getPot() {
        return pot;
    }

    public int getLastBet() {
        return lastBet;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public int getSubHandValue(int index) {
        return subHandValue(lobby.get(index).getHand());
    }

    public Rank[] getCardVals() {
        return cardVals;
    }

    public Suit[] getCardSuits() {
        return cardSuits;
    }

    public ArrayList<Player> getLobby() {
        return lobby;
    }

    public ArrayList<Card> getDealtCards() {
        return dealtCards;
    }

    public void setActivePlayer(int activePlayer) {
        this.activePlayer = activePlayer;
    }

    public void setCardBack(Card cardBack) {
        this.cardBack = cardBack;
    }

    public void setCardSuits(Suit[] cardSuits) {
        this.cardSuits = cardSuits;
    }

    public void setCardVals(Rank[] cardVals) {
        this.cardVals = cardVals;
    }

    public void setGameStage(int gameStage) {
        this.gameStage = gameStage;
    }

    public void setPlayerBet(int playerBet, int index) {
        lobby.get(index).setBet(playerBet);
    }

    public void setPlayerHand(Card[] playerHand, int index) {
        lobby.get(index).setHand(playerHand);
    }

    public void setPlayerMoney(int playerMoney, int index) {
        lobby.get(index).setMoney(playerMoney);
    }

    public void setPot(int pot) {
        this.pot = pot;
    }


}
