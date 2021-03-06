package com.shebdev.sclermont.myfirstapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.shebdev.sclermont.myfirstapp.MessagePartEdit;
import com.shebdev.sclermont.myfirstapp.MyActivity;
import com.shebdev.sclermont.myfirstapp.MyApplication;
import com.shebdev.sclermont.myfirstapp.R;
import com.shebdev.sclermont.myfirstapp.db.MessagePartData;

import java.util.ArrayList;

/**
 * Created by sclermont on 17/08/15.
 */
public class MessagePartRecyclerAdapter extends RecyclerView.Adapter<MessagePartRecyclerAdapter.ViewHolder> {

    // TODO: Changer pour une liste de MessagePartData ou quelquechose du genre.  Si l'id du mpd est null, c'est un message pas encore en bd.  c'est ce qui va permettre de gerer si on cree un nouveau message.  si id pas null, on pourra verifier si le message a changé en bd ou si le nom du fichier audio a changé
    private ArrayList<MessagePartData> mDataset;
    private long assemblyId;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MessagePartRecyclerAdapter(ArrayList<MessagePartData> mDatasetValue, long assemblyIdValue) {
        mDataset = mDatasetValue;
        assemblyId = assemblyIdValue;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mMoveDownImageView;
        public ImageView mMoveUpImageView;
        public ImageView mDeleteImageView;
        public IMyViewHolderClicks mListener;

        public ViewHolder(View itemLayoutView, IMyViewHolderClicks listener) {
            super(itemLayoutView);
            mTextView = (TextView) itemLayoutView.findViewById(R.id.recycler_view_text);
            mMoveDownImageView = (ImageView) itemLayoutView.findViewById(R.id.recycler_view_move_down);
            mMoveUpImageView = (ImageView) itemLayoutView.findViewById(R.id.recycler_view_move_up);
            mDeleteImageView = (ImageView) itemLayoutView.findViewById(R.id.recycler_view_image);
            mListener = listener;
            mMoveDownImageView.setOnClickListener(this);
            mMoveUpImageView.setOnClickListener(this);
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
                switch (v.getId()) {
                    case R.id.recycler_view_move_down:
                        mListener.onClickMoveDown((ImageView) v, getPosition());
                        break;
                    case R.id.recycler_view_move_up:
                        mListener.onClickMoveUp((ImageView) v, getPosition());
                        break;
                    case R.id.recycler_view_image:
                        mListener.onClickDelete((ImageView) v, getPosition());
                        break;
                }
            }
        }

        public static interface IMyViewHolderClicks {
            public void onClickMessagePart(TextView txtView, int position);
            public void onClickMoveDown(ImageView imgView, int position);
            public void onClickMoveUp(ImageView imgView, int position);
            public void onClickDelete(ImageView imgView, int position);
        }

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MessagePartRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_message_part_in_assembly, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, new MessagePartRecyclerAdapter.ViewHolder.IMyViewHolderClicks() {
            public void onClickMessagePart(TextView txtView, int position) {
                Intent intent = new Intent(txtView.getContext(), MessagePartEdit.class);
                intent.putExtra(MyActivity.EXTRA_MESSAGE_PART, txtView.getText());
                intent.putExtra(MyActivity.EXTRA_MESSAGE_PART_POSITION, position);
                intent.putExtra(MyActivity.EXTRA_MESSAGE_PART_AUDIO_FILE, ((MyActivity) txtView.getContext()).getMAdapter().getMDataset().get(position).getAudioFileName());
                ((Activity) txtView.getContext()).startActivityForResult(intent, MyActivity.REQUEST_CODE_MESSAGE_PART_EDIT);
            }
            public void onClickMoveDown(ImageView imgView, int position) {
                if (position < mDataset.size()-1) {
                    MessagePartData tmp = mDataset.get(position+1);
                    mDataset.set(position+1, mDataset.get(position));
                    mDataset.set(position, tmp);
                    notifyItemChanged(position);
                    notifyItemChanged(position + 1);
                }
            }
            public void onClickMoveUp(ImageView imgView, int position) {
                if (position > 0) {
                    MessagePartData tmp = mDataset.get(position-1);
                    mDataset.set(position-1, mDataset.get(position));
                    mDataset.set(position, tmp);
                    notifyItemChanged(position);
                    notifyItemChanged(position-1);
                }
            }
            public void onClickDelete(ImageView imgView, int position) {
                mDataset.remove(position);
                notifyItemRemoved(position);
                ((MyActivity) imgView.getContext()).adapterChanged();
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
        holder.mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, MyApplication.RECYCLER_VIEW_TEXT_SIZE);
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(MyApplication.EVEN_BACKGROUND_COLOR);
        }
        else {
            holder.itemView.setBackgroundColor(MyApplication.ODD_BACKGROUND_COLOR);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Methodes utilitaires
    public void addLine(MessagePartData mpd) {
        mDataset.add(mpd);
        notifyItemInserted(mDataset.size());
    }

    public void editLine(MessagePartData mpd, int position) {
        mDataset.set(position, mpd);
        notifyItemChanged(position);
    }

    public ArrayList<MessagePartData> getMDataset() {
        return mDataset;
    }

    public void changeMDataSet(ArrayList<MessagePartData> mDatasetValue) {
        mDataset = mDatasetValue;
        notifyDataSetChanged();
    }

    public long getAssemblyId() {
        return assemblyId;
    }

    public void setAssemblyId(long assemblyId) {
        this.assemblyId = assemblyId;
    }



}
