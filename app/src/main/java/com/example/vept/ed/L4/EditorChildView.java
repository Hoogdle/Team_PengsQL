package com.example.vept.ed.L4;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditorChildView extends SimpleExpandableListAdapter {
    private int selectedGroupPosition;
    private int selectedChildPosition;
    private Context context;
    public EditorChildView(Context context, List<? extends Map<String, ?>> groupData, int groupLayout, String[] groupFrom, int[] groupTo, List<? extends List<? extends Map<String, ?>>> childData, int childLayout, String[] childFrom, int[] childTo) {
        super(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout, childFrom, childTo);
        this.context = context;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        final String childText = ((HashMap<String,String>) getChild(groupPosition, childPosition)).get("name");

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        TextView txtListChild = convertView.findViewById(android.R.id.text1);
        txtListChild.setText(childText);

        // 선택된 아이템이면 배경을 검은색으로 설정
        if (groupPosition == selectedGroupPosition && childPosition == selectedChildPosition) {
            convertView.setBackgroundColor(Color.BLACK);
            txtListChild.setTextColor(Color.WHITE);
        } else {
            convertView.setBackgroundColor(Color.WHITE);
            txtListChild.setTextColor(Color.BLACK);
        }

        return convertView;
    }

    public void setSelectedItem(int groupPosition, int childPosition) {
        this.selectedGroupPosition = groupPosition;
        this.selectedChildPosition = childPosition;
        notifyDataSetChanged();
    }

    public String getSelectedItemName() {
        final String childText = ((HashMap<String,String>) getChild(this.selectedGroupPosition, this.selectedChildPosition)).get("name");
        return childText;
    }
}
