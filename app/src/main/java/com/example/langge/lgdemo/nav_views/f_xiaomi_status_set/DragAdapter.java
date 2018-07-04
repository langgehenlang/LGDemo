package com.example.langge.lgdemo.nav_views.f_xiaomi_status_set;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.langge.lgdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by langge on 18-3-27.
 */
public class DragAdapter extends BaseAdapter {

    private Context mContext;
    private int finalAdded = 0;
    private List<DragItemClass> mAddedItems;
    private List<DragItemClass> mNotAddedItems;

    public static final int TYPE_TAG = 0;
    public static final int TYPE_ITEM = 1;


    private String tag = "";

    public DragAdapter(Context context, ArrayList<DragItemClass> added, ArrayList<DragItemClass> notAdded) {
        this.mContext = context;
        mAddedItems = added;
        mNotAddedItems = notAdded;
        finalAdded = mAddedItems.size();

        tag = String.format(context.getResources().getString(R.string.drag_tag_text), mAddedItems.size());
    }

    @Override
    public int getCount() {
        return 1+mAddedItems.size()+mNotAddedItems.size();
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==mAddedItems.size()){
            return TYPE_TAG;
        }else {
            return TYPE_ITEM;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder=null;
        if (convertView==null){
            if (getItemViewType(position)==TYPE_ITEM){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.drag_list_item, null);
                viewHolder = new ViewHolder(convertView);
            }else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.drag_list_item_tag, null);
                viewHolder = new ViewHolder(convertView);
            }
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        DragItemClass itemObj = getLDragItem(position);
        if(getItemViewType(position)==TYPE_ITEM){
            viewHolder.nameView.setText(itemObj.name);
        }else {
            viewHolder.nameView.setText(tag);
            viewHolder.nameView.setEnabled(false);
        }

        return convertView;
    }


    class ViewHolder{
        TextView nameView;
        ImageView iconView;
        public ViewHolder(View view){
            nameView = (TextView)view.findViewById(R.id.drag_list_item_text);

            view.setTag(this);
        }
    }


    private DragItemClass getLDragItem(int position){
        if (position==mAddedItems.size()){
            return null;
        }
        else if(position<mAddedItems.size()){
            //add
            return mAddedItems.get(position);
        }else{
            //not add
            return mNotAddedItems.get(position-mAddedItems.size()-1);
        }
    }


    public boolean isTag(int pos){
        if( getItemViewType(pos)==TYPE_TAG){
            return true;
        }
        return false;
    }

    public boolean canDrag(int pos, View dragView, int x, int y){


        if( getItemViewType(pos)==TYPE_TAG){
            return false;
        }
        else {
            if (dragView == null) {
                return false;
            }
            View dragger = dragView.findViewById(R.id.drag_list_item_drag);
            if (dragger == null || dragger.getVisibility() != View.VISIBLE) {
                return false;
            }

            float tx = x - getViewX(dragView);
            float ty = y - getViewY(dragView);
            Rect mFrame = new Rect();
            dragger.getHitRect(mFrame);
            if (mFrame.contains((int) tx, (int) ty)) { // 当点击拖拽图标才可进行拖拽
                return true;
            }

            return false;
        }
    }


    public boolean exchange(int scrPos, int desPos){
        boolean success = false;
        int count = mAddedItems.size();
        if(scrPos==count || desPos==count){
            return false;
        }

        if(scrPos!=desPos){
            if(scrPos<count){
                DragItemClass srcObj = mAddedItems.get(scrPos);
                if (desPos<count){
                    //change in add rang
                    DragItemClass desObj = mAddedItems.get(desPos);
                    mAddedItems.set(scrPos, desObj);
                    mAddedItems.set(desPos, srcObj);
                }else {
                    //change from add to notAdd
                    desPos = desPos-mAddedItems.size()-1;
                    DragItemClass desObj = mNotAddedItems.get(desPos);
                    mAddedItems.set(scrPos, desObj);
                    mNotAddedItems.set(desPos, srcObj);
                }

            }else {
                scrPos = scrPos-mAddedItems.size()-1;
                DragItemClass srcObj = mNotAddedItems.get(scrPos);
                if (desPos<count){
                    //change from noTAdd to add
                    DragItemClass desObj = mAddedItems.get(desPos);
                    mAddedItems.set(desPos, srcObj);
                    mNotAddedItems.set(scrPos, desObj);
                }else {
                    //change in notAdd rang
                    desPos = desPos-mAddedItems.size()-1;
                    DragItemClass desObj = mNotAddedItems.get(desPos);
                    mNotAddedItems.set(scrPos, desObj);
                    mNotAddedItems.set(desPos, srcObj);
                }
            }
            success = true;
        }

        if (success) {
            notifyDataSetChanged();
        }
        return success;

    }

    public float getViewX(View view) {
        if (Build.VERSION.SDK_INT >= 11) {
            return view.getX();
        } else {
            return view.getLeft() + view.getTranslationX();
        }
    }

    public float getViewY(View view) {
        if (Build.VERSION.SDK_INT >= 11) {
            return view.getY();
        } else {
            return view.getTop() + view.getTranslationY();
        }
    }

}
