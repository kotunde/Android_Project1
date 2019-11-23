package com.example.planningpokeradmin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class AnswersFragment extends Fragment
{
    private static final String ARG_ADMIN_NAME = "adminName";
    private static final String ARG_GROUP_ID = "groupId";

    private String mAdminName;
    private String mGroupName;
    private ArrayList<Answer> answers;

    public AnswersFragment()
    {
        // Required empty public constructor
    }

    public static AnswersFragment newInstance(String param1, String param2)
    {
        AnswersFragment fragment = new AnswersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ADMIN_NAME, param1);
        args.putString(ARG_GROUP_ID, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAdminName = getArguments().getString(ARG_ADMIN_NAME);
            mGroupName = getArguments().getString(ARG_GROUP_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View retView= inflater.inflate(R.layout.fragment_answers, container, false);
        initView(retView);
        return retView;
    }

    private void initView(final View view)
    {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mGroupName + " - Answers");
        answers = new ArrayList<>();

        //RECYCLERVIEW
        // get the reference of RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.rv_answerList);
        //recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //  call the constructor of RVAdapter to send the reference and data to Adapter
        AnswerAdapter answerAdapter = new AnswerAdapter(getActivity(),answers);
        recyclerView.setAdapter(answerAdapter);

    }
}
