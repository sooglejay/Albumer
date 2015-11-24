package sooglejay.youtu.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import sooglejay.youtu.R;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aaa_activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle("一直");
        setSupportActionBar(toolbar);

        //StatusBarCompat.compat(this, 0xFFFF0000);
         StatusBarCompat.compat(this);

    }

}
