package sg.edu.np.week_6_whackamole_3_0;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "WhackAMole.db";
    public static int DATABASE_VERSION = 1;
    public static String ACCOUNTS = "Accounts";
    public static String COLUMN_USERNAME = "Username";
    public static String COLUMN_PASSWORD = "Password";
    public static String COLUMN_LEVEL = "Level";
    public static String COLUMN_SCORE = "Score";
    /*
        The Database has the following properties:
        1. Database name is WhackAMole.db
        2. The Columns consist of
            a. Username
            b. Password
            c. Level
            d. Score
        3. Add user method for adding user into the Database.
        4. Find user method that finds the current position of the user and his corresponding
           data information - username, password, level highest score for each level
        5. Delete user method that deletes based on the username
        6. To replace the data in the database, we would make use of find user, delete user and add user

        The database shall look like the following:

        Username | Password | Level | Score
        --------------------------------------
        User A   | XXX      | 1     |    0
        User A   | XXX      | 2     |    0
        User A   | XXX      | 3     |    0
        User A   | XXX      | 4     |    0
        User A   | XXX      | 5     |    0
        User A   | XXX      | 6     |    0
        User A   | XXX      | 7     |    0
        User A   | XXX      | 8     |    0
        User A   | XXX      | 9     |    0
        User A   | XXX      | 10    |    0
        User B   | YYY      | 1     |    0
        User B   | YYY      | 2     |    0

     */

    private static final String FILENAME = "MyDBHandler.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context,DATABASE_NAME,factory, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_ACCOUNTS_TABLE="CREATE TABLE "+ACCOUNTS+"(" + COLUMN_USERNAME + " TEXT,"+ COLUMN_PASSWORD + " TEXT,"
        + COLUMN_LEVEL + " TEXT," + COLUMN_SCORE+ " TEXT"+")";
        db.execSQL(CREATE_ACCOUNTS_TABLE);
        Log.v(TAG, "DB Created: " + CREATE_ACCOUNTS_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+ACCOUNTS);
        onCreate(db);
    }

    public void addUser(String username, String password)
    {
        for(Integer i=0;i<10;i++){
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME,username);
            values.put(COLUMN_PASSWORD,password);
            values.put(COLUMN_LEVEL, i+1);
            values.put(COLUMN_SCORE,0);
            Log.v(TAG, FILENAME + ": Adding data for Database: " + values.toString());
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(ACCOUNTS,null,values);
            db.close();
        }


    }

    public UserData findUser(String username)
    {
        String query = "SELECT * FROM " + ACCOUNTS + " WHERE " +
                COLUMN_USERNAME + " = \"" + username + "\"";
        Log.v(TAG, FILENAME +": Find user form database: " + query);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        UserData queryData = new UserData();
        ArrayList<Integer> levelsList = new ArrayList<>();
        ArrayList<Integer> scoresList = new ArrayList<>();
        if(cursor.moveToFirst()){
            queryData.setMyUserName(cursor.getString(0));
            queryData.setMyPassword(cursor.getString(1));
            do{
                levelsList.add(cursor.getInt(2));
                scoresList.add(cursor.getInt(3));
            }while(cursor.moveToNext());
            queryData.setLevels(levelsList);
            queryData.setScores(scoresList);
            Log.v(TAG, FILENAME + ": QueryData: " + queryData.getLevels().toString() + queryData.getScores().toString());
            cursor.close();
        }
        else{
            queryData = null;
            Log.v(TAG, FILENAME+ ": No data found!");
        }
        return queryData;
    }
    public void UpdateScore(String username, int score, int level){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SCORE,score);
        String query = "UPDATE " + ACCOUNTS + " SET " + COLUMN_SCORE + " = "
                + score + " WHERE " + COLUMN_USERNAME + " = \"" + username
                + "\"" + " AND " + COLUMN_LEVEL + " = \"" + level + "\"";
        db.execSQL(query);
        db.close();

    }

    public boolean deleteAccount(String username) {
        boolean result = false;
        String query = "SELECT * FROM " + ACCOUNTS + " WHERE " + COLUMN_USERNAME +
                " = \"" + username + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        Log.v(TAG, FILENAME + ": Database delete user: " + query);
        UserData queryData = new UserData();
        if(cursor.moveToFirst()){
            queryData.setMyUserName(cursor.getString(0));
            db.delete(ACCOUNTS, COLUMN_USERNAME + " = ?",new String[]{username});
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}
