package edu.up.cs301.FCDGame;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.card.Card;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;

/**
 * The human controlled player player. This the class that will contain the human controlled
 * player.
 *
 * @author David Carbonari
 * @author Ryan Dehart
 * @author Gabe Hall
 * @version March 2016
 */
public class FCDHumanPlayer extends GameHumanPlayer implements View.OnClickListener, SeekBar.OnSeekBarChangeListener{

    private Activity myActivity;
    protected FCDState state;

    protected ImageView player1Card[] = new ImageView[5];
    protected boolean[] cardSelected = new boolean[5];
    protected ArrayList<Integer> discards = new ArrayList();

    protected ImageView player2card[] = new ImageView[5];

    protected Button callButton;
    protected Button foldButton;
    protected Button throwButton;
    protected Button raiseButton;
    protected SeekBar raiseSeekBar;

    protected TextView potValue;
    protected TextView player1Money;
    protected TextView player2Money;
    protected TextView raiseValue;

    private int lastBet;
    private int clickCount = 0;


    /**
     * constructor
     *
     * @param name
     */
    public FCDHumanPlayer(String name) {
        super(name);
    }

    @Override
    public View getTopView() {
        return null;
    }

    @Override
    public void receiveInfo(GameInfo info) {
        if(info instanceof FCDState) {
            this.state = (FCDState) info;
            potValue.setText("$" + state.getPot());
            player1Money.setText("$" + state.getLobby().get(0).getMoney());
            player2Money.setText("$" + state.getLobby().get(1).getMoney());
            if(state.getGameStage() == 1) {
                if (state.getLastBet() == -1) {
                    raiseSeekBar.setMax(state.getLobby().get(0).getMoney() - 10);
                    raiseValue.setText("" + (raiseSeekBar.getProgress() + 10));
                    lastBet = state.getLastBet();
                } else {
                    lastBet = state.getLastBet();
                    raiseSeekBar.setMax(state.getLobby().get(0).getMoney() - lastBet);
                    raiseValue.setText("" + (raiseSeekBar.getProgress() + lastBet));
                }
            }else{
                lastBet = state.getLastBet();
                raiseSeekBar.setMax(state.getLobby().get(0).getMoney() - lastBet);
                raiseValue.setText("" + (raiseSeekBar.getProgress() + lastBet));
            }

        }else{//do nothing
            return;
        }

    }


    public void setAsGui(GameMainActivity activity) {

        // remember the activity
        myActivity = activity;

        // Load the layout resource for the new configuration
        activity.setContentView(R.layout.five_card_draw);

        player1Card[0] = (ImageView) activity.findViewById(R.id.playerCard1);
        player1Card[0].setOnClickListener(this);
        player1Card[1] = (ImageView) activity.findViewById(R.id.playerCard2);
        player1Card[1].setOnClickListener(this);
        player1Card[2] = (ImageView) activity.findViewById(R.id.playerCard3);
        player1Card[2].setOnClickListener(this);
        player1Card[3] = (ImageView) activity.findViewById(R.id.playerCard4);
        player1Card[3].setOnClickListener(this);
        player1Card[4] = (ImageView) activity.findViewById(R.id.playerCard5);
        player1Card[4].setOnClickListener(this);

        player2card[0] = (ImageView) activity.findViewById(R.id.player2Card1);
        player2card[1] = (ImageView) activity.findViewById(R.id.player2Card2);
        player2card[2] = (ImageView) activity.findViewById(R.id.player2Card3);
        player2card[3] = (ImageView) activity.findViewById(R.id.player2Card4);
        player2card[4] = (ImageView) activity.findViewById(R.id.player2Card5);


        callButton = (Button) activity.findViewById(R.id.callButton);
        callButton.setOnClickListener(this);
        foldButton = (Button) activity.findViewById(R.id.foldButton);
        foldButton.setOnClickListener(this);
        throwButton = (Button) activity.findViewById(R.id.throwButton);
        throwButton.setOnClickListener(this);
        raiseButton = (Button) activity.findViewById(R.id.raiseButton);
        raiseButton.setOnClickListener(this);
        raiseSeekBar = (SeekBar) activity.findViewById(R.id.raiseSeekBar);
        raiseSeekBar.setOnSeekBarChangeListener(this);

        potValue = (TextView) activity.findViewById(R.id.potValue);
        player1Money = (TextView) activity.findViewById(R.id.player1Money);
        player2Money = (TextView) activity.findViewById(R.id.player2Money);
        raiseValue = (TextView) activity.findViewById(R.id.raiseValue);

        for(int i = 0; i < 5; i++){
            cardSelected[i] = false;
        }





        // if the state is not null, simulate having just received the state so that
        // any state-related processing is done
       // if (state != null) {
            receiveInfo(state);
       // }
    }

    public void onClick(View v) {


        //check if clicked on a card
        for(int i = 0; i < 5; i++){
            if (v == player1Card[i]){
                discards.add(i);
                if(cardSelected[i] == false) {
                    player1Card[i].setColorFilter(0xAAFF0000);
                    cardSelected[i] = true;
                }else{
                    player1Card[i].setColorFilter(0x00000000);
                    cardSelected[i] = false;
                }
                clickCount++;
            }
        }

        //call
        if (v == callButton){
            if(state.getLastBet() == -1){
                return;
            }else {
                state.playerCalls(state.getActivePlayer(), state.getLastBet());
                game.sendAction(new FCDCallAction(this));
            }
        }

        //fold
        if (v == foldButton){
            state.playerFolds(0);
            game.sendAction(new FCDFoldAction(this));
        }

        //throw
        if (v == throwButton){
            state.playerDiscards(state.getActivePlayer(), state.getPlayer1Hand(state.getActivePlayer()));
            int[] temp = new int[5];
            for(int i = 0; i < discards.size(); i++){
                temp[i] = discards.get(i);
            }
            game.sendAction(new FCDThrowAction(this, temp));
        }

        //raise
        if (v == raiseButton){
            state.playerRaises(state.getActivePlayer(), state.getLastBet(), Integer.parseInt(
                    ((String) (raiseValue.getText())).substring(1, raiseValue.length())));
            game.sendAction(new FCDRaiseAction(this, Integer.parseInt(
                    ((String)(raiseValue.getText())).substring(1, raiseValue.length()))));
        }
        return;
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //raise
        if (seekBar == raiseSeekBar){
            if(state.getGameStage() == 1) {
                if (state.getLastBet() == -1) {
                    raiseSeekBar.setMax(state.getLobby().get(0).getMoney() - 10);
                    raiseValue.setText("" +(raiseSeekBar.getProgress() + 10));
                    lastBet = state.getLastBet();
                } else {
                    lastBet = state.getLastBet();
                    raiseSeekBar.setMax(state.getLobby().get(0).getMoney() - lastBet);
                    raiseValue.setText("$" + (raiseSeekBar.getProgress() + lastBet));
                }
            }else{
                lastBet = state.getLastBet();
                raiseSeekBar.setMax(state.getLobby().get(0).getMoney() - lastBet);
                raiseValue.setText("$" + (raiseSeekBar.getProgress() + lastBet));
            }
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void setCardImage(int slot, Card card){
        player1Card[slot].setImageResource(card.getResIdx(card.getSuit(),card.getRank()));

    }
}