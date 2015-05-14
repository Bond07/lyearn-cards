package com.example.nirav.lyearn;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private HomeCardAdapter mCardAdapter;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_home, container, false);


        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(MainApplication.getAppContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);


        mRecyclerView = (RecyclerView) layout.findViewById(R.id.card_home);

        mCardAdapter = new HomeCardAdapter(getData());

        mRecyclerView.setAdapter(mCardAdapter);

        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        //
//        if (mListener != null) {
//            mListener.onFragmentInteraction("Home Cards");
//        }


        return layout;

    }

    public static ArrayList<HomeCards> getData() {
        ArrayList<HomeCards> data  = new ArrayList<>();

        String[] titles = {"Test1", "Test2", "fdsafasd", "fdsfas","fdsfas", "Test2", "fdsafasd", "fdsfas","fdsfas"};
        String[] description = {"24133-24133/com.example.nirav.lyearn I/ViewUtils", "No adapter attached; skipping layout", "fsdfasfdsa", "fdsfas", "fdsfsadfsadfsadfasdfasd", "No adapter attached; skipping layout", "fsdfasfdsa", "fdsfas", "fdsfsadfsadfsadfasdfasd"};


        for (int i = 0; i<titles.length && i<description.length; i++ ) {

            HomeCards current = new HomeCards(titles[i], description[i]);
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
