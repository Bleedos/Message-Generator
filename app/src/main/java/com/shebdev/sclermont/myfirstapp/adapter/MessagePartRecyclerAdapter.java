package com.shebdev.sclermont.myfirstapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shebdev.sclermont.myfirstapp.R;

/**
 * Created by sclermont on 17/08/15.
 */
public class MessagePartRecyclerAdapter extends RecyclerView.Adapter<MessagePartRecyclerAdapter.ViewHolder> {

    private String[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mImageView;
        public IMyViewHolderClicks mListener;

        public ViewHolder(View itemLayoutView, IMyViewHolderClicks listener) {
            super(itemLayoutView);
            mTextView = (TextView) itemLayoutView.findViewById(R.id.recycler_view_text);
            mImageView = (ImageView) itemLayoutView.findViewById(R.id.recycler_view_image);
            mListener = listener;
            mImageView.setOnClickListener(this);
            mTextView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(),
                    "click "+v.getClass().getCanonicalName(),
                    Toast.LENGTH_LONG).show();
            if (v instanceof TextView) {
                mListener.onPotato((TextView)v, getPosition());
            }
            else if (v instanceof ImageView) {
                mListener.onTomato((ImageView)v, getPosition());
            }
        }

        public static interface IMyViewHolderClicks {
            public void onPotato(TextView caller, int position);
            public void onTomato(ImageView caller, int position);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MessagePartRecyclerAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MessagePartRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_part_recycler_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, new MessagePartRecyclerAdapter.ViewHolder.IMyViewHolderClicks() {
            public void onPotato(TextView caller, int position) {
            // TODO: Onclick ici
                String texttt = caller.getText().toString();
                caller.setAlpha(0.6f);
                caller.append("bob" + position + "::" + mDataset[position]);
            };
            public void onTomato(ImageView caller, int position) {
                // TODO: Onclick ici
                caller.setAlpha(0.6f);
            }
        });
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset[position]);
        holder.mImageView.setImageResource(R.drawable.ic_delete_white_24dp);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}
