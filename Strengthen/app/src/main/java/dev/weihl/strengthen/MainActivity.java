package dev.weihl.strengthen;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.expandableListView)
    ExpandableListView expandableListView;

    private ArrayList<String> mGroupList;
    private ArrayList<ArrayList<ItemData>> mChildList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mGroupList = new ArrayList<>();
        mChildList = new ArrayList<>();

        putActivityGroupList();


        Log.d(getClass().getSimpleName(),"mGroupList = "+mGroupList.toString());
        Log.d(getClass().getSimpleName(),"mChildList = "+mChildList.toString());
        expandableListView.setAdapter(new SampleExpandableAdapter());


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {

                Log.d(getClass().getSimpleName(),"groupPosition = "+groupPosition+" / childPosition = "+childPosition);
                ItemData data = (ItemData) view.getTag(R.id.obj);
                try {
                    Intent intent = new Intent(MainActivity.this,Class.forName(data.activityCls));
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });



    }

    private void putActivityGroupList() {
        try {
            PackageInfo mPackageInfo = getPackageManager()
                    .getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES | PackageManager.GET_META_DATA);

            mGroupList.addAll(findAllGroup(mPackageInfo.activities));

            for (String group : mGroupList) {
                putActivityChildList(group, mPackageInfo.activities);
            }


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void putActivityChildList(@NonNull String group, ActivityInfo[] activities) {
        ArrayList<ItemData> itemChildList = new ArrayList<>();
        for (ActivityInfo activityInfo : activities) {

            if(activityInfo.metaData == null) continue;

            String tGroup = activityInfo.metaData.getString("Group");
            if (group.equals(tGroup)) {
                ItemData tItemData = new ItemData();
                tItemData.group = tGroup;
                tItemData.name = activityInfo.metaData.getString("ChildName");
                tItemData.activityCls = activityInfo.name;
                itemChildList.add(tItemData);
            }
        }
        if (!itemChildList.isEmpty()) {
            mChildList.add(itemChildList);
        }
    }

    private ArrayList<String> findAllGroup(ActivityInfo[] activities) {
        ArrayList<String> tGroupList = new ArrayList<>();
        for (ActivityInfo activityInfo : activities) {
            if(activityInfo.metaData == null){
                continue;
            }
            String group = activityInfo.metaData.getString("Group");

            if (!tGroupList.contains(group)) {
                tGroupList.add(group);
            }
        }
        return tGroupList;
    }


    class ItemData {

        public String name;
        public String group;
        public String activityCls;

        @Override
        public String toString() {
            return "ItemData{" +
                    "activityCls='" + activityCls + '\'' +
                    ", name='" + name + '\'' +
                    ", group='" + group + '\'' +
                    '}';
        }
    }

    class SampleExpandableAdapter extends BaseExpandableListAdapter {


        @Override
        public int getGroupCount() {
            return mGroupList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mChildList.get(groupPosition).size();
        }

        @Override
        public String getGroup(int groupPosition) {
            return mGroupList.get(groupPosition);
        }

        @Override
        public ItemData getChild(int groupPosition, int childPosition) {
            return mChildList.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            // false 局部刷新
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            GroupHolder tHolder = null;
            if(convertView == null){
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.desgin_main_exlv_item_group,null);
                tHolder = new GroupHolder();
                tHolder.mTitle = (TextView) convertView;
                convertView.setTag(tHolder);
            }else {
                tHolder = (GroupHolder) convertView.getTag();
            }

            String groupName = mGroupList.get(groupPosition);
            tHolder.mTitle.setText(groupName);

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder tHolder = null;
            if(convertView == null){
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.desgin_main_exlv_item_child,null);
                tHolder = new ChildHolder();
                tHolder.mTitle = (TextView) convertView;
                convertView.setTag(tHolder);
            }else {
                tHolder = (ChildHolder) convertView.getTag();
            }

            ItemData itemData = mChildList.get(groupPosition).get(childPosition);
            tHolder.mTitle.setTag(R.id.obj,itemData);
            tHolder.mTitle.setText(itemData.name);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        class GroupHolder {
            public TextView mTitle;
        }

        class ChildHolder{
            public TextView mTitle;
        }

    }
}
