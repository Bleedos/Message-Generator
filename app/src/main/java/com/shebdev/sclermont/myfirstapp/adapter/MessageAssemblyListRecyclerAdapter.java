package com.shebdev.sclermont.myfirstapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shebdev.sclermont.myfirstapp.MessageAssemblyListActivity;
import com.shebdev.sclermont.myfirstapp.R;
import com.shebdev.sclermont.myfirstapp.db.MessageAssemblyData;
import com.shebdev.sclermont.myfirstapp.db.MessagePartData;

import java.util.ArrayList;

/**
 * Created by sclermont on 17/08/15.
 */
public class MessageAssemblyListRecyclerAdapter extends RecyclerView.Adapter<MessageAssemblyListRecyclerAdapter.ViewHolder> {

    private ArrayList<MessageAssemblyData> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mTextView;
        public IMyViewHolderClicks mListener;

        public ViewHolder(View itemLayoutView, IMyViewHolderClicks listener) {
            super(itemLayoutView);
            mTextView = (TextView) itemLayoutView.findViewById(R.id.assembly_list_recycler_view_text);
            mListener = listener;
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
        }

        public static interface IMyViewHolderClicks {
            public void onClickMessagePart(TextView txtView, int position);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MessageAssemblyListRecyclerAdapter(ArrayList<MessageAssemblyData> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MessageAssemblyListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assembly_list_recycler_view_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, new MessageAssemblyListRecyclerAdapter.ViewHolder.IMyViewHolderClicks() {
            public void onClickMessagePart(TextView txtView, int position) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("assemblyId", ((MessageAssemblyData)mDataset.get(position)).get_id());
                Activity host = (Activity) txtView.getContext();
                host.setResult(host.RESULT_OK, returnIntent);
                host.finish();

                // TODO: Au lieu d'appender bob, retourner l'id ou l'obj au complet pour affichage
                // dans la page principale (remplacer recyclder view de page principale par nouveau
                // contenu sélectionné
            }
        });
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).getTitle());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
