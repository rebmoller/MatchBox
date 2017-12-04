package se.kth.g2.planner;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

/*
* Created by Jens Egeland
* Created by Rasmus Olsson
*/

/**
 * MyAdapter is a class to handle the ViewHolder and the data
 * ** that is shown in every list object, in the recycler viewer.
 * ** The intention with an Adapter is to provide the recycle view with
 * ** new views.
 **/

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private static final String TAG = "debugTAG";   //A debugg-tag
    private ArrayList<ListObject> objectList;       //Initilizeing the ArrayList
    private LayoutInflater inflater;
    private Context context;                        //Context and Typeface is created
    private Typeface fontAwesome;
    private ItemClickCallback itemClickCallback;


    public interface ItemClickCallback {
        void onItemClick(int p);

        //void onSecondaryClick(int p);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    /**
     * Creating a ViewHolder-object that is also initiated and
     * ** bounded with the placeholders for every listobjects.
     * **  The intention of this method is to contain a reference to
     * **  root view objects for the item. Also cache view objects represented
     * **  in layout.
     **/

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView objectTitle;
        public TextView objectEstimate;
        public TextView objectPlannedIcon;
        public TextView objectDeadlineIcon;
        public TextView objectDisplayDeadline;
        public TextView objectDisplayPlanned;
        public TextView objectDisplayImportance;
        public View container;

        public MyViewHolder(View view) {
            super(view);
            objectTitle = (TextView) view.findViewById(R.id.item_title);
            objectEstimate = (TextView) view.findViewById(R.id.item_estimate);
            objectPlannedIcon = (TextView) view.findViewById(R.id.item_icon_planned);
            objectDeadlineIcon = (TextView) view.findViewById(R.id.item_icon_deadline);
            objectDisplayPlanned = (TextView) view.findViewById(R.id.item_display_planned);
            objectDisplayDeadline = (TextView) view.findViewById(R.id.item_display_deadline);
            objectDisplayImportance = (TextView) view.findViewById(R.id.item_display_importance);
            container = view.findViewById(R.id.item_list_viewer);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.item_list_viewer) {
                itemClickCallback.onItemClick(getAdapterPosition());
            }
        }
    }

    public MyAdapter(ArrayList<ListObject> objectList, Context c) {
        inflater = LayoutInflater.from(c);
        this.objectList = objectList;
    }

    /**
     * onCreateViewHolder, this method creates a view from the initiated ViewHolder-object
     * ** and sets the layout to the item_row XML file, and then returns the View
     **/

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        fontAwesome = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome.ttf");
        View view = inflater.inflate(R.layout.recycleview_item_row, parent, false);
        return new MyViewHolder(view);
    }

    /**
     * ** Binds the listobject values to the right placeholder
     **/

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ListObject listObj = objectList.get(position);
        holder.objectTitle.setText(listObj.getTitle());
        holder.objectEstimate.setText(((Integer) listObj.getEstimate()).toString());
        holder.objectEstimate.setTextColor(Color.parseColor("#0b2538"));


        if (listObj.isPlanned()) {
            holder.objectPlannedIcon.setTextColor(Color.parseColor("#FFFE424D"));
            holder.objectPlannedIcon.setTypeface(fontAwesome);
            holder.objectPlannedIcon.setText(context.getString(R.string.icon_planned_event));

            String curTime1 = String.format("%02d:%02d", listObj.getStartHour(), listObj.getStartMinute());
            String curTime2 = String.format("%02d:%02d", listObj.getEndHour(), listObj.getEndMinute());
            holder.objectDisplayPlanned.setText(listObj.getStartDay() + "/" + (listObj.getStartMonth() + 1) +
                    "/" + listObj.getStartYear() + "  " + curTime1 + " - " + curTime2);
            holder.objectDisplayDeadline.setText("");
        } else {
            holder.objectDisplayPlanned.setText("");
            holder.objectPlannedIcon.setText("");
            holder.objectPlannedIcon.setTextColor(Color.parseColor("#FFFFFF"));
        }

        if (listObj.hasDeadline()) {
            holder.objectDeadlineIcon.setTextColor(Color.parseColor("#FFFE424D"));
            holder.objectDeadlineIcon.setTypeface(fontAwesome);
            holder.objectDeadlineIcon.setText(context.getString(R.string.icon_deadline_event));
            String deadlineTime = String.format("%02d:%02d", listObj.getDeadlineHour(), listObj.getDeadlineMinute());
            holder.objectDisplayDeadline.setText(listObj.getDeadlineDay() + "/" + (listObj.getDeadlineMonth() + 1) + "/" + listObj.getDeadlineYear() + "  " + deadlineTime);
            holder.objectDisplayPlanned.setText("");
        } else {
            holder.objectDisplayDeadline.setText("");
            holder.objectDeadlineIcon.setText("");
            holder.objectDeadlineIcon.setTextColor(Color.parseColor("#FFFFFF"));
        }
        if (listObj.getImportanceLevel() == 0) {
            holder.objectDisplayImportance.setText("");
        } else {
            switch (listObj.getImportanceLevel()) {
                case 1:
                    holder.objectDisplayImportance.setText("Importance level: " + listObj.getImportanceLevel());
                    break;
                case 2:
                    holder.objectDisplayImportance.setText("Importance level: " + listObj.getImportanceLevel());
                    break;
                case 3:
                    holder.objectDisplayImportance.setText("Importance level: " + listObj.getImportanceLevel());
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }
}
