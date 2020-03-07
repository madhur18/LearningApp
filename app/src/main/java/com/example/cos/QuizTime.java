package com.example.cos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cos.Object.Question;
import com.example.cos.layout.FitDoughnut;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class QuizTime extends AppCompatActivity {

    String[] questions;
    String lessontitle;
   // LessonsLDH lessonsLDH;
    int curq;
    int lessonid;
    public static final String Q_SEP = "<<-->>";
    public static final String A_SEP = "<->";
    boolean[] results;

    TextView lessontitleTv;
    TextView questiontitleTv;
    Button[] answersBt;
    ImageView[] resqIv;
    RelativeLayout emptyview;
    ImageView closeBt;
    private static final String TAG = "QuizTime";
    int correctans;
    String path;
    String path1;
    String ques="question1";
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_time);


        Intent intent = getIntent();
        path=intent.getStringExtra("Path");

        //lessonid = intent.getIntExtra("lessonid", 0);

        //lessonsLDH = LessonsLDH.getInstance(this);
        //String q = lessonsLDH.getQuestions(lessonid);
        //lessontitle = lessonsLDH.getLessonTitle(lessonid);
        path1=path.substring(0,path.lastIndexOf("/"));
        lessontitle=path1.substring(path1.lastIndexOf("/")+1);

        curq = 0;

        lessontitleTv = findViewById(R.id.lesson_title);
        questiontitleTv = findViewById(R.id.question_title);

        answersBt = new Button[]{findViewById(R.id.answer1), findViewById(R.id.answer2), findViewById(R.id.answer3), findViewById(R.id.answer4)};
        resqIv = new ImageView[]{findViewById(R.id.resq0), findViewById(R.id.resq1), findViewById(R.id.resq2),
                findViewById(R.id.resq3), findViewById(R.id.resq4),findViewById(R.id.resq5),findViewById(R.id.resq6),
                findViewById(R.id.resq7),findViewById(R.id.resq8),findViewById(R.id.resq9)};
        emptyview = findViewById(R.id.emptyview);
        emptyview.setVisibility(View.GONE);

        for(int i=0;i<4;i++)
            answersBt[i].setVisibility(View.GONE);
        lessontitleTv.setText(lessontitle);

        results = new boolean[]{false, false, false, false, false, false, false, false, false, false};

        emptyview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestion();
            }
        });

        setCurrentQuestion();

        closeBt = findViewById(R.id.closebt);
        closeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizTime.this, StudyPane.class);
                intent.putExtra("Path",path);
                finish();
                startActivity(intent);
            }
        });
    }

    private void nextQuestion(){
        if(curq == 9){
            int tot = 0;
            for(int i = 0; i<10; i++){
                if(results[i]) {
                    tot++;
                }
            }

            //int ch = 5*lessonsLDH.updateResult(lessonid, tot);

            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.result_dialog, null);

            final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(QuizTime.this).setView(dialogView).show();

            TextView tvCompl = dialogView.findViewById(R.id.tvcompl);
            TextView tvScore = dialogView.findViewById(R.id.tvscore);
            TextView tvResult = dialogView.findViewById(R.id.tvresult);

            if(tot == 10){
                tvCompl.setText("Perfect!");
            } else if (tot>=7){
                tvCompl.setText("Well Done!");
            } else {
                tvCompl.setText("Try Again!");
                tvCompl.setTextColor(getResources().getColor(R.color.wrong));
                TextView tvExtra = dialogView.findViewById(R.id.tvextra);
                tvExtra.setVisibility(View.VISIBLE);
            }

            String res = "Score:\n" + Integer.toString(tot) + "/10";
            tvResult.setText(res);

            String change = path1.substring(path1.lastIndexOf("/")+1);
            tvScore.setText(change);

            // level

            //Level level = lessonsLDH.getLevel();
            FitDoughnut doughnut = (FitDoughnut) dialogView.findViewById(R.id.doughnuttot);
            doughnut.animateSetPercent((float) tot*10);
            TextView tvperctot = dialogView.findViewById(R.id.tvpercentage);
            String p = Integer.toString(tot*10)+ "%";
            tvperctot.setText(p);
            TextView tvLev = dialogView.findViewById(R.id.tvlevel);
            tvLev.setText("User Level.");
            TextView tvProg = dialogView.findViewById(R.id.tvprogress);
            String prog = "User Progress";
            tvProg.setText(prog);

            Button btTryAgain = dialogView.findViewById(R.id.btTryAgain);
            btTryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(QuizTime.this, QuizTime.class);
                    intent.putExtra("Path",path);
                    finish();
                    startActivity(intent);
                }
            });

            Button btHome = dialogView.findViewById(R.id.btHome);
            btHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    Intent intent = new Intent(QuizTime.this, StudyPane.class);
                    intent.putExtra("Path",path);
                    finish();
                    startActivity(intent);

                }
            });

        } else {
            emptyview.setVisibility(View.GONE);
            curq++;
            ques="question"+(curq+1);
            setCurrentQuestion();
        }
    }

    private void setCurrentQuestion(){


        mDatabase= FirebaseDatabase.getInstance().getReference().child(path).child("quiz").child(ques);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                for(int i=0;i<4;i++)
                    answersBt[i].setVisibility(View.VISIBLE);
                Question mquestion=dataSnapshot.getValue(Question.class);
                questiontitleTv.setText(mquestion.getQuestion());
                Log.i(TAG, "onDataChange: answer:"+mquestion.getAnswer());
                Log.i(TAG, "onDataChange: option1:"+mquestion.getOption1());
                Log.i(TAG, "onDataChange: option2:"+mquestion.getOption2());
                Log.i(TAG, "onDataChange: option3:"+mquestion.getOption3());
                Log.i(TAG, "onDataChange: option4:"+mquestion.getOption4());
                if(mquestion.getOption3()==null)
                {
                    answersBt[0].setBackground(getResources().getDrawable(R.drawable.answerbox));
                    answersBt[1].setBackground(getResources().getDrawable(R.drawable.answerbox));
                    answersBt[0].setText(mquestion.getOption1());
                    answersBt[1].setText(mquestion.getOption2());
                    answersBt[2].setVisibility(View.GONE);
                    answersBt[3].setVisibility(View.GONE);

                    if(mquestion.getAnswer().equals(mquestion.getOption1())){
                        correctans = 0;
                    } else {
                        correctans = 1;
                    }
                }
                else
                {
                    if(mquestion.getAnswer().equals(mquestion.getOption1())){
                        correctans = 0;
                    }
                    else if(mquestion.getAnswer().equals(mquestion.getOption2()))
                    {
                        correctans = 1;
                    }
                    else if(mquestion.getAnswer().equals(mquestion.getOption3()))
                    {
                        correctans = 2;
                    }
                    else {
                        correctans = 3;
                    }
                    answersBt[0].setVisibility(View.VISIBLE);
                    answersBt[0].setBackground(getResources().getDrawable(R.drawable.answerbox));
                    answersBt[0].setText(mquestion.getOption1());
                    answersBt[1].setVisibility(View.VISIBLE);
                    answersBt[1].setBackground(getResources().getDrawable(R.drawable.answerbox));
                    answersBt[1].setText(mquestion.getOption2());
                    answersBt[2].setVisibility(View.VISIBLE);
                    answersBt[2].setBackground(getResources().getDrawable(R.drawable.answerbox));
                    answersBt[2].setText(mquestion.getOption3());
                    answersBt[3].setVisibility(View.VISIBLE);
                    answersBt[3].setBackground(getResources().getDrawable(R.drawable.answerbox));
                    answersBt[3].setText(mquestion.getOption4());

                }
                answersBt[correctans].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        answersBt[correctans].setBackground(getResources().getDrawable(R.drawable.answerboxcorrect));
                        results[curq] = true;
                        resqIv[curq].setBackground(getResources().getDrawable(R.drawable.qcorrect));
                        emptyview.setVisibility(View.VISIBLE);
                    }
                });
                for(int i = 0; i<4; i++){
                    if(i != correctans){
                        final int finalI = i;
                        answersBt[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                answersBt[correctans].setBackground(getResources().getDrawable(R.drawable.answerboxcorrect));
                                answersBt[finalI].setBackground(getResources().getDrawable(R.drawable.answerboxwrong));
                                resqIv[curq].setBackground(getResources().getDrawable(R.drawable.qwrong));
                                emptyview.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
                }
                else
                {
                    questiontitleTv.setText("No Quiz available right now!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
