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

import javax.security.auth.login.LoginException;

public class Subjects extends AppCompatActivity {

    private static final String TAG = "Subjects";
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private ArrayList<String> msubjectnames=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
       /* for(int i=1;i<=6;i++)
        {
            msubjectnames.add("Subject"+i);
        }

        */
        Intent i=getIntent();
        final String classname=i.getStringExtra("ClassName");
        Log.i(TAG, "onCreate: classname:"+classname);
        recyclerView=findViewById(R.id.rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase= FirebaseDatabase.getInstance().getReference().child(classname).child("subjects");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                msubjectnames.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                    msubjectnames.add(dataSnapshot1.getValue(String.class));
                }


                SubjectAdapter adapter=new SubjectAdapter(msubjectnames,classname,getApplicationContext());
                recyclerView.setAdapter(adapter);


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
