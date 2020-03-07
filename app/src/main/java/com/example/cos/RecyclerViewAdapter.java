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




public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "recyclerviewadapter";

    private ArrayList<String> mclassnames=new ArrayList<>();

    Context mcontext;

    public RecyclerViewAdapter(ArrayList<String> mclassnames, Context mcontext) {
        this.mclassnames = mclassnames;

        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.classlistitem,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.classname.setText(mclassnames.get(position));
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG,"clicked on "+mclassnames.get(position));
                Toast.makeText(mcontext,""+mclassnames.get(position),Toast.LENGTH_SHORT).show();
                Intent i=new Intent(mcontext,Subjects.class);
                i.putExtra("ClassName",mclassnames.get(position));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mclassnames.size();
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
