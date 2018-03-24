package com.anna.sent.soft.childbirthdate.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.age.LocalizableObject;

import java.util.List;

public class LocalizableSimpleSpinnerItemArrayAdapter extends ArrayAdapter<LocalizableObject> {
    private final List<LocalizableObject> mObjects;

    public LocalizableSimpleSpinnerItemArrayAdapter(Context context,
                                                    List<LocalizableObject> objects) {
        super(context, android.R.layout.simple_spinner_item, objects);
        mObjects = objects;
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
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(
                    isDropDownView
                            ? android.R.layout.simple_spinner_dropdown_item
                            : android.R.layout.simple_spinner_item,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.textView = view.findViewById(android.R.id.text1);

            viewHolder.textView.setMinHeight(getContext().getResources()
                    .getDimensionPixelSize(R.dimen.height));
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
