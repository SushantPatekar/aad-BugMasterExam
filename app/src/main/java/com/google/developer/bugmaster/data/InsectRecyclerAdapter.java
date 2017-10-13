package com.google.developer.bugmaster.data;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.developer.bugmaster.R;
import com.google.developer.bugmaster.views.DangerLevelView;

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
         TextView friendlyName, scientificName, dangerLevel;
        DangerLevelView dangerLevelView;
        public InsectHolder(View itemView) {
            super(itemView);
            friendlyName = (TextView) itemView.findViewById(R.id.friendlyName);
            scientificName = (TextView) itemView.findViewById(R.id.scientificName);
            dangerLevel = (TextView) itemView.findViewById(R.id.text1);
            dangerLevelView=(DangerLevelView)itemView.findViewById(R.id.avatar);

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

        Log.i("Value","-------------   "+insectname);
       String dangerString = "" + dangerlevelInt;


        holder.dangerLevel.setText(dangerString+""+position);

        holder.friendlyName.setText(insectRName);
        holder.scientificName.setText(scienceRName);
       // holder.dangerLevelView.setDangerLevel(dangerlevel);
        holder.dangerLevelView.setText("HI");
        holder.dangerLevelView.setTextColor(mContext.getResources().getColor(R.color.color_white));


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
        int[] colorDangerarray = mContext.getResources().getIntArray(R.array.dangerColors);

        switch(danger) {
            case 1: priorityColor = ContextCompat.getColor(mContext, colorDangerarray[0]);
                break;
            case 2: priorityColor = ContextCompat.getColor(mContext, colorDangerarray[1]);
                break;
            case 3: priorityColor = ContextCompat.getColor(mContext, colorDangerarray[2]);
                break;
            default: break;
        }
        return priorityColor;
    }

}
