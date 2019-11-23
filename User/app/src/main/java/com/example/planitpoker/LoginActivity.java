package com.example.planitpoker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
    }

    // ezzel terveztem elinditani a kovetkezo activityt meg itt van a nev amit megkapunk
    public void Planning(View view){

        TextView Name_id = findViewById(R.id.Name_ET_id);

        String Name = Name_id.getText().toString();

        Intent intent = new Intent(this, VoteActivity.class);
        intent.putExtra("Login_name",Name);
        startActivity(intent);

    }
}
