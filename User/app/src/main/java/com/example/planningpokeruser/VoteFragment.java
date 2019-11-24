package com.example.planningpokeruser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;


public class VoteFragment extends Fragment
{
    private static final String ARG_USER_NAME = "userName";
    private static final String ARG_GROUP_NAME = "groupName";
    private static final String ARG_QUESTION_ID = "questionId";

    private String mUserName;
    private String mGroupName;
    private String mQuestionId;
    private int pressed_button_id;
    DatabaseReference dbReference;
    String questionToAnswer;
    String answer;
    Answer userAnswer;

    public VoteFragment()
    {
        // Required empty public constructor
    }

    public static VoteFragment newInstance(String param1, String param2,String param3)
    {
        VoteFragment fragment = new VoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_NAME, param1);
        args.putString(ARG_GROUP_NAME, param2);
        args.putString(ARG_QUESTION_ID, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserName = getArguments().getString(ARG_USER_NAME);
            mGroupName = getArguments().getString(ARG_GROUP_NAME);
            mQuestionId = getArguments().getString(ARG_QUESTION_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View retView= inflater.inflate(R.layout.fragment_vote, container, false);
        initView(retView);
        return retView;
    }
    public void initView(final View view)
    {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Vote");
        final ArrayList<String> buttonText = new ArrayList<>(Arrays.asList("0","1/2","1","2","3","5","8","13","20","40","100","?","coffee"));
        GridLayout myGridLayout =view.findViewById(R.id.ly_grid);

        //fill grid with buttons
        for (int i=0; i<12; ++i)
        {
            Button myButton = new Button(getContext());

            myButton.setId(i);
            String btn_text = buttonText.get(i);
            myButton.setText(btn_text);
            myGridLayout.addView(myButton);
        }

        //the last button contains an image, has to be managed differently form other buttons
        ImageButton btn_coffe = new ImageButton(getContext());
        btn_coffe.setLayoutParams(new GridLayout.LayoutParams());
        int btn_id=12;
        //it will be placed in the 4th row, and 1st column (5,2)
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(4),GridLayout.spec(1));
        btn_coffe.setLayoutParams(params);
        btn_coffe.setId(btn_id);
        Drawable dr = getResources().getDrawable(R.drawable.coffe);
        Bitmap bitmap = ((BitmapDrawable)dr).getBitmap();
        Drawable d = new BitmapDrawable(getResources(),Bitmap.createScaledBitmap(bitmap,50,50,true));
        btn_coffe.setImageDrawable(d);
        myGridLayout.addView(btn_coffe);
        //set the question in the voteFor textview
        dbReference = FirebaseDatabase.getInstance().getReference("Question").child(mQuestionId);
        dbReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                questionToAnswer = dataSnapshot.child("question").getValue().toString();
                TextView tv_voteFor = view.findViewById(R.id.tv_voteFor);
                tv_voteFor.setText(questionToAnswer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
        //whichever button was pressed last, its id will be stored in pressed_button_id
        btn_coffe.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pressed_button_id = 12;
            }
        });
        //set onClickListener for each button
        for(int i = 0; i < 12; ++i)
        {
            final Button button = view.findViewById(i);
            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    v.setSelected(true);
                    pressed_button_id=button.getId();
                }
            });
        }

        Button btn_vote = view.findViewById(R.id.btn_Vote);
        dbReference = FirebaseDatabase.getInstance().getReference("Answers");
        btn_vote.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                answer = buttonText.get(pressed_button_id);
                String msg =mUserName + " " + questionToAnswer + " "+ answer;
                Log.i("Adatbazisba: ",msg);

                //insert vote into database: loginName, title, vote
                addAnswerToDB();

                //start the second fragment, which has the list
                AnswersFragment answersFragment = new AnswersFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fg_placeholder,answersFragment.newInstance(mUserName,mGroupName));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

    }
    public void addAnswerToDB()
    {
        String id = dbReference.push().getKey();
        userAnswer = new Answer(id,mUserName,mGroupName,mQuestionId,answer);
        dbReference.child(id).setValue(userAnswer);
        Toast.makeText(getContext(), "Answer added", Toast.LENGTH_LONG).show();
    }


}
