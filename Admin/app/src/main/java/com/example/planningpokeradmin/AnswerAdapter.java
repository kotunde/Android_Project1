package com.example.planningpokeradmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.MyViewHolder>
{
    ArrayList<Answer> mDataset;
    Context context;
    DatabaseReference dbReference;

    public AnswerAdapter(Context context, ArrayList myDataset)
    {
        this.context = context;
        this.mDataset = myDataset;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_userName;
        TextView tv_question;
        TextView tv_answer;
        public MyViewHolder(View view)
        {
            super(view);
            tv_userName = view.findViewById(R.id.tv_userName);
            tv_question = view.findViewById(R.id.tv_question);
            tv_answer = view.findViewById(R.id.tv_question);
        }
    }
    @Override
    public AnswerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {
        // set the data in items
        Answer output = mDataset.get(position);
        // implement setOnClickListener event on item view.
        holder.tv_userName.setText(output.getUserName());
        String questionId = output.getQuestionId();
        //TODO get question from database with questionId

        holder.tv_answer.setText(output.getAnswer());
    }
    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }

}