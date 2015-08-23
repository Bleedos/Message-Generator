package com.shebdev.sclermont.myfirstapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shebdev.sclermont.myfirstapp.R;
import com.shebdev.sclermont.myfirstapp.db.MessageDbHelper;
import com.shebdev.sclermont.myfirstapp.db.MessagePartData;

import java.util.ArrayList;

/**
 * Created by sclermont on 17/08/15.
 */
public class MessagePartSelectRecyclerAdapter extends RecyclerView.Adapter<MessagePartSelectRecyclerAdapter.ViewHolder> {

    private ArrayList<MessagePartData> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MessagePartSelectRecyclerAdapter(ArrayList<MessagePartData> mDatasetValue) {
        mDataset = mDatasetValue;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mDeleteImageView;
        public IMyViewHolderClicks mListener;

        public ViewHolder(View itemLayoutView, IMyViewHolderClicks listener) {
            super(itemLayoutView);
            mTextView = (TextView) itemLayoutView.findViewById(R.id.message_part_select_recycler_view_text);
            mDeleteImageView = (ImageView) itemLayoutView.findViewById(R.id.message_part_select_recycler_view_image);
            mListener = listener;
            mDeleteImageView.setOnClickListener(this);
            mTextView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(v.getContext(),
//                    "click "+v.getClass().getCanonicalName() + "::"+v.getId(),
//                    Toast.LENGTH_LONG).show();

            if (v instanceof TextView) {
                mListener.onClickMessagePart((TextView) v, getPosition());
            }
            else if (v instanceof ImageView) {
                mListener.onClickDelete((ImageView) v, getPosition());
            }
        }

        public static interface IMyViewHolderClicks {
            public void onClickMessagePart(TextView txtView, int position);
            public void onClickDelete(ImageView imgView, int position);
        }

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MessagePartSelectRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_part_select_recycler_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, new MessagePartSelectRecyclerAdapter.ViewHolder.IMyViewHolderClicks() {
            public void onClickMessagePart(TextView txtView, int position) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("message",(mDataset.get(position).getText()));
                ((Activity)txtView.getContext()).setResult(((Activity)txtView.getContext()).RESULT_OK, returnIntent);
                ((Activity)txtView.getContext()).finish();
            };
            public void onClickDelete(ImageView imgView, int position) {
                MessageDbHelper dbHelper = new MessageDbHelper(imgView.getContext());
                dbHelper.deletePartAssemblyLinkFromMessagePartId(mDataset.get(position).get_id());
                dbHelper.deleteMessagePart(mDataset.get(position).get_id());
                mDataset.remove(position);
                notifyItemRemoved(position);
            }
        });
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).getText());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public ArrayList<MessagePartData> getMDataset() {
        return mDataset;
    }

    public void changeMDataSet(ArrayList<MessagePartData> mDatasetValue) {
        mDataset = mDatasetValue;
        notifyDataSetChanged();
    }

}
