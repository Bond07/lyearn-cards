package com.example.nirav.lyearn;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.nirav.lyearn.textCards.TextCardActivity;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AssignedCardFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private AssignedCardAdapter mCardAdapter;

    private OnFragmentInteractionListener mListener;

    public AssignedCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_assigned_card, container, false);

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.card_assigned);

        mCardAdapter = new AssignedCardAdapter(getData());

//        if (mListener != null) {
//            mListener.onFragmentInteraction("Assigned Cards");
//        }

        mRecyclerView.setAdapter(mCardAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        switch (position) {
                            case 0: {
                                Intent intent = new Intent(getActivity(), TextCardActivity.class);
                                break;
                            }

                        }

                    }
                })
        );

        //



        return layout;

    }

    public static ArrayList<AssignedCards> getData() {
        ArrayList<AssignedCards> data  = new ArrayList<>();

        String[] titles = {"Introduction to Twitter Compliance", "Warming up with Node.js", "Setting up Plugins in Sketch 3"};
        String[] cards = {"12 cards", "6 cards", "9 cards"};
        String[] time = {"2 minutes", "30 minutes", "1 hr 20 mins"};
        String[] due = {"19 May, 14:00 hrs", "25 May, 12:00 hrs","5 June, 05:00 hrs"};
        String[] assigner = {"Kishan Patel", "Arkit Vora", "Parth Rao"};



        for (int i = 0; i<titles.length && i<assigner.length && i<cards.length && i<time.length && i<due.length; i++ ) {

            AssignedCards current = new AssignedCards(titles[i], cards[i], time[i], due[i], assigner[i]);
            data.add(current);
        }

        return data;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String title);
    }


}
