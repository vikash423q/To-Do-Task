package com.vikash.todotask;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;


public class MainFragment extends Fragment {

    static ArrayList<Task> taskList;
    RecyclerView recyclerView;
    TaskAdapter taskAdapter;

    public MainFragment() {
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
        final View view= inflater.inflate(R.layout.fragment_main, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton button=view.findViewById(R.id.imageButton2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask(v);
            }
        });

        taskList=new ArrayList<Task>();

        taskList.add(new Task("Homeworks",false,22));
        taskList.add(new Task("Groceries",false,32));
        taskList.add(new Task("Girlfriend Birthday",false,50));
        taskList.add(new Task("Party Planning",false,40));
        taskList.add(new Task("Puja Decoration",false,45));


        recyclerView=(RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        taskAdapter=new TaskAdapter(taskList,getActivity());
        recyclerView.setAdapter(taskAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                //   Toast.makeText(getActivity(), position+ " is selected successfully", Toast.LENGTH_SHORT).show();

                //handle click event.
                MainActivity.switchFragment(view,position);
                Log.i("recycler","OnClickListener is being called");


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    public void addTask(View view){

        LayoutInflater li=LayoutInflater.from(getContext());
        View view1=li.inflate(R.layout.custom_alert,null);

        AlertDialog.Builder alertdialog=new AlertDialog.Builder(getContext());
        alertdialog.setView(view1);

        final EditText editText=(EditText)view1.findViewById(R.id.editText);
        editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);


        alertdialog
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //save the title here.
                        taskList.add(new Task(editText.getText().toString(),false,0));
                        taskAdapter.notifyDataSetChanged();
                        Log.i("task",taskList.get(0).title);

                    }})
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        alertdialog.show();


    }


}
