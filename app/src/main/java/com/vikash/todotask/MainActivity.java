package com.vikash.todotask;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MainFragment fragment;
    PageAdapter pageAdapter;
    static ViewPager viewPager;
    static SecondFragment secondFragment;
    static SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database=this.openOrCreateDatabase("Tasks",MODE_PRIVATE,null);
        database.execSQL("CREATE TABLE IF NOT EXISTS tasklist(title VARCHAR PRIMARY KEY,completed  int)");
        database.execSQL("CREATE TABLE IF NOT EXISTS subtasklist(tasktitle VARCHAR,description TEXT,startdate VARCHAR,enddate VARCHAR,completed int)");

        fragment=new MainFragment();
        MainFragment.getTaskList(MainFragment.taskList=new ArrayList<>());


        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            Window window=getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.window,null));
        }
        FragmentManager fm=getSupportFragmentManager();
        secondFragment=new SecondFragment();
        pageAdapter =new PageAdapter(fm);
        pageAdapter.addFragment(fragment);
        pageAdapter.addFragment(secondFragment);
        viewPager=(ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(0);

    }

    public static void switchFragment(View view, int index){
        SecondFragment.index=index;
        secondFragment.refreshFragment();
        viewPager.setCurrentItem(1);
    }




}
