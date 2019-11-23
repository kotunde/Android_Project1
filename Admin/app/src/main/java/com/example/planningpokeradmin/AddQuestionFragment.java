package com.example.planningpokeradmin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.TimePickerDialog;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class AddQuestionFragment extends DialogFragment
{

    EditText et_question;
    Button btn_addQuestion,btn_cancel;
    Question newQuestion;
    DatabaseReference dbReference;
    private static final String ARG_ADMIN_NAME = "adminName";
    private static final String ARG_GROUP_ID = "groupId";

    private String mAdminName;
    private String mGroupName;

    public AddQuestionFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddQuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddQuestionFragment newInstance(String param1, String param2)
    {
        AddQuestionFragment fragment = new AddQuestionFragment();
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
        return inflater.inflate(R.layout.fragment_add_question, container, false);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        getDialog().setTitle("New question");
        et_question = view.findViewById(R.id.et_question);
        btn_addQuestion = view.findViewById(R.id.btn_addQuestion);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        dbReference = FirebaseDatabase.getInstance().getReference("Question");

        //addquestion button saves data to firebase, then returns to previous fragment
        btn_addQuestion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addQuestionToDB();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getDialog().dismiss();
            }
        });
    }

    public void addQuestionToDB()
    {
        //get question from edittext
        String str_question = et_question.getText().toString();
        if (TextUtils.isEmpty(str_question))
        {
            et_question.setError("Quesetion required or Cancel");
            return;
        }else {
            String id = dbReference.push().getKey();
            newQuestion = new Question(id, mAdminName, mGroupName, str_question);
            //push into db
            dbReference.child(id).setValue(newQuestion);
            Toast.makeText(getContext(), "Question added", Toast.LENGTH_LONG).show();
            //remove dialog
            dismiss();
        }
    }
}
