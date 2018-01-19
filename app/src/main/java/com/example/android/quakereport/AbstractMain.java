package com.example.android.quakereport;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ntumba on 17-12-28.
 */

public abstract class AbstractMain extends AppCompatActivity {


    protected abstract Fragment getFragment();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.container);


        if(fragment == null){
            fragment = getFragment();
            manager.beginTransaction().add(R.id.container , fragment).commit();
        }
    }
}
