package com.exerciser.sessions;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exerciser.R;
import com.exerciser.sessions.SessionsFragment.OnListFragmentInteractionListener;
import com.exerciser.sessions.content.SessionContent.SessionItem;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link SessionItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SessionsRecyclerViewAdapter extends RecyclerView.Adapter<SessionsRecyclerViewAdapter.ViewHolder> {

    private final List<SessionItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    int counter = 0;

    public SessionsRecyclerViewAdapter(List<SessionItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_sessions, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.dayLabel.setText("Day " + Integer.toString(position + 1));
        //holder.sessionNumber.setText(position + 1);
        String name = holder.mItem.exerciseCount + " exercises";
        holder.programName.setText(name);

        String time = new SimpleDateFormat("mm:ss").format(new Date(((long) holder.mItem.seconds) * 1000));
        String description = "Total Time: " + time;
        holder.programDescription.setText(description);

        int counterMax = 6;
        if (counter == 0)
            holder.cardLayout.setBackgroundResource(R.drawable.bg_sessions_gradient_red);
        else if (counter == 1)
            holder.cardLayout.setBackgroundResource(R.drawable.bg_sessions_gradient_green);
        else if (counter == 2)
            holder.cardLayout.setBackgroundResource(R.drawable.bg_sessions_gradient_blue);
        else if (counter == 3)
            holder.cardLayout.setBackgroundResource(R.drawable.bg_sessions_gradient_purple);
        else if (counter == 4)
            holder.cardLayout.setBackgroundResource(R.drawable.bg_sessions_gradient_orange);
        else if (counter == 5)
            holder.cardLayout.setBackgroundResource(R.drawable.bg_sessions_gradient_yellow);
        else if (counter == 6)
            holder.cardLayout.setBackgroundResource(R.drawable.bg_sessions_gradient_gray);

        counter++;
        if (counter > counterMax)
            counter = 0;

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

        final View mView;
        SessionItem mItem;

        CardView card_view;
        TextView programName;
        TextView programDescription;
        TextView dayLabel;
        RelativeLayout cardLayout;
        TextView sessionNumber;

        ViewHolder(View view) {
            super(view);
            mView = view;

            card_view = (CardView) view.findViewById(R.id.card_view);
            programName = (TextView)itemView.findViewById(R.id.program_name);
            programDescription = (TextView)itemView.findViewById(R.id.program_description);
            dayLabel = (TextView)itemView.findViewById(R.id.day_label);
            cardLayout = (RelativeLayout) view.findViewById(R.id.card_layout);
            //sessionNumber = (TextView) itemView.findViewById(R.id.textViewSessionNumber);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
