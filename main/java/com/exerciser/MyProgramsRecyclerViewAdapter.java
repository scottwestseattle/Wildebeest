package com.exerciser;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exerciser.ProgramsFragment.OnListFragmentInteractionListener;
import com.exerciser.Program.ProgramContent.ProgramItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ProgramItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyProgramsRecyclerViewAdapter extends RecyclerView.Adapter<MyProgramsRecyclerViewAdapter.ViewHolder> {

    private final List<ProgramItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyProgramsRecyclerViewAdapter(List<ProgramItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_programs, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.programName.setText(mValues.get(position).name);
        holder.programDescription.setText(mValues.get(position).description);

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
        public ProgramItem mItem;

        CardView card_view;
        TextView programName;
        TextView programDescription;
        TextView sessionCount;
        RelativeLayout programLayout;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            card_view = (CardView) view.findViewById(R.id.card_view);
            programName = (TextView)view.findViewById(R.id.program_name);
            programDescription = (TextView)view.findViewById(R.id.program_description);
            sessionCount = (TextView)view.findViewById(R.id.session_count);
            programLayout = (RelativeLayout) view.findViewById(R.id.program_layout);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + programName.getText() + "'";
        }
    }
}
