package com.vikash.todotask;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;


public class MainFragment extends Fragment {

    static ArrayList<Task> taskList;
    static ArrayList<Task>completedTaskList;
    RecyclerView recyclerView,recyclerView1;
    static TaskAdapter taskAdapter,taskAdapter1;
    AlertDialog ad;
    Vibrator v;
    static View mainView;
    static boolean completedView=false;

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
        mainView=view;

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton button=view.findViewById(R.id.imageButton2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask(v);
            }
        });

        recyclerView=(RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        v = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);

        taskAdapter=new TaskAdapter(taskList,getActivity());
        recyclerView.setAdapter(taskAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                //   Toast.makeText(getActivity(), position+ " is selected successfully", Toast.LENGTH_SHORT).show();

                //handle click event.
                MainActivity.switchFragment(view,position,false);
                Log.i("recycler","OnClickListener is being called");

            }

            @Override
            public void onLongClick(final View view, final int position) {

                createOptions(view,position,taskList,completedTaskList,taskAdapter,taskAdapter1,true);

            }
        }));

        recyclerView1=(RecyclerView)view.findViewById(R.id.recycler3);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));

        taskAdapter1=new TaskAdapter(completedTaskList,getActivity());
        recyclerView1.setAdapter(taskAdapter1);

        recyclerView1.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView1, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //
                MainActivity.switchFragment(view,position,true);
            }

            @Override
            public void onLongClick(View view, int position) {
                createOptions(view,position,completedTaskList,taskList,taskAdapter1,taskAdapter,false);
            }
        }));

        CardView cardView=(CardView)view.findViewById(R.id.cardView1);
        TextView textView=(TextView)view.findViewById(R.id.textView5);

        textView.setText("("+completedTaskList.size()+")");
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout frameLayout=mainView.findViewById(R.id.frameLayout2);
                LinearLayout linearLayout=mainView.findViewById(R.id.linearLayout3);
                ImageView imageView=mainView.findViewById(R.id.imageView);

                if(completedView){
                    //completedView On.Switch to Normalview.
                    frameLayout.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    imageView.setRotation(0);
                }
                else
                {
                    //NormalView On.Switch to CompletedView.
                    frameLayout.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    imageView.setRotation(180);
                }
                completedView=!completedView;
                Log.i("mainView","View changed "+String.valueOf(completedView));
            }
        });

    }

    private void createOptions(final View view, final int position, final ArrayList<Task> taskList, final ArrayList<Task> completedTaskList, final TaskAdapter taskAdapter, final TaskAdapter taskAdapter1, final boolean b) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(35,VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            //deprecated in API 26
            v.vibrate(35);
        }
        LayoutInflater li=LayoutInflater.from(getContext());
        final View view1=li.inflate(R.layout.itemoption,null);

        AlertDialog.Builder alertbuilder=new AlertDialog.Builder(getContext());
        alertbuilder.setView(view1);

        TextView textView=(TextView)view1.findViewById(R.id.editItem);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTask(taskList,taskAdapter,position);
                ad.dismiss();
            }
        });

        final TextView completedCount=(TextView)mainView.findViewById(R.id.textView5);


        textView=view1.findViewById(R.id.deleteItem);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getActivity())
                        .setMessage("This will delete the task and it's subtasks")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String old=taskList.get(position).getTitle();
                                taskList.remove(position);
                                taskAdapter.notifyDataSetChanged();
                                SQLiteStatement statement=MainActivity.database.compileStatement("DELETE FROM tasklist WHERE title=?");
                                statement.bindString(1,old);
                                statement.execute();

                                statement=MainActivity.database.compileStatement("DELETE FROM subtasklist WHERE tasktitle=?");
                                statement.bindString(1,old);
                                statement.execute();
                                completedCount.setText("("+MainFragment.completedTaskList.size()+")");
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create().show();
                ad.dismiss();

            }
        });

        TextView textView1=view1.findViewById(R.id.setComplete);
        final ToggleButton button1=view.findViewById(R.id.completed);

        textView1.setOnClickListener(new View.OnClickListener() {
            SQLiteStatement statement;

            @Override
            public void onClick(View v) {
                TextView textView=view.findViewById(R.id.textView2);
                String temp=textView.getText().toString();
                int rate=Integer.parseInt(temp.substring(0,temp.length()-1));
                if(button1.isChecked()) {
                    if(rate==100&&!b){
                        new AlertDialog.Builder(getContext())
                                .setMessage("This will reset your progress.")
                                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //delete progress.
                                        try{
                                            MainActivity.database.execSQL("UPDATE subtasklist SET completed=0 WHERE tasktitle='"+taskList.get(position).getTitle()+"'");

                                            button1.setChecked(false);
                                            statement=MainActivity.database.compileStatement("UPDATE tasklist SET completed=0 WHERE title=?");
                                            statement.bindString(1,taskList.get(position).getTitle());
                                            statement.execute();
                                            getTaskList(completedTaskList,b);
                                            getTaskList(taskList,!b);
                                            taskAdapter.notifyDataSetChanged();
                                            taskAdapter1.notifyDataSetChanged();
                                        }   catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).create().show();
                    }
                    else{
                        button1.setChecked(false);
                        taskList.get(position).setCompleted(false);
////
                        statement=MainActivity.database.compileStatement("UPDATE tasklist SET completed=0 WHERE title=?");
                        statement.bindString(1,taskList.get(position).getTitle());
                        statement.execute();
                    }
                }
                else {
                    button1.setChecked(true);
                    taskList.get(position).setCompleted(true);
                    statement=MainActivity.database.compileStatement("UPDATE tasklist SET completed=1 WHERE title=?");
                    statement.bindString(1,taskList.get(position).getTitle());
                    statement.execute();
                }

                ad.dismiss();
                Log.i("list","list being updated");
                getTaskList(completedTaskList,b);
                getTaskList(taskList,!b);
                taskAdapter.notifyDataSetChanged();
                taskAdapter1.notifyDataSetChanged();
                completedCount.setText("("+MainFragment.completedTaskList.size()+")");
            }
        });

        if(button1.isChecked())
            textView1.setText("Set Uncomplete");
        else
            textView1.setText("Set Complete");

        ad=alertbuilder.create();
        ad.show();

    }

    public void editTask(final ArrayList<Task> taskList,final TaskAdapter taskAdapter,final int position) {

        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        View view= LayoutInflater.from(getContext()).inflate(R.layout.custom_alert,null);

        final EditText editText=view.findViewById(R.id.editText);
        editText.setText(taskList.get(position).getTitle());

        final String old=taskList.get(position).getTitle();
        builder.setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        taskList.get(position).setTitle(editText.getText().toString());
                        taskAdapter.notifyDataSetChanged();
                        //save locally now
                        SQLiteStatement statement=MainActivity.database.compileStatement("UPDATE tasklist SET title=? WHERE title=?");
                        statement.bindString(1,editText.getText().toString());
                        statement.bindString(2, old);
                        statement.execute();


                        statement=MainActivity.database.compileStatement("UPDATE subtasklist SET tasktitle=? WHERE tasktitle=?");
                        statement.bindString(1,editText.getText().toString());
                        statement.bindString(2,old);
                        statement.execute();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();
    }


    public static void getTaskList(ArrayList<Task> taskList,boolean completed) {
        Cursor c;
        if(taskList!=null&&taskList.size()>0)
            taskList.clear();
        if(completed)
            c=MainActivity.database.rawQuery("SELECT * FROM tasklist WHERE completed=1",null);
        else
            c=MainActivity.database.rawQuery("SELECT * FROM tasklist WHERE completed=0",null);
        int taskIndex=c.getColumnIndex("title");
        if(c.moveToNext()){
            int rate;
            do{
                Cursor total=MainActivity.database.rawQuery("SELECT * FROM subtasklist WHERE tasktitle="+"'"+c.getString(taskIndex)+"'",null);
                Cursor foundComplete=MainActivity.database.rawQuery("SELECT * FROM subtasklist WHERE tasktitle="+"'"+c.getString(taskIndex)+"' AND completed=1",null);

                if(total.moveToNext()) {
                    rate = (foundComplete.getCount() * 100) / total.getCount();
                    Log.i("rate", String.valueOf(rate));
                }
                else rate=0;
                boolean temp;
                if(rate==100)
                    temp=true;
                else
                    temp=completed;
                taskList.add(new Task(c.getString(taskIndex),temp,rate));

            }   while(c.moveToNext());
        }
        else{
            if(!completed)
            taskList.add(new Task("Demo Task",false,0));
        }
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

                        try{
                            SQLiteStatement statement=MainActivity.database.compileStatement("INSERT INTO tasklist (TITLE,COMPLETED) VALUES(?,0)");
                            statement.bindString(1,editText.getText().toString());
                            statement.execute();
                        }   catch (Exception e){
                            e.printStackTrace();
                        }

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
