package com.example.cos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chapters extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    String path;
    private ArrayList<String> mchapternames=new ArrayList<String>();
    private static final String TAG = "Chapters";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);
        /*for(int i=1;i<=24;i++)
        {
            mchapternames.add("Chapter"+i);
        }

         */
        Intent i=getIntent();
        String classname=i.getStringExtra("ClassName");
        String subjectname=i.getStringExtra("SubjectName");
        path=classname+"/"+subjectname+"/";
        Log.i(TAG, "onCreate: classname:"+classname);
        recyclerView=findViewById(R.id.rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase= FirebaseDatabase.getInstance().getReference().child(classname).child(subjectname).child("chapters");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mchapternames.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                    mchapternames.add(dataSnapshot1.getValue(String.class));
                }


                ChapterAdapter adapter=new ChapterAdapter(mchapternames,path,getApplicationContext());
                recyclerView.setAdapter(adapter);


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
