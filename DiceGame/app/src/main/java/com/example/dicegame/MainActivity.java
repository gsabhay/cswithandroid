package com.example.dicegame;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends Activity implements Runnable {

    ImageView diceImage;
    Button roll,reset,hold;
    TextView scores;
    Thread thr;
    int userScore = 0,userTurnScore = 0,compScore = 0,compTurnScore = 0;
    int diceImages[] = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4 , R.drawable.dice5 , R.drawable.dice6};
    Random rand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rand = new Random();
        diceImage = (ImageView)findViewById(R.id.iv_dice);
        roll = (Button) findViewById(R.id.btRoll);
        reset = (Button) findViewById(R.id.btReset);
        hold = (Button) findViewById(R.id.btHold);
        scores = (TextView) findViewById(R.id.tvScores);

        diceImage.setImageResource(R.drawable.dice1);

        roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ind = rand.nextInt(6);
                diceImage.setImageResource(diceImages[ind]);
                int currVal = ind + 1;
                // Toast.makeText(MainActivity.this, " " + currVal, Toast.LENGTH_SHORT).show();
                if (currVal != 1) {
                    userTurnScore += currVal;
                    checkScores();
                    scores.setText(" Your score: " + userScore + " computer score: " + compScore + " your turn score:  " + userTurnScore);

                } else {

                    userTurnScore = 0;
                    scores.setText(" Your score: " + userScore + " computer score: " + compScore + " your turn score:  " + userTurnScore);
                    computerTurn();
                }
            }
        });

        hold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userScore += userTurnScore;
                checkScores();
                scores.setText(" Your score: " + userScore + " computer score: " + compScore + " your turn score:  " + userTurnScore);
                computerTurn();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userScore = 0;
                userTurnScore = 0;
                compScore = 0;
                compTurnScore = 0;
                scores.setText(" Your score: " + userScore + " computer score: " + compScore + " your turn score:  " + userTurnScore);
            }
        });

    }
    public  void computerTurn()
    {

        userTurnScore = 0;
       // hold.setEnabled(false);
       // roll.setEnabled(false);
        hold.setVisibility(View.INVISIBLE);
        roll.setVisibility(View.INVISIBLE);
        boolean flag = true;
        thr = new Thread(this);
        thr.start();
        while(flag)
        {
            checkScores();
            if(compTurnScore >= 20)
            {
                Toast.makeText(MainActivity.this, "Computer Holds", Toast.LENGTH_SHORT).show();
                compScore += compTurnScore;
                compTurnScore = 0;
                hold.setVisibility(View.VISIBLE);
                roll.setVisibility(View.VISIBLE);
                scores.setText( " Your score: " + userScore + " computer score: " + compScore + " your turn score:  " + userTurnScore );
                break;

            }
            int ind = rand.nextInt(6);
            int currVal = ind + 1;
            if(currVal != 1)
            compTurnScore += currVal;
            else
            {
                flag = false;
               // hold.setEnabled(true);
                //roll.setEnabled(true);
                hold.setVisibility(View.VISIBLE);
                 roll.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "Computer Rolled a One", Toast.LENGTH_SHORT).show();
                compTurnScore = 0;
            }
            Toast.makeText(MainActivity.this, "Computer Rolled Dice value :  " + currVal + " Turn Value : " + compTurnScore , Toast.LENGTH_SHORT).show();
         /*   try {
                thr.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
           } */
        }
    }

    void checkScores()
    {
        if(userTurnScore + userScore >= 100)
        {
            Toast.makeText(MainActivity.this, "User Wins", Toast.LENGTH_SHORT).show();
        }
        else if(compScore + compTurnScore >= 100)
        {
            Toast.makeText(MainActivity.this, "Computer Wins", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void run() {

    }
}
