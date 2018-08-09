package com.vikash.todotask;

import android.content.DialogInterface;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            Window window=getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.window,null));
        }
        fragment=new MainFragment();
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
