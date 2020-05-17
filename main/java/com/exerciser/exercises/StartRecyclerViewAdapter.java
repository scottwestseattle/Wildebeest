package com.exerciser.exercises;

import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.exerciser.R;
import com.exerciser.Tools;
import com.exerciser.exercises.StartFragment.OnListFragmentInteractionListener;
import com.exerciser.exercises.content.ExerciseContent;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ExerciseItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class StartRecyclerViewAdapter extends RecyclerView.Adapter<StartRecyclerViewAdapter.ViewHolder> {

    private final List<ExerciseContent.ExerciseItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public StartRecyclerViewAdapter(List<ExerciseContent.ExerciseItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_start, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).name);
        holder.mContentView.setText(Integer.toString(mValues.get(position).runSeconds) + " seconds");

        // make thumbnails so we don't exhaust memory on cheap phones
        String imageName = mValues.get(position).imageName;
        int id = holder.mView.getResources().getIdentifier(imageName, "drawable", holder.mView.getContext().getPackageName());
        //orig: holder.mImageView.setImageResource(id);
        holder.mImageView.setImageBitmap(
            Tools.getThumbnail(holder.mView.getResources(), id, 100, 100));

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
        public final ImageView mImageView;
        public ExerciseContent.ExerciseItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.program_name);
            mContentView = (TextView) view.findViewById(R.id.content);
            mImageView = (ImageView) view.findViewById(R.id.program_photo);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
