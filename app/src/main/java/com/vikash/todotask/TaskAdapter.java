package com.vikash.todotask;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class TaskAdapter extends  RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{

    public TaskAdapter(ArrayList<Task> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
    }

    ArrayList<Task> taskList;
    Context context;


    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view= inflater.inflate(R.layout.task,null);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskViewHolder holder, final int position) {

            final Task task=taskList.get(position);
            holder.title.setText(task.getTitle());
            holder.completionRate.setText(String.valueOf(task.getCompletionRate())+"%");
            holder.completed.setText(null);
            holder.completed.setTextOn(null);
            holder.completed.setTextOff(null);
            holder.completed.setChecked(task.isCompleted());

            if(holder.completed.isChecked())
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.grey));
            else
                holder.cardView.setCardBackgroundColor(ColorGenerator.colorForPosition(position,context));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


    class TaskViewHolder extends RecyclerView.ViewHolder{

        ToggleButton completed;
        TextView title,completionRate;
        CardView cardView;

        public TaskViewHolder(View itemView) {
            super(itemView);

            completed=itemView.findViewById(R.id.completed);
            title=itemView.findViewById(R.id.textView);
            completionRate=itemView.findViewById(R.id.textView2);
            cardView=(CardView)itemView.findViewById(R.id.cardView);


        }

    }
}
