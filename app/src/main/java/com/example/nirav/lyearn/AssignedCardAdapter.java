package com.example.nirav.lyearn;

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

        import java.util.ArrayList;


public class AssignedCardAdapter extends RecyclerView.Adapter<AssignedCardAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<AssignedCards> mAssignedCardsArrayList;

    public AssignedCardAdapter(ArrayList<AssignedCards> cardsArrayList) {

        this.mAssignedCardsArrayList = cardsArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assigned_card, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        CardView cardView = holder.mCardView;
        TextView courseTitle = holder.courseTitle;
        TextView numberOfCards = holder.numberOfCards;
        TextView estimatedTime = holder.estimatedTime;
        TextView dueDateTime = holder.dueDateTime;
        TextView courseAssigner = holder.courseAssigner;

        courseTitle.setText(mAssignedCardsArrayList.get(listPosition).getCourseTitle());
        numberOfCards.setText(mAssignedCardsArrayList.get(listPosition).getNumberOfCards());
        estimatedTime.setText(mAssignedCardsArrayList.get(listPosition).getEstimatedTime());
        dueDateTime.setText(mAssignedCardsArrayList.get(listPosition).getDueDateTime());
        courseAssigner.setText(mAssignedCardsArrayList.get(listPosition).getCourseAssigner());
    }

    public static void runOnUiThread(Runnable runnable){
        final Handler UIHandler = new Handler(Looper.getMainLooper());
        UIHandler .post(runnable);
    }

    @Override
    public int getItemCount() {
        return mAssignedCardsArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView courseTitle;
        private TextView numberOfCards;
        private TextView estimatedTime;
        private TextView dueDateTime;
        private TextView courseAssigner;
        private CardView mCardView;
        public static View.OnClickListener cardOnClickListener;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.courseTitle = (TextView) itemView.findViewById(R.id.courseTitle);
            this.numberOfCards = (TextView) itemView.findViewById(R.id.numberOfCards);
            this.estimatedTime = (TextView) itemView.findViewById(R.id.estimatedTimeValue);
            this.dueDateTime = (TextView) itemView.findViewById(R.id.dueDateTimeValue);
            this.courseAssigner = (TextView) itemView.findViewById(R.id.assignedByValue);
            this.mCardView = (CardView) itemView.findViewById(R.id.cv);
        }
    }
}

