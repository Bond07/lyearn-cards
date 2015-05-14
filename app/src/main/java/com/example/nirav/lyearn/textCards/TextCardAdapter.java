package com.example.nirav.lyearn.textCards;

/**
 * Created by nirav on 04/02/15.
 */
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nirav.lyearn.R;

import java.util.ArrayList;


public class TextCardAdapter extends RecyclerView.Adapter<TextCardAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<TextCards> mTextCardsArrayList;

    public TextCardAdapter(ArrayList<TextCards> cardsArrayList) {

        this.mTextCardsArrayList = cardsArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_card, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView title = holder.title;
        TextView description = holder.description;

        title.setText(mTextCardsArrayList.get(listPosition).getTitle());
        description.setText(mTextCardsArrayList.get(listPosition).getDescription());
    }

    public static void runOnUiThread(Runnable runnable){
        final Handler UIHandler = new Handler(Looper.getMainLooper());
        UIHandler .post(runnable);
    }

    @Override
    public int getItemCount() {
        return mTextCardsArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.description = (TextView) itemView.findViewById(R.id.description);
        }
    }
}

