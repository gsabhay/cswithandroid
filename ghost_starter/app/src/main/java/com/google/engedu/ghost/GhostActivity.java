package com.google.engedu.ghost;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends ActionBarActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    InputStream stream;
    SimpleDictionary simple_dict;
    TextView ghostText,gameStatus;
    Button challenge,restart;
    private Random random = new Random();

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        char pressedKey = (char) event.getUnicodeChar();
        if(pressedKey >= 'A' && pressedKey <= 'Z' || pressedKey >= 'a' && pressedKey <= 'z' )
        {
            ghostText.append(String.valueOf(pressedKey).toLowerCase());
            if(simple_dict.isWord(ghostText.getText().toString()))
            {
                gameStatus.setText("Valid Word by the Player");
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        ghostText = (TextView)  findViewById(R.id.ghostText);
        gameStatus = (TextView)  findViewById(R.id.gameStatus);
        challenge = (Button)  findViewById(R.id.bt_challenge);
       // restart = (Button)  findViewById(R.id.bt_restart);

        try {
          stream =   getAssets().open("words.txt");
          simple_dict = new SimpleDictionary(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currWord = ghostText.getText().toString();
                if(currWord.length() >=  4 && simple_dict.isWord(currWord))
                    gameStatus.setText("User Won");
                else
                {
                    String nextWord =  simple_dict.getAnyWordStartingWith(currWord);
                    Log.d("Word : " , nextWord);
                    if(nextWord != null)
                    {
                        gameStatus.setText(" Computer Won. Word that can be formed is : " + nextWord);
                    }
                    else
                    {
                        gameStatus.setText(" User Won ");
                    }
                }


            }
        });
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
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

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        String currWord = ghostText.getText().toString();
        if(currWord.length() >=  4 && simple_dict.isWord(currWord))
            gameStatus.setText("Computer Won");
       String nextWord =  simple_dict.getAnyWordStartingWith(currWord);
        if(nextWord == null)
        {
           gameStatus.setText("You Can't bluff this Computer.  Computer Won.");
        }
        else
        {
            char c = nextWord.charAt(currWord.length());
            ghostText.append(String.valueOf(c));
        }

        // Do computer turn stuff then make it the user's turn again
        userTurn = true;
        label.setText(USER_TURN);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }
}
