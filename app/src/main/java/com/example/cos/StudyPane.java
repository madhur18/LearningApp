package com.example.cos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StudyPane extends AppCompatActivity {

    String path;
    TextView Text,Video,Quiz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_pane);
        Text=findViewById(R.id.TextNotes);
        Video=findViewById(R.id.Videos);
        Quiz=findViewById(R.id.Quiz);
        Intent i=getIntent();
        path=i.getStringExtra("Path");
        Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),TextViewer.class);
                i.putExtra("Path",path);
                startActivity(i);
            }
        });
        Video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),VideoPlayer.class);
                i.putExtra("Path",path);
                startActivity(i);
            }
        });
        Quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),QuizTime.class);
                i.putExtra("Path",path);
                startActivity(i);
            }
        });
    }
}
