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




public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {


    private static final String TAG = "chapterdapter";
    private String path;
    private ArrayList<String> mchapternames=new ArrayList<>();

    Context mcontext;

    public ChapterAdapter(ArrayList<String> mchapternames,String path, Context mcontext) {
        this.mchapternames = mchapternames;
        this.path=path;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chapterlistitem,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.classname.setText(mchapternames.get(position));
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG,"clicked on "+mchapternames.get(position));
                Toast.makeText(mcontext,""+mchapternames.get(position),Toast.LENGTH_SHORT).show();
                Intent i=new Intent(mcontext,StudyPane.class);
                i.putExtra("Path",path+mchapternames.get(position)+"/");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Toast.makeText(mcontext,"Path:"+path,Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onClick: Path:"+path);
                mcontext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mchapternames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView classname;
        RelativeLayout parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            classname=itemView.findViewById(R.id.image_name);
            parent=itemView.findViewById(R.id.parentlayout);
        }
    }

}
