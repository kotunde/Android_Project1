package com.example.planitpoker;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Arrays;

public class VoteFragment extends Fragment
{
    private static int counter = 0;
    private int pressed_button_id;
    MyDBAdapter myDb;
    static Cursor cursor;
    static String title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        //inflate the layout for this fragment (don't forget to set the last param to false !!!)
        View retView = inflater.inflate(R.layout.vote_fragment,container,false);

        //get login name form VoteActivity
        //Bundle bundle = this.getArguments();
        //final String loginName = bundle.getString("data");
        //Log.i("Sent through args"," "+getArguments().getString("date")); //nem mukodik
        VoteActivity voteActivity= (VoteActivity) getActivity();
        final String loginName= voteActivity.getLoginName();

        //text of buttons in arraylist
        final ArrayList<String> buttonText = new ArrayList<>(Arrays.asList("0","1/2","1","2","3","5","8","13","20","40","100","?","coffee"));

        GridLayout myGridLayout =retView.findViewById(R.id.ly_grid);

        //fill grid with buttons
        for (int i=0; i<12; ++i)
        {
            Button myButton = new Button(getContext());

            myButton.setId(i);
            String btn_text = buttonText.get(i);
            myButton.setText(btn_text);
            myGridLayout.addView(myButton);
        }
        //instead of creating a space view, set the position of view in the gridlayout
        //Space space = new Space(getContext());
        //myGridLayout.addView(space);

        //the last button contains an image, has to be managed differently form other buttons
        ImageButton btn_coffe = new ImageButton(getContext());
        btn_coffe.setLayoutParams(new GridLayout.LayoutParams());
        int btn_id=12;
        //it will be placed in the 4th row, and 1st column (5,2)
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(4),GridLayout.spec(1));
        btn_coffe.setLayoutParams(params);
        btn_coffe.setId(btn_id);
        Drawable dr = getResources().getDrawable(R.drawable.coffe,null);
        Bitmap bitmap = ((BitmapDrawable)dr).getBitmap();
        Drawable d = new BitmapDrawable(getResources(),Bitmap.createScaledBitmap(bitmap,50,50,true));
        btn_coffe.setImageDrawable(d);
        myGridLayout.addView(btn_coffe);

        //on first call of the fragment get the first line of Titles table
        myDb=new MyDBAdapter(getContext());
        if (counter ==0)
        {
            cursor= myDb.getDataTitles();
            if (cursor.getCount()==0)
            {
                Log.d("MyError","Nothing found in Titles");
            }
            counter++;
            cursor.moveToFirst();

        }
        //set title for textview
        title = cursor.getString(cursor.getColumnIndex("title"));
        TextView tv_title = retView.findViewById(R.id.tv_voteFor);
        tv_title.setText(title);



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
            final Button button = retView.findViewById(i);
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

        Button btn_vote = retView.findViewById(R.id.btn_Vote);
        btn_vote.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String str_vote = buttonText.get(pressed_button_id);
                String msg = loginName + " " + title + " "+str_vote;
                Log.i("Adatbazisba: ",msg);

                //insert vote into database: loginName, title, vote
                MyDBAdapter db = new MyDBAdapter(getContext());
                db.insertVote(loginName,title,str_vote);
                VoteFragment.cursor.moveToNext();

                //start the second fragment, which has the list
                ListVoteFragment listVoteFragment = new ListVoteFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fg_placeholder,listVoteFragment,"List Fragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


        return retView;
    }


}
