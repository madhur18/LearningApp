package com.example.cos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TextViewer extends AppCompatActivity {

    public String vu;
    private static final String TAG = "TextViewer";
    TextView text;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_viewer);
        text=findViewById(R.id.textView);
        Intent i=getIntent();
        String path=i.getStringExtra("Path");
        Log.i(TAG, "onCreate: Path:" + path);
        Toast.makeText(getApplicationContext(),"Path:"+path,Toast.LENGTH_SHORT).show();
        mDatabase= FirebaseDatabase.getInstance().getReference().child(path).child("content/");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                text.setText(dataSnapshot.getValue(String.class));

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        vu=text.getText().toString().trim();
        Log.i(TAG, "onCreate: vu:"+vu);
        Toast.makeText(getApplicationContext(),"vu:"+vu,Toast.LENGTH_SHORT).show();
    }
}
