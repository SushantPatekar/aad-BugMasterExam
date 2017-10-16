package com.google.developer.bugmaster.data;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.developer.bugmaster.R;
import com.google.developer.bugmaster.views.CircularTextView;
import com.google.developer.bugmaster.views.DangerLevelView;

import java.util.ArrayList;

/**
 * RecyclerView adapter extended with project-specific required methods.
 */

public class InsectRecyclerAdapter extends
        RecyclerView.Adapter<InsectRecyclerAdapter.InsectHolder> {


   // ImageView image;

    public InsectRecyclerAdapter(Context mcontext,Cursor mCursor) {
        this.mContext=mcontext;
        this.mCursor = mCursor;
        Log.i("Value","-------------   "+mCursor.getCount());
    }

    /* ViewHolder for each insect item */
    public class InsectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
         TextView friendlyName, scientificName;
        //DangerLevelView dangerLevelView;
        CircularTextView circularTextView;
        public InsectHolder(View itemView) {
            super(itemView);
            friendlyName = (TextView) itemView.findViewById(R.id.friendlyName);
            scientificName = (TextView) itemView.findViewById(R.id.scientificName);
          //  dangerLevel = (TextView) itemView.findViewById(R.id.text1);
            circularTextView=(CircularTextView)itemView.findViewById(R.id.circularTextView);
          //  dangerLevelView=(DangerLevelView)itemView.findViewById(R.id.avatar);

        }

        @Override
        public void onClick(View v) {
        }
    }

    private Cursor mCursor;
    private Context mContext;
    @Override
    public InsectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.bugs_list_item, parent, false);
        return new InsectHolder(view);
    }

    @Override
    public void onBindViewHolder(InsectHolder holder, int position) {


       int insectname = mCursor.getColumnIndex(BugsContract.BugEntry.COLUMN_NAME);
        int scienceName = mCursor.getColumnIndex(BugsContract.BugEntry.COLUMN_SCIENTIFICNAME);
        int id = mCursor.getColumnIndex(BugsContract.BugEntry._ID);
        int dangerlevel = mCursor.getColumnIndex(BugsContract.BugEntry.COLUMN_DANGERLEVEL);
        int insectImage = mCursor.getColumnIndex(BugsContract.BugEntry.COLUMN_IMAGEASSET);
        if (!mCursor.moveToPosition(position))
            return;

        String insectRName = mCursor.getString(insectname);
        String scienceRName = mCursor.getString(scienceName);
        String insectRImage = mCursor.getString(insectImage);
        int dangerlevelInt = mCursor.getInt(dangerlevel);



        holder.friendlyName.setText(insectRName);
        holder.scientificName.setText(scienceRName);
        holder.circularTextView.setSolidColor(getDangerLevelColor(dangerlevelInt));
        holder.circularTextView.setText(""+dangerlevelInt);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    /**
     * Return the {@link Insect} represented by this item in the adapter.
     *
     * @param position Adapter item position.
     *
     * @return A new {@link Insect} filled with this position's attributes
     *
     * @throws IllegalArgumentException if position is out of the adapter's bounds.
     */
    public Insect getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            throw new IllegalArgumentException("Item position is out of adapter's range");
        } else if (mCursor.moveToPosition(position)) {
            return new Insect(mCursor);
        }
        return null;
    }




   private int getDangerColor(int danger) {
        int priorityColor = 0;
       String colors[] = mContext.getResources().getStringArray(R.array.dangerColors);
       ArrayList<Integer>colorList = new ArrayList<Integer>();

        switch(danger) {
            case 1: priorityColor = ContextCompat.getColor(mContext, colorList.get(0));
                break;
            case 2: priorityColor = ContextCompat.getColor(mContext, colorList.get(1));
                break;
            case 3: priorityColor = ContextCompat.getColor(mContext, colorList.get(2));
                break;
            case 4: priorityColor = ContextCompat.getColor(mContext, colorList.get(3));
                break;
            case 5: priorityColor = ContextCompat.getColor(mContext, colorList.get(0));
                break;
            case 6: priorityColor = ContextCompat.getColor(mContext, colorList.get(4));
                break;
            case 7: priorityColor = ContextCompat.getColor(mContext, colorList.get(5));
                break;
            case 8: priorityColor = ContextCompat.getColor(mContext, colorList.get(6));
                break;
            case 9: priorityColor = ContextCompat.getColor(mContext, colorList.get(8));
                break;
            case 10: priorityColor = ContextCompat.getColor(mContext, colorList.get(9));
                break;
            default: break;
        }
        return priorityColor;
    }

    private String getDangerLevelColor(int danger){
        String priorityColor = null;
        String colors[] = mContext.getResources().getStringArray(R.array.dangerColors);
        switch(danger) {
            case 1: priorityColor = colors[0];
                break;
            case 2: priorityColor = colors[1];
                break;
            case 3: priorityColor = colors[2];
                break;
            case 4: priorityColor = colors[3];
                break;
            case 5: priorityColor = colors[4];
                break;
            case 6: priorityColor = colors[5];
                break;
            case 7: priorityColor = colors[6];
                break;
            case 8: priorityColor = colors[7];
                break;
            case 9: priorityColor = colors[8];
                break;
            case 10: priorityColor = colors[9];
                break;
            default: break;
        }
        return  priorityColor;
    }

}
