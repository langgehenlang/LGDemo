package com.example.langge.lgdemo.nav_binder;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NavBinderActivity extends ListActivity {


    private String[] mDta = {"仿小米状态栏编辑界面"};
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

                break;
            case 1:

                break;
            default:
        }

    }
}
