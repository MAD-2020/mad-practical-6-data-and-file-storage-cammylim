package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etPassword;
    Button loginButton;
    Button newButton;

    /*
        1. This is the main page for user to log in
        2. The user can enter - Username and Password
        3. The user login is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user does not exist. This loads the level selection page.
        4. There is an option to create a new user account. This loads the create user page.
     */
    private static final String FILENAME = "MainActivity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.loginButton);
        newButton = findViewById(R.id.newUserButton);
        etUsername = findViewById(R.id.createUsername);
        etPassword = findViewById(R.id.createPassword);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, FILENAME + ": Logging in with: " + etUsername.getText().toString() + ": " + etPassword.getText().toString());
                if(isValidUser(etUsername.getText().toString(),etPassword.getText().toString())){
                    Log.v(TAG, FILENAME + ": Valid User! Logging in");
                    Intent intent = new Intent(MainActivity.this,Main3Activity.class);
                    intent.putExtra("username",etUsername.getText().toString());
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, FILENAME + ": Invalid user!");
                }
            }
        });
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, FILENAME + ": Create new user!");
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
            }
        });
    }

    protected void onStop(){
        super.onStop();
        finish();
    }

    public boolean isValidUser(String userName, String password){
        UserData dbData = dbHandler.findUser(userName);
        if(dbData!=null && dbData.getMyPassword().equals(password)){
            Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + " " + password);
            return true;
        }
        else{
            return false;
        }
    }

}
