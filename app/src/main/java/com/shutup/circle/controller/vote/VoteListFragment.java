package com.shutup.circle.controller.vote;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shutup.circle.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VoteListFragment extends Fragment {


    public VoteListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vote_list, container, false);
    }

}
