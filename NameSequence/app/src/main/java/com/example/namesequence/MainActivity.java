package com.example.namesequence;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] color = {"red", "green", "blue"};
        String textToShow = "";
        Log.i("FaysalFirst", "This is first Log");
        for(int i = 0; i<color.length; i++){
            textToShow = textToShow+color[i]+", ";
        }
        Log.i("Faysal Colors", textToShow);
    }
}
