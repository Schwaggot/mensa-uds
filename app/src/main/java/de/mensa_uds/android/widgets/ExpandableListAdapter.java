package de.mensa_uds.android.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

import redfox.android.mensa.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> content;

    public ExpandableListAdapter(Context context, List<String> listChildData) {
        this.context = context;
        this.content = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.content.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, parent, false);
        }

        TextView txtListChild = convertView.findViewById(R.id.lblListItem);

        txtListChild.setText(childText);


        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        animation.setStartOffset(childPosition * 100);
        txtListChild.startAnimation(animation);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.content.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.content;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, parent, false);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setText("Inhaltsstoffe");

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}