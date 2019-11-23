package com.example.planitpoker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.Arrays;


public class VoteActivity extends AppCompatActivity
{
    String loginName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        Intent intent = getIntent();
        loginName = intent.getStringExtra("Login_name");
        Log.i("Sent through intent: ",loginName);//mukodik
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //does not work!!!
        //ft.replace(R.id.fg_placeholder,new VoteFragment());
        //ft.commit();

        createTitles();

        VoteFragment voteFragment = new VoteFragment();
        //send loginName to the fragment
        Bundle data = new Bundle();
        data.putString("name",loginName);
        voteFragment.setArguments(data);
        ft.add(R.id.fg_placeholder,voteFragment);
        //ft.add(R.id.fg_placeholder,listVoteFragment);
        ft.commit();
    }


    public String getLoginName()
    {
        return loginName;
    }

    public void createTitles()
    {
        MyDBAdapter db = new MyDBAdapter(this);
        ArrayList<String> titleList = new ArrayList<>(Arrays.asList("Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7","Title 8","Title 9","Title 10"));
        for (int i=0; i<titleList.size();++i)
        {
            db.insertTitle(titleList.get(i));
        }
    }
}
