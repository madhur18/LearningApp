package com.example.cos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DatabaseReference mDatabase;
    private ArrayList<String> mclassnames=new ArrayList<String>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* for(int i=1;i<=12;i++)
        {
            mclassnames.add("class"+i);
        }

        */
    recyclerView=findViewById(R.id.rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase= FirebaseDatabase.getInstance().getReference().child("classes");

        Log.i(TAG, "onCreate: main started");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mclassnames.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                    mclassnames.add(dataSnapshot1.getValue(String.class));
                }


                RecyclerViewAdapter adapter=new RecyclerViewAdapter(mclassnames,getApplicationContext());
                recyclerView.setAdapter(adapter);


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
