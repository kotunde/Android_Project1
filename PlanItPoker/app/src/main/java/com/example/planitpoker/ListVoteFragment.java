package com.example.planitpoker;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListVoteFragment extends Fragment {

    View retView;
    private RecyclerView recyclerView;
    private List<TaskVoteResult> Names;
    MyDBAdapter myDb;
    Cursor cursor;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        retView = inflater.inflate(R.layout.listvote_fragment,container,false);

        recyclerView = (RecyclerView) retView.findViewById(R.id.Recycler_view);
        RecyclerViewAdaptor adapter = new RecyclerViewAdaptor(getContext(),Names);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);



        return retView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Names = new ArrayList<>();


        //VoteActivity voteActivity= (VoteActivity) getActivity();
        //final String loginName= voteActivity.getLoginName();

        myDb=new MyDBAdapter(getContext());
        cursor= myDb.getDataVotes();
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){

            String loginName = cursor.getString(cursor.getColumnIndex("name"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String vote = cursor.getString(cursor.getColumnIndex("vote"));
            Names.add(new TaskVoteResult(loginName,vote,title));
            cursor.moveToNext();
            }
    }
}
