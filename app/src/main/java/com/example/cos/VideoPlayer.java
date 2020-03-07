package com.example.cos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoPlayer extends YouTubeBaseActivity {

    private final static String expression = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
    YouTubePlayerView youTubePlayerView;
    Button button;
    String url;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    private DatabaseReference mDatabase;
    String videourl;
    private static final String TAG = "VideoPlayer";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        youTubePlayerView=findViewById(R.id.youtubeplayer);

        Intent i=getIntent();
        String path=i.getStringExtra("Path");
        Log.i(TAG, "onCreate: Path:" + path);
        Toast.makeText(getApplicationContext(),"Path:"+path,Toast.LENGTH_SHORT).show();
        mDatabase= FirebaseDatabase.getInstance().getReference().child(path).child("video_url/");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "onDataChange: data chabdas");
                videourl=dataSnapshot.getValue(String.class);
                Toast.makeText(getApplicationContext(),"videourl:"+videourl,Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onCreate: videourl:"+videourl);
                if(videourl!=null)
                    url=getVideoId(videourl);
                onInitializedListener=new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.loadVideo(url);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                };
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //videourl="https://www.youtube.com/watch?v=bSMZknDI6bg";

        button=findViewById(R.id.button);

        Log.i(TAG, "onCreate: url:"+url);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youTubePlayerView.initialize(YoutubeConfig.getAPI_Key(),onInitializedListener);
            }
        });

    }

    public static String getVideoId(String videoUrl) {
        if (videoUrl == null || videoUrl.trim().length() <= 0){
            return null;
        }
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(videoUrl);
        try {
            if (matcher.find())
                return matcher.group();
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
