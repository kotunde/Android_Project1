package com.example.planitpoker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdaptor extends RecyclerView.Adapter<RecyclerViewAdaptor.ViewHolder> {

    private static final String TAG ="RecyclerViewAdapter";

    List<TaskVoteResult> resultVote;
    Context mContext;

    public RecyclerViewAdaptor(Context mContext, List<TaskVoteResult>resultVote)
    {
        this.resultVote = resultVote;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_listitem, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.d(TAG,"onBindViewHolder: Called");

        holder.Name.setText(resultVote.get(position).getName());
        holder.Grade_value.setText(resultVote.get(position).getGrade().toString());
        holder.Task.setText(resultVote.get(position).getTask());
    }

    @Override
    public int getItemCount() {

        return resultVote.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView Name;
        private TextView Grade_value;
        private TextView Task;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Name= itemView.findViewById(R.id.Names_id);
            Grade_value= itemView.findViewById(R.id.Grades_id);
            Task = itemView.findViewById(R.id.Task_id);
        }

    }
}
