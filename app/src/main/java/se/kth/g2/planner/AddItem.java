package se.kth.g2.planner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

/*
* Created by Jens Egeland
* Created by Rasmus Olsson
*/

public class AddItem extends AppCompatActivity {
    private static final String TAG = "debugTAG";

    private Typeface fontAwesome;
    private Button addOk;
    private Button addCancel;
    private Button addEstimate;
    private TextView addPlanned;
    private TextView addDeadline;
    private TextView addDelete;
    private TextView addImportance;
    private TextView displayPlanned;
    private TextView displayDeadline;
    private EditText addTitle;
    private Integer estimateValue;
    private Integer importanceInt;
    SharedPreferences sharedPref;


    //Button for PLANNED Popup Window
    private PopupWindow popupWindowPlanned;
    private LayoutInflater layoutInflaterPlanned;
    private DatePicker datePicker_planned;
    private Button setBtn_planned;
    private Button cancelBtn_planned;
    private TimePicker timePicker_planned1;
    private TimePicker timePicker_planned2;
    private Integer dayPlanned;
    private Integer monthPlanned;
    private Integer yearPlanned;
    private Integer hourPlanned1;
    private Integer hourPlanned2;
    private Integer minPlanned1;
    private Integer minPlanned2;
    private boolean plannedPage;

