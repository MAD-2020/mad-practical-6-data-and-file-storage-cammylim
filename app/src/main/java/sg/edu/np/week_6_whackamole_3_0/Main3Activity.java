package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Main3Activity extends AppCompatActivity {
    /* Hint:
        1. This displays the available levels from 1 to 10 to the user.
        2. The different levels makes use of the recyclerView and displays the highest score
           that corresponds to the different levels.
        3. Selection of the levels will load relevant Whack-A-Mole game.
        4. The levels are with the following difficulties.
            a. Level 1 will have a new mole at each 10000ms.
            b. Each level up will shorten the time to next mole by 100ms with level 10 as 1000 second per mole.
            c. For level 1 ~ 5, there is only 1 mole.
            d. For level 6 ~ 10, there are 2 moles.
            e. Each location of the mole is randomised.
        5. There is an option return to the login page.
     */
    UserData queryData;
    Button back;
    MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);
    private static final String FILENAME = "Main3Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        back = findViewById(R.id.backLoginButton);
        final String username = getIntent().getStringExtra("username");
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        CustomScoreAdaptor csAdaptor = new CustomScoreAdaptor(dbHandler.findUser(username));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        csAdaptor.setOnItemClickListener(new CustomScoreAdaptor.OnItemClickListener(){
            @Override
            public void ItemClick(int position) {
                Log.v(TAG, FILENAME + ": Show level for User: "+ username);
                Intent intent = new Intent(Main3Activity.this,Main4Activity.class);
                intent.putExtra("level",position);
                intent.putExtra("username",username);
                startActivity(intent);

            }
        });
        recyclerView.setAdapter(csAdaptor);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main3Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
