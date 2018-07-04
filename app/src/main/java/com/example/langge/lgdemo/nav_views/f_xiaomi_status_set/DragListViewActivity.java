package com.example.langge.lgdemo.nav_views.f_xiaomi_status_set;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.langge.lgdemo.R;

import java.util.ArrayList;

public class DragListViewActivity extends Activity {

    private static final String TAG = "DragListViewActivity";
    private String data[] = new String[]{"WLAN","截屏","数据","GPS","静音","飞行模式","蓝牙","方向锁屏","自动亮度","手电筒","省电模式","快传","振动","屏蔽按键","勿扰模式","锁屏","同步","护眼模式"};
    private DragAdapter adapter = null;
    private DragListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listView = new DragListView(this);
        addContentView(listView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        listView.setBackgroundColor(Color.WHITE);
        listView.setDividerHeight(1);
        listView.setDivider(getDrawable(R.drawable.drag_list_item_divider));
        listView.setSelector(R.drawable.drag_list_selector);


        ArrayList<DragItemClass> addlist = new ArrayList<DragItemClass>();
        ArrayList<DragItemClass> notAddlist = new ArrayList<DragItemClass>();

        for(int i=0; i<data.length; i++){
            DragItemClass item = new DragItemClass();
            item.name = data[i];
            if(i>10){
                notAddlist.add(item);
            }else {
                addlist.add(item);
            }
        }

        adapter = new DragAdapter(this, addlist, notAddlist);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "setOnItemLongClickListener pos="+position);
                listView.startDragOnLong();
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(DragListViewActivity.this, ""+position, Toast.LENGTH_SHORT).show();
            }
        });

    }


}
