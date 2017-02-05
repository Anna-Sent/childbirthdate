package com.anna.sent.soft.childbirthdate.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.age.LocalizableObject;
import com.anna.sent.soft.childbirthdate.utils.MyLog;

import java.util.List;

public class LocalizableSimpleSpinnerItemArrayAdapter extends
        ArrayAdapter<LocalizableObject> {
    private final List<LocalizableObject> mObjects;

    public LocalizableSimpleSpinnerItemArrayAdapter(Context context,
                                                    List<LocalizableObject> objects) {
        super(context, android.R.layout.simple_spinner_item, objects);
        mObjects = objects;
    }

    private String wrapMsg(String msg) {
        return getClass().getSimpleName() + ": " + msg;
    }

    private void log(String msg) {
        MyLog.getInstance().logcat(Log.DEBUG, wrapMsg(msg));
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @NonNull
    @Override
    public View getView(int position, View contentView, @NonNull ViewGroup viewGroup) {
        return getView(position, contentView, false);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, true);
    }

    private View getView(int position, View contentView, boolean isDropDownView) {
        View view;
        ViewHolder viewHolder;
        if (contentView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater
                    .inflate(
                            isDropDownView ? android.R.layout.simple_spinner_dropdown_item
                                    : android.R.layout.simple_spinner_item,
                            null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view
                    .findViewById(android.R.id.text1);

            viewHolder.textView.setMinHeight(getItemHeight()); // getListPreferredItemHeight());
            viewHolder.textView.setGravity(Gravity.CENTER_VERTICAL);

            view.setTag(viewHolder);
        } else {
            view = contentView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.textView.setText(mObjects.get(position).toString(
                getContext()));

        return view;
    }

    private int getItemHeight() {
        return getContext().getResources()
                .getDimensionPixelSize(R.dimen.height);
    }

    @SuppressWarnings("unused")
    private int getListPreferredItemHeight() {
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(
                android.R.attr.listPreferredItemHeight, typedValue, true);

        int[] attrs = new int[]{android.R.attr.listPreferredItemHeight};
        int indexOfAttr = 0;

        TypedArray a = getContext().obtainStyledAttributes(typedValue.data,
                attrs);
        int size = a.getDimensionPixelSize(indexOfAttr, -1);
        a.recycle();

        return size;
    }

    public List<LocalizableObject> getObjects() {
        return mObjects;
    }

    public int addObject(LocalizableObject object) {
        mObjects.add(object);
        notifyDataSetChanged();
        return mObjects.size() - 1;
    }

    static class ViewHolder {
        TextView textView;
    }
}