    //Button for DEADLINE Popup Window
    private PopupWindow popupWindowDeadline;
    private LayoutInflater layoutInflaterDeadline;
    private DatePicker datePicker_deadline;
    private Button cancelBtn_deadline;
    private Button setBtn_deadline;
    private TimePicker timePicker_deadline;
    private Integer dayDeadline;
    private Integer monthDeadline;
    private Integer yearDeadline;
    private Integer hourDeadline;
    private Integer minDeadline;
    private boolean deadlinePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        this.fontAwesome = Typeface.createFromAsset(this.getAssets(), "fonts/fontawesome.ttf");
        ListObject object = (ListObject) getIntent().getSerializableExtra("addObject");
        bindComponents(object);
        listener(object);
    }

    private void bindComponents(ListObject object) {
        addOk = (Button) findViewById(R.id.add_btn_ok_object);
        addCancel = (Button) findViewById(R.id.add_btn_cancel_object);
        addEstimate = (Button) findViewById(R.id.add_estimate_btn);
        addImportance = (TextView) findViewById(R.id.add_importance_textview);
        importanceInt = 0;

        addPlanned = (TextView) findViewById(R.id.add_planned_textview);
        addPlanned.setTypeface(fontAwesome);
        addPlanned.setTextColor(Color.parseColor(getString(R.color.colorPrimaryDark)));

        addDeadline = (TextView) findViewById(R.id.add_deadline_textview);
        addDeadline.setTypeface(fontAwesome);
        addDeadline.setTextColor(Color.parseColor(getString(R.color.colorPrimaryDark)));

        displayPlanned = (TextView) findViewById(R.id.display_planned);
        displayDeadline = (TextView) findViewById(R.id.display_deadline);

        addDelete = (TextView) findViewById(R.id.add_delete_textview);
        addDelete.setTypeface(fontAwesome);

        addTitle = (EditText) findViewById(R.id.add_title_edittext);

        sharedPref = getSharedPreferences("setting", Context.MODE_PRIVATE);

        estimateValue = 0;
        addEstimate.setText(estimateValue.toString());
        displayPlanned.setText("");
        displayDeadline.setText("");
        addTitle.setText("");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void initiatePopUpWindowPlanned(View v, final ListObject object) {
        layoutInflaterPlanned = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup container = (ViewGroup) layoutInflaterPlanned.inflate(R.layout.popup_menu_planned, null);
        popupWindowPlanned = new PopupWindow(container, 1150, 2130);
        popupWindowPlanned.showAtLocation(v, Gravity.CENTER, 0, -350);
        cancelBtn_planned = (Button) container.findViewById(R.id.cancel_button_planned);
        setBtn_planned = (Button) container.findViewById(R.id.set_button_planned);
        timePicker_planned1 = (TimePicker) container.findViewById(R.id.timePicker_planned_start);
        timePicker_planned2 = (TimePicker) container.findViewById(R.id.timePicker_planned_end);
        datePicker_planned = (DatePicker) container.findViewById(R.id.datePicker_planned);
        timePicker_planned1.setIs24HourView(true);
        timePicker_planned2.setIs24HourView(true);
        timePicker_planned1.setHour(12);
        timePicker_planned1.setMinute(0);
        timePicker_planned2.setHour(13);
        timePicker_planned2.setMinute(0);
        plannedPage = true;

        cancelBtn_planned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlanned.setTextColor(Color.parseColor(getString(R.color.colorPrimaryDark)));
                plannedPage = false;
                popupWindowPlanned.dismiss();
            }
        });
        setBtn_planned.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                object.initPlannedCal();
                object.setPlanned(true);
                object.setDeadline(false);
                plannedPage = false;

                dayPlanned = datePicker_planned.getDayOfMonth();
                monthPlanned = datePicker_planned.getMonth();
                yearPlanned = datePicker_planned.getYear();
                hourPlanned1 = timePicker_planned1.getHour();
                hourPlanned2 = timePicker_planned2.getHour();
                minPlanned1 = timePicker_planned1.getMinute();
                minPlanned2 = timePicker_planned2.getMinute();

                String curTime1 = String.format("%02d:%02d", hourPlanned1, minPlanned1);
                String curTime2 = String.format("%02d:%02d", hourPlanned2, minPlanned2);

                object.setStartDate(yearPlanned, monthPlanned, dayPlanned, hourPlanned1, minPlanned1);
                object.setEndDate(yearPlanned, monthPlanned, dayPlanned, hourPlanned2, minPlanned2);

                estimateValue = (hourPlanned2 * 60 + minPlanned2) - (hourPlanned1 * 60 + minPlanned1);
                object.setEstimate(estimateValue);
                addEstimate.setText(estimateValue.toString());
                Integer displayPlannedMonth = monthPlanned + 1;

                displayPlanned.setText(dayPlanned.toString() + "/" + displayPlannedMonth.toString() + "/" + yearPlanned.toString() +
                "  " + curTime1 + " - " + curTime2);

                popupWindowPlanned.dismiss();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void initiatePopUpWindowDeadline(View v, final ListObject object) {
        layoutInflaterDeadline = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup container1 = (ViewGroup) layoutInflaterDeadline.inflate(R.layout.popup_menu_deadline, null);
        popupWindowDeadline = new PopupWindow(container1, 1150, 2130);
        popupWindowDeadline.showAtLocation(v, Gravity.CENTER, 0, 350);
        cancelBtn_deadline = (Button) container1.findViewById(R.id.cancel_button_deadline);
        setBtn_deadline = (Button) container1.findViewById(R.id.set_button_deadline);
        timePicker_deadline = (TimePicker) container1.findViewById(R.id.time_picker_deadline);
        datePicker_deadline = (DatePicker) container1.findViewById(R.id.date_picker_deadline);
        timePicker_deadline.setIs24HourView(true);
        timePicker_deadline.setHour(sharedPref.getInt("endHour", 0));
        timePicker_deadline.setMinute(sharedPref.getInt("endMinute", 0));

        deadlinePage = true;

        cancelBtn_deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDeadline.setTextColor(Color.parseColor(getString(R.color.colorPrimaryDark)));
                deadlinePage = false;
                popupWindowDeadline.dismiss();
            }
        });
        setBtn_deadline.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                object.initDeadlineCal();
                object.setDeadline(true);
                object.setPlanned(false);
                deadlinePage = false;

                dayDeadline = datePicker_deadline.getDayOfMonth();
                monthDeadline = (datePicker_deadline.getMonth());
                yearDeadline = datePicker_deadline.getYear();
                hourDeadline = timePicker_deadline.getHour();
                minDeadline = timePicker_deadline.getMinute();

                Integer displayDeadlineMonth = monthDeadline + 1;
                String curTime = String.format("%02d:%02d", hourDeadline, minDeadline);
                object.setDeadlineDate(yearDeadline, monthDeadline, dayDeadline, hourDeadline, minDeadline);
                displayDeadline.setText(dayDeadline.toString() + "/" + displayDeadlineMonth.toString() + "/" + yearDeadline.toString() + "  " + curTime);

                popupWindowDeadline.dismiss();
            }
        });
    }

    /*
     * Starts 4 onClickListeners.
     * addEstimate - For adding 15 min/click to the estimateValue.
     * addPlanned/addDeadline - True/False values, also starts the datepicker.
     * addOk - Sends back the updated object to the calling method, with control codes.
     * addCancel - Sends back a string "null", and control codes.
     */
    private void listener(final ListObject object) {
        addEstimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estimateValue += 15;
                if (estimateValue > 180) {
                    estimateValue = 0;
                }
                addEstimate.setText(estimateValue.toString());
            }
        });
        addPlanned.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (!deadlinePage && (object.getHasDeadline() == false)) {
                    if (object.getIsPlanned() == false) {
                        addPlanned.setTextColor(Color.parseColor(getString(R.color.colorAccent)));
                        if (!plannedPage) {
                            initiatePopUpWindowPlanned(v, object);
                        } else {
                            popupWindowPlanned.dismiss();
                            plannedPage = false;
                            addPlanned.setTextColor(Color.parseColor(getString(R.color.colorPrimaryDark)));
                        }
                    } else {
                        object.setPlanned(false);
                        addPlanned.setTextColor(Color.parseColor(getString(R.color.colorPrimaryDark)));
                        displayPlanned.setText("");
                        estimateValue = 0;
                        addEstimate.setText(estimateValue.toString());
                    }
                }
            }
        });
        addDeadline.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (!plannedPage && (object.getIsPlanned() == false)) {
                    if (object.getHasDeadline() == false) {
                        addDeadline.setTextColor(Color.parseColor(getString(R.color.colorAccent)));
                        if (!deadlinePage) {
                            initiatePopUpWindowDeadline(v, object);
                        } else {
                            popupWindowDeadline.dismiss();
                            deadlinePage = false;
                            addDeadline.setTextColor(Color.parseColor(getString(R.color.colorPrimaryDark)));
                        }
                    } else {
                        object.setDeadline(false);
                        addDeadline.setTextColor(Color.parseColor(getString(R.color.colorPrimaryDark)));
                        displayDeadline.setText("");
                    }
                }
            }
        });
        addOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (object.getIsPlanned() == false) {
                    object.setEstimate(estimateValue);
                } else {
                    // This should calculate the length of the planned event.
                    //estimateValue = 666;
                    //object.setEstimate(estimateValue);
                    //Test Test
                }
                object.setTitle(addTitle.getText().toString());

                Intent result = new Intent();
                result.putExtra("AddObject", object);
                setResult(TodoListView.RESULT_OK, result);
                finish();
            }
        });
        addCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra("CancelObject", "null");
                setResult(TodoListView.RESULT_CANCELED, result);
                finish();
            }
        });
        addImportance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importanceInt++;
                if (importanceInt > 3) {
                    importanceInt = 0;
                }
                switch (importanceInt) {
                    case 0:
                        addImportance.setTextSize(25);
                        break;
                    case 1:
                        addImportance.setTextSize(50);
                        break;
                    case 2:
                        addImportance.setTextSize(80);
                        break;
                    case 3:
                        addImportance.setTextSize(100);
                        break;
                }
                object.setImportanceLevel(importanceInt);
            }
        });
    }
}