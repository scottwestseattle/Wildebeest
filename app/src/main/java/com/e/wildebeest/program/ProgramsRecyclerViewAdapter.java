package com.e.wildebeest.program;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;

import com.e.wildebeest.program.ProgramsFragment.OnListFragmentInteractionListener;
import com.e.wildebeest.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ProgramItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ProgramsRecyclerViewAdapter extends RecyclerView.Adapter<ProgramsRecyclerViewAdapter.ViewHolder> {

    private List<ProgramItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ProgramsRecyclerViewAdapter(List<ProgramItem> items, OnListFragmentInteractionListener listener) {
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
        holder.programLayout.setBackgroundResource(ProgramContent.getBackgroundImageResourceId(mValues.get(position).imageId));

        ProgramItem program = mValues.get(position);
        int next = program.sessionNext;
        holder.continueButton.setTag(program);
        if (next >= 0) {
            String name = program.sessionMap.get(next).name;
            holder.continueButton.setText("Continue: " + name);
        }
        else {
            holder.continueButton.setVisibility(View.INVISIBLE);
        }

        // show number of sessions
        int cnt = mValues.get(position).sessionCount;
        String sessionCount = Integer.toString(cnt) + ((cnt == 1) ? " session" : " sessions");
        holder.sessionCount.setText(sessionCount);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (null != mListener) {
                mListener.onListFragmentInteraction(holder.mItem);
            }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void updatePrograms(List<ProgramItem> programs)
    {
        this.mValues = programs;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ProgramItem mItem;

        CardView card_view;
        TextView programName;
        TextView programDescription;
        TextView sessionCount;
        RelativeLayout programLayout;
        Button continueButton;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            card_view = (CardView) view.findViewById(R.id.card_view);
            programName = (TextView)view.findViewById(R.id.program_name);
            programDescription = (TextView)view.findViewById(R.id.program_description);
            sessionCount = (TextView)view.findViewById(R.id.session_count);
            programLayout = (RelativeLayout) view.findViewById(R.id.program_layout);
            continueButton = (Button) view.findViewById(R.id.buttonStart);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + programName.getText() + "'";
        }


    }
}
