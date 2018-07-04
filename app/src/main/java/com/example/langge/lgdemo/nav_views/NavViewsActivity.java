package com.example.langge.lgdemo.nav_views;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.langge.lgdemo.nav_views.f_xiaomi_status_set.DragListViewActivity;

public class NavViewsActivity extends ListActivity {


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
                startActivity(new Intent(NavViewsActivity.this, DragListViewActivity.class));
                break;
            case 1:

                break;
            default:
        }

    }
}
