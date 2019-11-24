package com.example.planningpokeruser;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class QuestionsFragment extends Fragment
{
    private static final String ARG_USER_NAME = "adminName";
    private static final String ARG_GROUP_ID = "groupId";

    private String mUserName;
    private String mGroupName;
    DatabaseReference dbReference;
    ArrayList<Question> questionList;
    QuestionAdapter questionAdapter;
    RecyclerView recyclerView;
    ArrayList<String> mKeys = new ArrayList<String>();

    public QuestionsFragment()
    {
        // Required empty public constructor
    }

    public static QuestionsFragment newInstance(String param1, String param2)
    {
        QuestionsFragment fragment = new QuestionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_NAME, param1);
        args.putString(ARG_GROUP_ID, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //get parameter sent from previous fragment
        if (getArguments() != null)
        {
            mUserName = getArguments().getString(ARG_USER_NAME);
            mGroupName = getArguments().getString(ARG_GROUP_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View retView = inflater.inflate(R.layout.fragment_questions, container, false);
        initView(retView);
        recyclerView.setAdapter(questionAdapter);
        return retView;
    }

    private void initView(final View view)
    {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mGroupName+" - Questions");
        questionList = new ArrayList<>();

        dbReference = FirebaseDatabase.getInstance().getReference("Question");

        //RECYCLERVIEW
        // get the reference of RecyclerView
        recyclerView = view.findViewById(R.id.rv_questionList);
        //recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //  call the constructor of QuesetionAdapter to send the reference and data to Adapter
        final QuestionAdapter.RecyclerViewClickListener listener = new QuestionAdapter.RecyclerViewClickListener()
        {
            @Override
            public void onClick(String questionId)
            {
                //Toast.makeText(getContext(), "Votefragment called", Toast.LENGTH_LONG).show();
                VoteFragment answersFragment = new VoteFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fg_placeholder,answersFragment.newInstance(mUserName,mGroupName,questionId));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        };
        questionAdapter = new QuestionAdapter(getActivity(),questionList,listener);
        recyclerView.setAdapter(questionAdapter);

        //get data from database
        dbReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                questionList.clear();
                for (DataSnapshot questionSnapshot: dataSnapshot.getChildren())
                {
                    Question question = questionSnapshot.getValue(Question.class);
                    if(question.getGroupName().equals(mGroupName))
                    {
                        questionList.add(question);
                        String key = questionSnapshot.getKey();
                        mKeys.add(key);
                    }

                }
                questionAdapter = new QuestionAdapter(getActivity(),questionList,listener);
                recyclerView.setAdapter(questionAdapter);
                //questionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        dbReference.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                Question newQuesetion=dataSnapshot.getValue(Question.class);
                //display question only if it belongs to this group
                if (newQuesetion.getGroupName().equals(mGroupName))
                {
                    questionList.add(newQuesetion);
                    String key = dataSnapshot.getKey();
                    mKeys.add(key);
                    //questionAdapter = new QuestionAdapter(getActivity(),questionList,listener);
                    //RecyclerView recyclerView = view.findViewById(R.id.rv_questionList);
                    //recyclerView.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(questionAdapter);
                    //questionAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                Question question = dataSnapshot.getValue(Question.class);
                if(question.getGroupName().equals(mGroupName))
                {
                    String key =  dataSnapshot.getKey();
                    int index = mKeys.indexOf(key);
                    questionList.set(index,question);
                }
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(questionAdapter);
                //questionAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(getContext(), "Some error ocurred", Toast.LENGTH_LONG).show();
            }
        });
    }



}