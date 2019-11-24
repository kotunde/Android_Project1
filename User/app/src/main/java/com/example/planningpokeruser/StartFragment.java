package com.example.planningpokeruser;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class StartFragment extends Fragment
{


    public StartFragment()
    {
        // Required empty public constructor
    }

    public static StartFragment newInstance(String param1, String param2)
    {
        StartFragment fragment = new StartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //get parameter sent from activity
        if (getArguments() != null)
        {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View retView =inflater.inflate(R.layout.fragment_start, container, false);
        final EditText et_userName = retView.findViewById(R.id.et_userName);
        final EditText et_groupName = retView.findViewById(R.id.et_groupName);
        Button btn_joinGroup = retView.findViewById(R.id.btn_joinGroup);

        //if Join Group button is pushed
        btn_joinGroup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Log.i("Debug","Button pushed");
                //send the two input string to the next fragment
                String str_userName = et_userName.getText().toString();
                String str_groupName = et_groupName.getText().toString();
                QuestionsFragment questionsFragment = new QuestionsFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fg_placeholder,questionsFragment.newInstance(str_userName,str_groupName));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Log in");
        return retView;
    }
}
