//Listener/Callback pattern:  https://dzone.com/articles/click-listener-for-recyclerview-adapter
package com.example.planningpokeradmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder>
{
    private ArrayList<Question> mDataset;
    Context context;
    DatabaseReference dbReference;
    ArrayList<Question> questionList;
    int nrOfActiveQuestions;
    private RecyclerViewClickListener mListener;

    public interface RecyclerViewClickListener
    {
        void onClick(String questionId);
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public RVAdapter(Context context,ArrayList myDataset,RecyclerViewClickListener listener)
    {
        this.context = context;
        this.mDataset = myDataset;
        questionList = myDataset;
        mListener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tv_question;
        public Switch switch_activation;
        public MyViewHolder(View view) {
            super(view);
            tv_question =view.findViewById(R.id.tv_question);
            switch_activation =view.findViewById(R.id.switch_activation);
        }
    }
    // Create new views (invoked by the layout manager)
    @Override
    public RVAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view/ infalte item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_view, parent, false);
        //pass the view to View Holder
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        // set the data in items
        final Question output = mDataset.get(position);
        String str_question= output.getQuestion();
        holder.tv_question.setText(str_question);
        if (output.getState().equals("active"))
        {
            holder.switch_activation.setChecked(true);
        }
        else
        {
            holder.switch_activation.setChecked(false);
        }

        //switch onclick
        holder.switch_activation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    //change state to active in firabase database
                    updateQuestion(output.getQuestionId(),output.getAdminName(),output.getGroupName(),output.getQuestion(),"active");
                    //we have to check wether it can be activated >>> only one question can be active at one time!
                    checkActiveQuestions(output,holder.switch_activation);
                }
                else
                {
                    //change state to inactive in firabase database
                    updateQuestion(output.getQuestionId(),output.getAdminName(),output.getGroupName(),output.getQuestion(),"inactive");
                    //send a toast about it
                    Toast.makeText(context, "Question is inactive", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // implement setOnClickListener event on item view. {...}
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //start a new fragment with recyclerview for seeing answers for the active question
                String questionId = output.getQuestionId();
                mListener.onClick(output.getQuestionId());
            }
        });

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }

    //get all of the question data from database, and count how many of the questions are active at that time
    public int checkActiveQuestions(final Question actualQuestion, final Switch switch_activation)
    {
        questionList = new ArrayList<>();
        dbReference = FirebaseDatabase.getInstance().getReference("Question");
        dbReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                questionList.clear();
                for (DataSnapshot questionSnapshot: dataSnapshot.getChildren())
                {
                    Question question = questionSnapshot.getValue(Question.class);
                    questionList.add(question);
                    //count number of active questions
                    nrOfActiveQuestions = 0;
                    Log.d("DEBUG","Tomb merete: " + Integer.toString(questionList.size()));
                    for (Question q:questionList)
                    {
                        Log.d("BEBUG  question state: ", q.getState());
                        if (q.getState().equals("active"))
                        {
                            ++nrOfActiveQuestions;
                        }
                    }
                }
                Log.d("DEBUG","nrOfActiveConnections: " + nrOfActiveQuestions);
                if (nrOfActiveQuestions==2)
                {
                    Log.d("DEBUG","There are two active questions");
                    Toast.makeText(context, "Only one question can be active", Toast.LENGTH_SHORT).show();
                    //set switch to unchecked
                    switch_activation.setChecked(false);
                    updateQuestion(actualQuestion.getQuestionId(),actualQuestion.getAdminName(),actualQuestion.getGroupName(),actualQuestion.getQuestion(),"inactive");
                }
                else {
                    //send a toast about it
                    Toast.makeText(context, "Question is active", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        return nrOfActiveQuestions;
    }

    public boolean updateQuestion(String questionId,String adminName, String groupName, String question, String state)
    {
        dbReference = FirebaseDatabase.getInstance().getReference("Question").child(questionId);
        Question newQuestion = new Question(questionId,adminName,groupName,question);
        newQuestion.setState(state);
        dbReference.setValue(newQuestion);
        //Toast.makeText(context, "Question updated", Toast.LENGTH_LONG).show();
        return true;
    }
}