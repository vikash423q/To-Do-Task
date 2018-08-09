package com.vikash.todotask;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;


public class SecondFragment extends Fragment {

    static ArrayList<Task.SubTask> subTasks;
    RecyclerView recyclerView;
    SubTaskAdapter subTaskAdapter;
    static int index;
    View view;


    public SecondFragment() {
        // Required empty public constructor

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view1, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view=view1;
        refreshFragment();
        Task task=MainFragment.taskList.get(index);

        ToggleButton toggleButton=view.findViewById(R.id.completed1);
        toggleButton.setText(null);
        toggleButton.setTextOn(null);
        toggleButton.setTextOff(null);

        TextView title=view.findViewById(R.id.titletextView);
        title.setText(task.getTitle());

        TextView completionRate=view.findViewById(R.id.completionview);
        completionRate.setText(task.getCompletionRate()+"%");

        CardView cardView=view.findViewById(R.id.cardView3);
        cardView.setCardBackgroundColor(ColorGenerator.colorForPosition(index,getActivity()));
        RelativeLayout layout=view.findViewById(R.id.relativeLayout3);
        int color=ColorGenerator.colorFader(ColorGenerator.colorForPosition(index,getActivity()));
        layout.setBackgroundColor(color);



        recyclerView=(RecyclerView)view.findViewById(R.id.recycler2);
        recyclerView.setBackgroundColor(color);

        subTasks=task.subTasks;
        if(subTasks==null)
            subTasks=new ArrayList<Task.SubTask>();

        subTasks.add(new Task.SubTask("8 June, 2018","5 July, 2018","Take out the Garbage",false));
        subTasks.add(new Task.SubTask("9 June, 2018","7 July, 2018","Clean the Kitchen floor",false));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        subTaskAdapter=new SubTaskAdapter(subTasks,getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(subTaskAdapter);

        ImageButton button=view.findViewById(R.id.imageButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask(v);
            }
        });


    }

    public void addTask(View view){

        LayoutInflater li=LayoutInflater.from(getContext());
        View view1=li.inflate(R.layout.subtask_input,null);

        AlertDialog.Builder alertdialog=new AlertDialog.Builder(getContext());
        alertdialog.setView(view1);

        final EditText editText=(EditText)view1.findViewById(R.id.editText1);
        final EditText editText1=(EditText)view1.findViewById(R.id.editText3) ;
        editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        editText1.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);


        alertdialog
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //save the title here.
                        String start="Start : ",end="End : ";
                        subTasks.add(new Task.SubTask(start+"today",end+editText1.getText(),editText.getText().toString(),false));
                        subTaskAdapter.notifyDataSetChanged();

                    }})
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        alertdialog.show();


    }

    @Override
    public void onResume() {
        super.onResume();
        refreshFragment();
    }

    public void refreshFragment(){
        Task task=MainFragment.taskList.get(index);

        ToggleButton toggleButton=view.findViewById(R.id.completed1);
        toggleButton.setText(null);
        toggleButton.setTextOn(null);
        toggleButton.setTextOff(null);

        TextView title=view.findViewById(R.id.titletextView);
        title.setText(task.getTitle());

        TextView completionRate=view.findViewById(R.id.completionview);
        completionRate.setText(task.getCompletionRate()+"%");

        CardView cardView=view.findViewById(R.id.cardView3);
        cardView.setCardBackgroundColor(ColorGenerator.colorForPosition(index,getActivity()));
        RelativeLayout layout=view.findViewById(R.id.relativeLayout3);
        int color=ColorGenerator.colorFader(ColorGenerator.colorForPosition(index,getActivity()));
        layout.setBackgroundColor(color);



        recyclerView=(RecyclerView)view.findViewById(R.id.recycler2);
        recyclerView.setBackgroundColor(color);
        LinearLayout layout1=view.findViewById(R.id.backLayout);
        layout1.setBackgroundColor(color);
    }
}
