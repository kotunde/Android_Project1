package com.example.planningpokeradmin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartFragment extends Fragment
{
    boolean inputError;
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
        final EditText et_adminName = retView.findViewById(R.id.et_adminName);
        final EditText et_groupName = retView.findViewById(R.id.et_groupName);
        Button btn_createGroup = retView.findViewById(R.id.btn_createGroup);

        //if Create Group button is pushed
        btn_createGroup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //send the two input string to the next fragment
                String str_adminName = et_adminName.getText().toString();
                String str_groupName = et_groupName.getText().toString();
                inputError = false;
                //check if fields are filled correctly
                if (TextUtils.isEmpty(str_adminName))
                {
                    et_adminName.setError("Admin name required");
                    inputError = true;
                }
                if (TextUtils.isEmpty(str_groupName))
                {
                    et_groupName.setError("Groupname required");
                    inputError = true;
                }
                if (!inputError)
                {
                    QuestionsFragment questionsFragment = new QuestionsFragment();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fg_placeholder,questionsFragment.newInstance(str_adminName,str_groupName));
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Log in");
        return retView;
    }

}
