package com.example.cos;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private static final String TAG = "subjectadapter";
    private String classname;
    private ArrayList<String> msubjectnames=new ArrayList<>();

    Context mcontext;

    public SubjectAdapter(ArrayList<String> msubjectnames,String classname, Context mcontext) {
        this.msubjectnames = msubjectnames;
        this.classname=classname;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.subjectlistitem,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.subjectname.setText(msubjectnames.get(position));
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"clicked on "+msubjectnames.get(position));
                Toast.makeText(mcontext,""+msubjectnames.get(position),Toast.LENGTH_SHORT).show();
                Intent i=new Intent(mcontext,Chapters.class);
                i.putExtra("ClassName",classname);
                i.putExtra("SubjectName",msubjectnames.get(position));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return msubjectnames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView subjectname;
        RelativeLayout parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subjectname=itemView.findViewById(R.id.image_name);
            parent=itemView.findViewById(R.id.parentlayout);
        }
    }

}
