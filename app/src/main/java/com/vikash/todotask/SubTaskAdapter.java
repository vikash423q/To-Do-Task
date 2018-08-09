package com.vikash.todotask;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class SubTaskAdapter extends RecyclerView.Adapter<SubTaskAdapter.SubTaskViewHolder> {

    ArrayList<Task.SubTask> subTasks;
    Context context;

    public SubTaskAdapter(ArrayList<Task.SubTask> subTasks, Context context) {
        this.subTasks = subTasks;
        this.context = context;
    }

    @NonNull
    @Override
    public SubTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.subtask,null);

        return new SubTaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubTaskViewHolder holder, int position) {
        Task.SubTask subTask=subTasks.get(position);
        holder.subtask.setText(subTask.getTaskTitle());
        holder.startDate.setText(subTask.getStartDate());
        holder.endDate.setText(subTask.getEndDate());
    }

    @Override
    public int getItemCount() {
        return subTasks.size();
    }

    public class SubTaskViewHolder extends RecyclerView.ViewHolder{

        TextView startDate,endDate,subtask;
        ImageButton button;
        public SubTaskViewHolder(View itemView) {
            super(itemView);
            startDate=itemView.findViewById(R.id.textView11);
            endDate=itemView.findViewById(R.id.textView10);
            subtask=itemView.findViewById(R.id.textView12);
            button=itemView.findViewById(R.id.imageButton3);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
