package se.kth.g2.planner;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/*
* Created by Jens Egeland
* Created by Rasmus Olsson
*/

public class TodoListView extends AppCompatActivity implements MyAdapter.ItemClickCallback {
    // This is a TAG for debuging the running application using "Android Monitor"
    private static final String TAG = "debugTAG";
    //                      Log.i(TAG, "onClick");

    // TODO Move this to MainActivity and let it be accessible to all other views from there
    public Typeface fontAwesome;
    private RecyclerView recView;
    private MyAdapter adapter;
    private ArrayList<ListObject> arrayListOfObjects;
    private ItemTouchHelper itemTouchHelper;
    private Button btnAddItem;
    private ListObject objectToChange;
    private ListObject objectGotCanceled;
    private int POSITION;
    private int ADD = 0;
    private int CHANGE = 1;

    EventHandler eventHandler;
    PlannerClass planner;

    private Button startAlgorithm;
    private Button goToCalender;

    private ArrayList<ScheduleObject> scheduleList;
    private ArrayList<ScheduleObject> schedulListTemp;

    //@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_view);

        // TODO Move this to MainActivity and let it be accessible to all other views from there
        this.fontAwesome = Typeface.createFromAsset(this.getAssets(), "fonts/fontawesome.ttf");
        eventHandler = EventHandler.getInstance();
        arrayListOfObjects = eventHandler.getListObjectsEvent();
        scheduleList = eventHandler.generateScheduleObjectList();
        planner = new PlannerClass();
        bindComponents();
        listener();
    }

    // bind objects to XML layout by XML id
    public void bindComponents() {
        recView = (RecyclerView) findViewById(R.id.display_recycle_viewer);
        btnAddItem = (Button) findViewById(R.id.add_item);

        startAlgorithm = (Button) findViewById(R.id.start_algorithm);
        goToCalender = (Button) findViewById(R.id.start_calender_view);

        itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recView);
        recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(arrayListOfObjects, this);
        recView.setAdapter(adapter);
        adapter.setItemClickCallback(this);
    }

    /*
     *   Method for swipe(delete) and move items in list
    */
    private ItemTouchHelper.Callback createHelperCallback() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                        return true;
                    }

                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        deleteItem(viewHolder.getAdapterPosition());
                    }
                };
        return simpleItemTouchCallback;
    }

    /*
        Change position on items in list
     */
    private void moveItem(int oldPos, int newPos) {
        ListObject obj = arrayListOfObjects.get(oldPos);
        arrayListOfObjects.remove(oldPos);
        arrayListOfObjects.add(newPos, obj);
        adapter.notifyItemMoved(oldPos, newPos);
    }

    /*
        Delete item in list
     */
    private void deleteItem(final int position) {
        ListObject removeObject = arrayListOfObjects.get(position);
        arrayListOfObjects.remove(position);
        adapter.notifyItemRemoved(position);
        eventHandler.removeListItemEvent(removeObject);
    }

    /*
        Set all listeners, to register clicks and swipes by the user
    */
    public void listener() {
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListObject item = new ListObject();
                Intent i = new Intent(TodoListView.this, AddItem.class);
                i.putExtra("addObject", item);
                startActivityForResult(i, ADD);
            }
        });
        startAlgorithm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "STARTED THE ALGORITHM");
                schedulListTemp = planner.generateSchedule(arrayListOfObjects);
                Log.i(TAG, "COMPLETED THE ALGORITHM");
                Log.i(TAG, "schedulListTemp.size(): " + ((Integer) (schedulListTemp.size())).toString());
                for (int i = 0; i < schedulListTemp.size(); i++) {
                    Log.i(TAG, schedulListTemp.get(i).listObject.getTitle() + "  Start -> End: " + schedulListTemp.get(i).getStartYear() + "/" + (schedulListTemp.get(i).getStartMonth() + 1) + "/" +
                            schedulListTemp.get(i).getStartDay() + " -> " + schedulListTemp.get(i).getEndYear() + "/" +
                            (schedulListTemp.get(i).getEndMonth() + 1) + "/" + schedulListTemp.get(i).getEndDay() + "  " +
                            schedulListTemp.get(i).getStartHour() + ":" + schedulListTemp.get(i).getStartMinute() +
                            " -> " + schedulListTemp.get(i).getEndHour() + ":" + schedulListTemp.get(i).getEndMinute());
                    scheduleList.add(schedulListTemp.get(i));
                    eventHandler.addScheduleItemEvent(scheduleList.get(i));
                }
            }
        });
        goToCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "GO TO CALENDER");
                Intent intent = new Intent(TodoListView.this, CalendarView.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemClick(int p) {
        POSITION = p;
        ListObject item = arrayListOfObjects.get(p);
        Intent i = new Intent(this, EditItem.class);
        i.putExtra("object", item);
        arrayListOfObjects.remove(p);
        adapter.notifyItemRemoved(p);
        startActivityForResult(i, CHANGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD) {
            if (resultCode == RESULT_OK) {
                objectToChange = (ListObject) data.getSerializableExtra("AddObject");
                arrayListOfObjects.add(objectToChange);
                adapter.notifyItemInserted(arrayListOfObjects.size() - 1);
                objectToChange.setID(eventHandler.addListItemEvent(objectToChange));
            } else {
            }
        } else if (requestCode == CHANGE) {
            if (resultCode == RESULT_OK) {
                objectToChange = (ListObject) data.getSerializableExtra("updatedObject");
                arrayListOfObjects.add(POSITION, objectToChange);
                adapter.notifyItemInserted(POSITION);
                eventHandler.modifyListItemEvent(objectToChange);
            } else {
                objectGotCanceled = (ListObject) data.getSerializableExtra("CancelObject");
                arrayListOfObjects.add(POSITION, objectGotCanceled);
                adapter.notifyItemInserted(POSITION);
            }
        }
    }

    /*
     * If we want more clickable features for objects in the list, this
     * method can be used.
     */

    //@Override
    //public void onSecondaryClick(int p) {
    //}

    /*
     * This is only a temporary method for loading a list of items.
     * Will be removed.
     */
    public void loadArray() {
        ListObject object1 = new ListObject();
        object1.setTitle("object1");
        object1.setEstimate(30);
        object1.setDeadline(false);
        object1.setPlanned(false);
        arrayListOfObjects.add(object1);
        adapter.notifyItemInserted(arrayListOfObjects.size() - 1);
        object1.setID(eventHandler.addListItemEvent(object1));

        ListObject object2 = new ListObject();
        object2.setTitle("object2");
        object2.setEstimate(45);
        object2.setDeadline(false);
        object2.setPlanned(false);
        arrayListOfObjects.add(object2);
        adapter.notifyItemInserted(arrayListOfObjects.size() - 1);
        object2.setID(eventHandler.addListItemEvent(object2));

        ListObject object3 = new ListObject();
        object3.setTitle("object3");
        object3.setEstimate(60);
        object3.setDeadline(false);
        object3.setPlanned(false);
        arrayListOfObjects.add(object3);
        adapter.notifyItemInserted(arrayListOfObjects.size() - 1);
        object3.setID(eventHandler.addListItemEvent(object3));

        ListObject object4 = new ListObject();
        object4.setTitle("object4");
        object4.setEstimate(70);
        object4.setDeadline(false);
        object4.setPlanned(false);
        arrayListOfObjects.add(object4);
        adapter.notifyItemInserted(arrayListOfObjects.size() - 1);
        object4.setID(eventHandler.addListItemEvent(object4));

        ListObject object5 = new ListObject();
        object5.setTitle("object5");
        object5.setEstimate(90);
        object5.setDeadline(false);
        object5.setPlanned(false);
        arrayListOfObjects.add(object5);
        adapter.notifyItemInserted(arrayListOfObjects.size() - 1);
        object5.setID(eventHandler.addListItemEvent(object5));

        ListObject object6 = new ListObject();
        object6.setTitle("object6");
        object6.setEstimate(80);
        object6.setDeadline(false);
        object6.setPlanned(false);
        arrayListOfObjects.add(object6);
        adapter.notifyItemInserted(arrayListOfObjects.size() - 1);
        object6.setID(eventHandler.addListItemEvent(object6));

        ListObject object7 = new ListObject();
        object7.setTitle("object7");
        object7.setEstimate(15);
        object7.setDeadline(false);
        object7.setPlanned(false);
        arrayListOfObjects.add(object7);
        adapter.notifyItemInserted(arrayListOfObjects.size() - 1);
        object7.setID(eventHandler.addListItemEvent(object7));
    }
}
