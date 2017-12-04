package se.kth.g2.planner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;


/**
 * Created by Jens on 2017-05-08.
 * Created by Anna
 */

public class SpecifyIntervalView extends AppCompatActivity {
    private SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private Integer lunchLength;

    private Button OK;
    private Button CANCEL;
    private Button inputLunchLength;

    private TimePicker inputStart;
    private TimePicker inputEnd;
    private TimePicker inputLunchStart;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specify_interval);

        bindComponents();
        init();
        listener();
    }

    public void bindComponents(){
        sharedPref = getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        OK = (Button) findViewById(R.id.input_ok);
        CANCEL = (Button) findViewById(R.id.input_cancel);
        inputLunchLength = (Button) findViewById(R.id.add_lunch_time);

        inputStart = (TimePicker) findViewById(R.id.time_picker_start);
        inputEnd = (TimePicker) findViewById(R.id.time_picker_end);
        inputLunchStart = (TimePicker) findViewById(R.id.time_picker_lunch);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void init(){
        inputStart.setIs24HourView(true);
        inputEnd.setIs24HourView(true);
        inputLunchStart.setIs24HourView(true);

        inputStart.setHour(sharedPref.getInt("startHour", 0));
        inputStart.setMinute(sharedPref.getInt("startMinute", 0));
        inputEnd.setHour(sharedPref.getInt("endHour", 0));
        inputEnd.setMinute(sharedPref.getInt("endMinute", 0));
        inputLunchStart.setHour(sharedPref.getInt("startLunchHour", 0));
        inputLunchStart.setMinute(sharedPref.getInt("startLunchMinute", 0));
        inputLunchLength.setText(((Integer)(sharedPref.getInt("lunchLength", 0))).toString());
        lunchLength = sharedPref.getInt("lunchLength", 0);
    }

    public void listener(){
        inputLunchLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lunchLength += 15;
                if (lunchLength > 180) {
                    lunchLength = 0;
                }
                inputLunchLength.setText(lunchLength.toString());
            }
        });
        OK.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                editor.putInt("startHour", inputStart.getHour());
                editor.putInt("startMinute", inputStart.getMinute());

                editor.putInt("endHour", inputEnd.getHour());
                editor.putInt("endMinute", inputEnd.getMinute());

                editor.putInt("startLunchHour", inputLunchStart.getHour());
                editor.putInt("startLunchMinute", inputLunchStart.getMinute());

                editor.putInt("lunchLength", lunchLength);

                editor.putBoolean("setInterval", true);
                editor.commit();
                finish();
            }
        });
        CANCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}