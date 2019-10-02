package com.vikash.todotask;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class SecondFragment extends Fragment {

    static ArrayList<Task.SubTask> subTasks;
    RecyclerView recyclerView1;
    SubTaskAdapter subTaskAdapter;
    static int index;
    static View view,view2;
    Task task;
    AlertDialog ad1;
    Vibrator v;
    static boolean completed;



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
        if(subTasks==null)
            subTasks=new ArrayList<Task.SubTask>();

        v = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);


        recyclerView1=(RecyclerView)view.findViewById(R.id.recycler2);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView1.setHasFixedSize(true);

        subTaskAdapter=new SubTaskAdapter(subTasks,getActivity());
        recyclerView1.setAdapter(subTaskAdapter);

        if(MainFragment.taskList.size()!=0)
            refreshFragment();



        ImageButton button=view.findViewById(R.id.imageButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask(v);
            }
        });

        recyclerView1.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView1, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {
                Log.i("recycler","OnClickListener is being called from secondFragment");
                return;
            }

            @Override
            public void onLongClick(final View view1, final int position) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(35,VibrationEffect.DEFAULT_AMPLITUDE));
                }else{
                    //deprecated in API 26
                    v.vibrate(35);
                }
                Log.i("recycler","OnLongClickListener is being called on "+position+" from secondFragment");
                View view2 = LayoutInflater.from(getContext()).inflate(R.layout.itemoption, null);

                AlertDialog.Builder alertbuilder = new AlertDialog.Builder(getContext());
                alertbuilder.setView(view2);

                TextView textView = view2.findViewById(R.id.editItem);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editSubTask(position);
                        ad1.dismiss();
                    }
                });
                textView = view2.findViewById(R.id.deleteItem);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String oldtitle = subTasks.get(position).getTaskTitle();
                        subTasks.remove(position);
                        subTaskAdapter.notifyDataSetChanged();
                        ad1.dismiss();
                        //Remove locally.
                        SQLiteStatement statement = MainActivity.database.compileStatement("DELETE FROM subtasklist WHERE description=?");
                        statement.bindString(1, oldtitle);
                        statement.execute();

                    }
                });

                TextView textView1=view2.findViewById(R.id.setComplete);

                textView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToggleButton button1=view1.findViewById(R.id.toggleButton3);
                        if(button1.isChecked()) {
                            button1.setChecked(false);
                            SQLiteStatement statement=MainActivity.database.compileStatement("UPDATE subtasklist SET completed=0 WHERE description=?");
                            statement.bindString(1,task.subTasks.get(position).getTaskTitle());
                            statement.execute();

                        }
                        else {
                            button1.setChecked(true);
                            SQLiteStatement statement = MainActivity.database.compileStatement("UPDATE subtasklist SET completed=1 WHERE description=?");
                            statement.bindString(1, task.subTasks.get(position).getTaskTitle());
                            statement.execute();

                        }
                        ad1.dismiss();
                        MainFragment.getTaskList(MainFragment.taskList,false);
                        MainFragment.getTaskList(MainFragment.completedTaskList,true);
                        MainFragment.taskAdapter.notifyDataSetChanged();
                        MainFragment.taskAdapter1.notifyDataSetChanged();
                        refreshFragment();


                        updateInfo(view);

                    }
                });

                ToggleButton toggleButton=view1.findViewById(R.id.toggleButton3);
                if(toggleButton.isChecked())
                    textView1.setText("Set Uncomplete");
                else
                    textView1.setText("Set Complete");




                ad1 = alertbuilder.create();
                ad1.show();

            }

        }));



    }

    public void editSubTask(final int position){
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.subtask_input,null);
        view2=view;

        Button button=view.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment=new DatePickerFragment();
                newFragment.show(getFragmentManager(),"datepicker");
            }
        });

        final EditText editText=view.findViewById(R.id.editText1);
        editText.setText(subTasks.get(position).getTaskTitle());

        final TextView textView1=view.findViewById(R.id.textView6);
        textView1.setText(subTasks.get(position).getEndDate().substring(6));

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String oldsubtitle=subTasks.get(position).getTaskTitle();
                        String tasktitle=MainFragment.taskList.get(index).getTitle();
                        subTasks.get(position).setTaskTitle(editText.getText().toString());
                        subTasks.get(position).setEndDate("End : "+textView1.getText().toString());

                        subTaskAdapter.notifyDataSetChanged();
                        //Save locally.
                        SQLiteStatement statement=MainActivity.database.compileStatement("UPDATE subtasklist SET description=?,enddate=? WHERE description=? and tasktitle=?");
                        statement.bindString(1,editText.getText().toString());
                        statement.bindString(2,"End : "+textView1.getText().toString());
                        statement.bindString(3,oldsubtitle);
                        statement.bindString(4,tasktitle);
                        statement.execute();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create().show();

    }

    public void addTask(View v){

        LayoutInflater li=LayoutInflater.from(getContext());
        View view1=li.inflate(R.layout.subtask_input,null);
        view2=view1;

        AlertDialog.Builder alertdialog=new AlertDialog.Builder(getContext());
        alertdialog.setView(view1);

        final EditText editText=(EditText)view1.findViewById(R.id.editText1);
        final TextView textView1=(TextView) view1.findViewById(R.id.textView6) ;
        editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        Button button=view1.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment=new DatePickerFragment();
                newFragment.show(getFragmentManager(),"datepicker");
            }
        });

        DateFormat df = new SimpleDateFormat("d MMM yyyy");
        final String date = df.format(Calendar.getInstance().getTime());
        alertdialog
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //save the title here.
                        String start="Start : "+date,end="End : ";
                        try {
                            SQLiteStatement statement = MainActivity.database.compileStatement("INSERT INTO subtasklist(tasktitle,description,startdate,enddate,completed) VALUES(?,?,?,?,?)");
                            statement.bindString(1, task.title);
                            statement.bindString(2, editText.getText().toString());
                            statement.bindString(3, start);
                            if(textView1.getText().toString()=="")
                                end+="NA";
                            else
                                end+=textView1.getText().toString();
                            statement.bindString(4, end );
                            statement.bindLong(5,0);
                            statement.execute();
                        }   catch (Exception e){
                            e.printStackTrace();
                        }
                        updateInfo(view);
                        refreshFragment();

                    }})
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        alertdialog.show();


    }

    public void updateInfo(View view){

        Cursor total=MainActivity.database.rawQuery("SELECT * FROM subtasklist WHERE tasktitle="+"'"+task.getTitle()+"'",null);
        Cursor foundComplete=MainActivity.database.rawQuery("SELECT * FROM subtasklist WHERE tasktitle="+"'"+task.getTitle()+"' AND completed=1",null);
        int rate = (foundComplete.getCount() * 100) / total.getCount();
        task.setCompletionRate(rate);
        if(rate==100){
            ToggleButton toggleButton=view.findViewById(R.id.completed1);
            toggleButton.setChecked(true);
            task.setCompleted(true);
            MainActivity.database.execSQL("UPDATE tasklist SET completed=1 WHERE title='"+task.getTitle()+"'");
            MainFragment.getTaskList(MainFragment.taskList,false);
            MainFragment.getTaskList(MainFragment.completedTaskList,true);
            TextView textView=MainFragment.mainView.findViewById(R.id.textView5);
            textView.setText("("+MainFragment.completedTaskList.size()+")");
        }
        TextView textView=view.findViewById(R.id.completionview);
        textView.setText(String.valueOf(rate)+"%");
        MainFragment.taskAdapter.notifyDataSetChanged();
        MainFragment.taskAdapter1.notifyDataSetChanged();
    }

    public void setToggleButton(){
        ToggleButton toggleButton=view.findViewById(R.id.completed1);
        if(task.isCompleted()) {
            toggleButton.setChecked(true);

        }
        else
            toggleButton.setChecked(false);
        toggleButton.setText(null);
        toggleButton.setTextOn(null);
        toggleButton.setTextOff(null);
    }


    public void refreshFragment(){
        boolean temp;
        if(completed)
            task=MainFragment.completedTaskList.get(index);
        else
            task=MainFragment.taskList.get(index);
        if(task.subTasks==null)
            task.subTasks=new ArrayList<Task.SubTask>();
        task.subTasks.clear();

        Cursor c=MainActivity.database.rawQuery("SELECT * FROM subtasklist WHERE tasktitle='"+task.getTitle()+"'",null);
        if(c.moveToNext()){

            do {
                if(Integer.parseInt(c.getString(c.getColumnIndex("completed")))==0)
                    temp=false;
                else
                    temp=true;
                Log.i("complete",String.valueOf(temp));
                task.subTasks.add(new Task.SubTask(c.getString(c.getColumnIndex("startdate")),c.getString(c.getColumnIndex("enddate")),c.getString(c.getColumnIndex("description")),temp));
            }   while(c.moveToNext());
        }


        subTasks.clear();
        for(Task.SubTask sub: task.subTasks){
            subTasks.add(sub);
        }
        subTaskAdapter.notifyDataSetChanged();

        setToggleButton();

        TextView title=view.findViewById(R.id.titletextView);
        title.setText(task.getTitle());

        TextView completionRate=view.findViewById(R.id.completionview);
        completionRate.setText(task.getCompletionRate()+"%");

        CardView cardView=view.findViewById(R.id.cardView3);
        ToggleButton toggleButton=view.findViewById(R.id.completed1);
        int backgroundColor;
        if(toggleButton.isChecked())
                backgroundColor=getActivity().getResources().getColor(R.color.grey);
            else
                backgroundColor=ColorGenerator.colorForPosition(index,getActivity());
        cardView.setCardBackgroundColor(backgroundColor);
        RelativeLayout layout=view.findViewById(R.id.relativeLayout3);
        int color=ColorGenerator.colorFader(backgroundColor);
        layout.setBackgroundColor(color);

        recyclerView1.setBackgroundColor(color);
        LinearLayout layout1=view.findViewById(R.id.backLayout);
        layout1.setBackgroundColor(color);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view1, int year, int month, int day) {
            // Do something with the date chosen by the user
            DateFormat df = new SimpleDateFormat("d MMM yyyy");
            final String date = df.format(new GregorianCalendar(year, month, day).getTime());
            TextView textView=(TextView)view2.findViewById(R.id.textView6);
            textView.setText(date);
        }
    }
}
