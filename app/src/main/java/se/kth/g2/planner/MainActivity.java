package se.kth.g2.planner;

/**
 * Created by Elias Sundberg on 2017-05-02.
 * Modified by Jens Egeland
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    public Typeface fontAwesome;
    EventHandler eventHandler;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO Build a new layout view for "boot screen"
        setContentView(R.layout.welcome_screen);
        init();
        try { TimeUnit.SECONDS.sleep(3); }
        catch (Exception e) { }

        if(!(sharedPref.getBoolean("setInterval", false) == false)){
            startActivity(new Intent(this, TodoListView.class));
        }
        else{
            defaultWork();
            startActivity(new Intent(this, TodoListView.class));
        }
        finish();
    }

    /** Any initializing is put here */
    protected void init() {
        eventHandler = EventHandler.getInstance();
        eventHandler.init(getApplicationContext());
        sharedPref = getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        this.fontAwesome = Typeface.createFromAsset(this.getAssets(), "fonts/fontawesome.ttf");
    }

    public void defaultWork(){
        editor.putInt("startHour", 8);
        editor.putInt("startMinute", 0);
        editor.putInt("endHour", 17);
        editor.putInt("endMinute", 0);
        editor.putInt("startLunchHour", 12);
        editor.putInt("startLunchMinute", 0);
        editor.putInt("lunchLength", 60);
        editor.putBoolean("setInterval", true);
        editor.commit();
    }
}
