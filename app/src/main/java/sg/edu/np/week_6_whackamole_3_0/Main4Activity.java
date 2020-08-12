package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Main4Activity extends AppCompatActivity {
    private static final String FILENAME = "Main4Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    CountDownTimer readyTimer;
    CountDownTimer newMolePlaceTimer;
    int[]countdownList = {10,9,8,7,6,5,4,3,2,1};
    int level;
    TextView score;
    Button back;
    int scoreCount;
    boolean game = false;
    String username;
    ArrayList<Button> buttonList = new ArrayList<>();

    MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);

    private void readyTimer(){
        readyTimer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
                Toast.makeText(getApplicationContext(),"Get Ready In "+millisUntilFinished/1000+" seconds",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(),"GO!", Toast.LENGTH_SHORT).show();
                placeMoleTimer(level);
                game = true;
            }
        };
        readyTimer.start();
    }
    private void placeMoleTimer(int level){
        newMolePlaceTimer = new CountDownTimer(1000*countdownList[level],1000*countdownList[level]) {
            public void onTick(long millisUntilFinished) {
                Log.v(TAG,"New Mole Location!");
                setNewMole();
            }

            @Override
            public void onFinish() {
                newMolePlaceTimer.start();
            }
        };
        newMolePlaceTimer.start();
    }
    private static final int[] BUTTON_IDS = {
            R.id.button,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7,
            R.id.button8,
            R.id.button9
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        level = getIntent().getIntExtra("level",0);
        username = getIntent().getStringExtra("username");
        Log.v(TAG,"Level: " + level);
        /*Hint:
            This starts the countdown timers one at a time and prepares the user.
            This also prepares level difficulty.
            It also prepares the button listeners to each button.
            You may wish to use the for loop to populate all 9 buttons with listeners.
            It also prepares the back button and updates the user score to the database
            if the back button is selected.
         */
        for(final int id : BUTTON_IDS){
            final Button bQuery = findViewById(id);
            buttonList.add(bQuery);
        }
        score = findViewById(R.id.score);
        back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserScore();
                Intent intent = new Intent(Main4Activity.this,Main3Activity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onStart(){
        super.onStart();
        UpdateScore();
        readyTimer();
    }
    private boolean doCheck(Button checkButton)
    {
        return checkButton.getText() == "*";
    }

    public void setNewMole()
    {
        ArrayList<Button> molesList = new ArrayList<>();
        Random ran = new Random();
        int randomLocation = ran.nextInt(9);
        Button b = buttonList.get(randomLocation);
        molesList.add(b);
        if(level>5){
            int r2 = ran.nextInt(9);
            Button b2 = buttonList.get(r2);
            molesList.add(b2);
        }
        for(Button c : buttonList){
            if (molesList.contains(c)){
                c.setText("*");
                Log.v(TAG,"New mole location!");
            }
            else{
                c.setText("O");
            }
        }
    }
    public void OnClickButton(View v) {
        if(game){
            Button button = (Button) v;
            if (doCheck(button)) {
                scoreCount++;
                Log.v(TAG, "Hit, score added!");
                placeMoleTimer(level);
            }
            else if(scoreCount != 0){
                scoreCount--;
                Log.v(TAG, "Missed, score deducted!");
            }
            else{
                Log.v(TAG, "Missed, score deducted!");
            }
            UpdateScore();
        }
    }
    public void UpdateScore() {
        score.setText(String.valueOf(scoreCount));
    }


    private void updateUserScore()
    {
        if(scoreCount > dbHandler.findUser(username).getScores().get(level))
        {
            dbHandler.UpdateScore(username,scoreCount,level+1);
            newMolePlaceTimer.cancel();
            readyTimer.cancel();
            Log.v(TAG, FILENAME + ": Update User Score...");

        }


    }

}
