package com.e.wildebeest.history;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.e.wildebeest.R;
import com.e.wildebeest.history.HistoryFragment.OnListFragmentInteractionListener;
import com.e.wildebeest.history.content.HistoryContent.HistoryItem;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link HistoryItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> {

    private final List<HistoryItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public HistoryRecyclerViewAdapter(List<HistoryItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        // set the date
        SimpleDateFormat df = new SimpleDateFormat("MM/dd HH:mm");
        String date = df.format(holder.mItem.date);
        holder.mIdView.setText(date);

        // set the session name
        holder.mContentView.setText(holder.mItem.programName + ": " + holder.mItem.sessionName);

        //
        // show a different bg for current day
        //

        // record date is already set to local timezone
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String recordDate = dateFormat.format(holder.mItem.date);

        //todo: get day of month and show different backgrounds
        dateFormat = new SimpleDateFormat("dd"); // use "u" for weekdays
        String sDay = dateFormat.format(holder.mItem.date);
        int day = Integer.parseInt(sDay);

        // get today from local timezone
        //String today = dateFormat.format(new Date());
        if (day % 2 == 0) // change colors for even/odd dates
            holder.mCardLayout.setBackgroundResource(R.drawable.bg_history_list_gradient);
        else
            holder.mCardLayout.setBackgroundResource(R.drawable.bg_history_list_2);
        //
        // listen for the click
        //
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public HistoryItem mItem;
        public final RelativeLayout mCardLayout;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.textViewHistoryItemDatetime);
            mContentView = (TextView) view.findViewById(R.id.textViewHistoryItemTitle);
            mCardLayout = (RelativeLayout) view.findViewById(R.id.card_layout);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
