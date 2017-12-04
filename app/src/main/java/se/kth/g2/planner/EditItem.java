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

/*
 * Same functionality as the AddItem.java class, except the editCancel method,
 * were it now returns the pulled object, unchanged.
 */

public class EditItem extends AppCompatActivity {
    private static final String TAG = "debugTAG";
    //                      Log.i(TAG, "onClick");

    private Typeface fontAwesome;
    private Button editOk;
    private Button editCancel;
    private Button editEstimate;
    private TextView editPlanned;
    private TextView editDeadline;
    private TextView editDelete;
    private TextView addImportance;
    private TextView displayPlanned;
    private TextView displayDeadline;
    private EditText editTitle;
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
        setContentView(R.layout.activity_edit_item);
        this.fontAwesome = Typeface.createFromAsset(this.getAssets(), "fonts/fontawesome.ttf");
        ListObject object = (ListObject) getIntent().getSerializableExtra("object");
        bindComponents();
        getOldValues(object);
        listener(object);
    }

    private void bindComponents() {
        editOk = (Button) findViewById(R.id.edit_btn_ok_object);
        editCancel = (Button) findViewById(R.id.edit_btn_cancel_object);
        editEstimate = (Button) findViewById(R.id.edit_estimate_btn);
        addImportance = (TextView) findViewById(R.id.edit_importance_textview);
        importanceInt = 0;

        editPlanned = (TextView) findViewById(R.id.edit_planned_textview);
        editPlanned.setTypeface(fontAwesome);

        editDeadline = (TextView) findViewById(R.id.edit_deadline_textview);
        editDeadline.setTypeface(fontAwesome);

        sharedPref = getSharedPreferences("setting", Context.MODE_PRIVATE);

        editDelete = (TextView) findViewById(R.id.edit_delete_textview);
        editDelete.setTypeface(fontAwesome);

        displayPlanned = (TextView) findViewById(R.id.display_planned);
        displayDeadline = (TextView) findViewById(R.id.display_deadline);
        editTitle = (EditText) findViewById(R.id.edit_title_edittext);
    }

    private void getOldValues(ListObject object) {
        if (object.getIsPlanned()) {
            editPlanned.setTextColor(Color.parseColor(getString(R.color.colorAccent)));
            displayPlanned.setText(object.getStartDay() + "/" + object.getStartMonth() + "/" + object.getStartYear() +
            "  " + object.getStartHour() + ":" + object.getStartMinute() + " - " + object.getEndHour() + ":" + object.getEndMinute());

        } else {
            editPlanned.setTextColor(Color.parseColor(getString(R.color.colorPrimaryDark)));
            displayPlanned.setText("");
        }
        if (object.getHasDeadline()) {
            editDeadline.setTextColor(Color.parseColor(getString(R.color.colorAccent)));
            displayDeadline.setText(object.getDeadlineDay() + "/" + object.getDeadlineMonth() + "/" + object.getDeadlineDay() +
            "  " + object.getDeadlineHour() + ":" + object.getDeadlineMinute());
        } else {
            editDeadline.setTextColor(Color.parseColor(getString(R.color.colorPrimaryDark)));
            displayDeadline.setText("");
        }
        deadlinePage = false;
        plannedPage = false;
        estimateValue = object.getEstimate();
        editEstimate.setText(estimateValue.toString());
        editTitle.setText(object.getTitle());

        importanceInt = object.getImportanceLevel();
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
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void initiatePopUpWindowPlanned(View v, final ListObject object) {
        layoutInflaterPlanned = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup container = (ViewGroup) layoutInflaterPlanned.inflate(R.layout.popup_menu_planned, null);
        popupWindowPlanned = new PopupWindow(container, 1150, 630);
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
                editPlanned.setTextColor(Color.parseColor(getString(R.color.colorPrimaryDark)));
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
                plannedPage = false;

                dayPlanned = datePicker_planned.getDayOfMonth();
                monthPlanned = (datePicker_planned.getMonth() + 1);
                yearPlanned = datePicker_planned.getYear();
                hourPlanned1 = timePicker_planned1.getHour();
                hourPlanned2 = timePicker_planned2.getHour();
                minPlanned1 = timePicker_planned1.getMinute();
                minPlanned2 = timePicker_planned2.getMinute();

                object.setStartDate(yearPlanned, monthPlanned, dayPlanned, hourPlanned1, minPlanned1);
                object.setEndDate(yearPlanned, monthPlanned, dayPlanned, hourPlanned2, minPlanned2);

                estimateValue = (hourPlanned2 * 60 + minPlanned2) - (hourPlanned1 * 60 + minPlanned1);
                object.setEstimate(estimateValue);
                editEstimate.setText(estimateValue.toString());

                String curTime1 = String.format("%02d:%02d", hourPlanned1, minPlanned1);
                String curTime2 = String.format("%02d:%02d", hourPlanned2, minPlanned2);
                displayPlanned.setText(dayPlanned.toString() + "/" + monthPlanned.toString() + "/" + yearPlanned.toString() +
                        "  " + curTime1 + " - " + curTime2);

                popupWindowPlanned.dismiss();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void initiatePopUpWindowDeadline(View v, final ListObject object) {
        layoutInflaterDeadline = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup container1 = (ViewGroup) layoutInflaterDeadline.inflate(R.layout.popup_menu_deadline, null);
        popupWindowDeadline = new PopupWindow(container1, 1150, 630);
        popupWindowDeadline.showAtLocation(v, Gravity.CENTER, 0, -350);
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
                editDeadline.setTextColor(Color.parseColor(getString(R.color.colorPrimaryDark)));
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
                deadlinePage = false;

                dayDeadline = datePicker_deadline.getDayOfMonth();
                monthDeadline = (datePicker_deadline.getMonth() + 1);
                yearDeadline = datePicker_deadline.getYear();
                hourDeadline = timePicker_deadline.getHour();
                minDeadline = timePicker_deadline.getMinute();
                String curTime = String.format("%02d:%02d", hourDeadline, minDeadline);
                object.setDeadlineDate(yearDeadline, monthDeadline, dayDeadline, hourDeadline, minDeadline);
                displayDeadline.setText(dayDeadline.toString() + "/" + monthDeadline.toString() + "/" + yearDeadline.toString() + "  " + curTime);
                popupWindowDeadline.dismiss();
            }
        });

    }

    private void listener(final ListObject object) {
        editEstimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estimateValue += 15;
                if (estimateValue > 180) {
                    estimateValue = 0;
                }
                editEstimate.setText(estimateValue.toString());
            }
        });
        editPlanned.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (!deadlinePage && (object.getHasDeadline() == false)) {
                    if (object.getIsPlanned() == false) {
                        editPlanned.setTextColor(Color.parseColor(getString(R.color.colorAccent)));
                        if (!plannedPage) {
                            initiatePopUpWindowPlanned(v, object);
                        } else {
                            popupWindowPlanned.dismiss();
                            plannedPage = false;
                            editPlanned.setTextColor(Color.parseColor(getString(R.color.colorPrimaryDark)));
                        }
                    } else {
                        object.setPlanned(false);
                        displayPlanned.setText("");
                        editPlanned.setTextColor(Color.parseColor(getString(R.color.colorPrimaryDark)));
                    }
                }
            }
        });
        editDeadline.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (!plannedPage && (object.getIsPlanned() == false)) {
                    if (object.getHasDeadline() == false) {
                        editDeadline.setTextColor(Color.parseColor(getString(R.color.colorAccent)));
                        if (!deadlinePage) {
                            initiatePopUpWindowDeadline(v, object);
                        } else {
                            popupWindowDeadline.dismiss();
                            deadlinePage = false;
                            editDeadline.setTextColor(Color.parseColor(getString(R.color.colorPrimaryDark)));
                        }
                    } else {
                        object.setDeadline(false);
                        displayDeadline.setText("");
                        editDeadline.setTextColor(Color.parseColor(getString(R.color.colorPrimaryDark)));
                    }
                }
            }
        });
        editOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (object.getIsPlanned() == false) {
                    object.setEstimate(estimateValue);
                } else {
                    // This should calculate the length of the planned event.
                    //estimateValue = 666;
                    //object.setEstimate(estimateValue);
                }
                object.setTitle(editTitle.getText().toString());

                Intent result = new Intent();
                result.putExtra("updatedObject", object);
                setResult(TodoListView.RESULT_OK, result);
                finish();
            }
        });
        editCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra("CancelObject", object);
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
