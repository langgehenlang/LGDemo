package com.example.langge.lgdemo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.langge.lgdemo.nav_binder.NavBinderActivity;
import com.example.langge.lgdemo.nav_frame.logger.NavLoggerActivity;
import com.example.langge.lgdemo.nav_views.NavViewsActivity;

public class MainActivity extends ListActivity {


    private String[] mDta = {"binder相关", "自定义view相关", "框架"};
    private ArrayAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mDta);
        setListAdapter(mAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        switch (position){
            case 0:
                startActivity(new Intent(MainActivity.this, NavBinderActivity.class));
                break;
            case 1:
                startActivity(new Intent(MainActivity.this, NavViewsActivity.class));
                break;
            case 2:
                startActivity(new Intent(MainActivity.this, NavLoggerActivity.class));
                break;
            default:
        }

    }
}
