package com.anna.sent.soft.childbirthdate.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.age.LocalizableObject;
import com.anna.sent.soft.childbirthdate.utils.MyLog;

import java.util.List;

public class MoveableItemsArrayAdapter extends ArrayAdapter<String> implements
        OnClickListener {
    private String wrapMsg(String msg) {
        return getClass().getSimpleName() + ": " + msg;
    }

    private void log(String msg) {
        MyLog.getInstance().logcat(Log.DEBUG, wrapMsg(msg));
    }

    private final List<LocalizableObject> mValues;

    public List<LocalizableObject> getValues() {
        return mValues;
    }

    private static class ViewHolder {
        private int position;
        private TextView textView;
    }

    public MoveableItemsArrayAdapter(Context context,
                                     List<LocalizableObject> values) {
        super(context, R.layout.dialog_list_item);
        mValues = values;
    }

    @Override
    public int getCount() {
        return mValues.size();
    }

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View contentView, @NonNull ViewGroup viewGroup) {
        View view;
        ViewHolder viewHolder;
        if (contentView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.dialog_list_item, null);

            viewHolder = new ViewHolder();

            view.setTag(viewHolder);

            viewHolder.textView = (TextView) view
                    .findViewById(R.id.textViewItem);

            Button buttonUp = (Button) view.findViewById(R.id.buttonUp);
            buttonUp.setTag(viewHolder);
            buttonUp.setOnClickListener(this);

            Button buttonDown = (Button) view.findViewById(R.id.buttonDown);
            buttonDown.setTag(viewHolder);
            buttonDown.setOnClickListener(this);

            Button buttonDelete = (Button) view.findViewById(R.id.buttonDelete);
            buttonDelete.setTag(viewHolder);
            buttonDelete.setOnClickListener(this);
        } else {
            view = contentView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.position = position;
        viewHolder.textView.setText(mValues.get(position)
                .toString(getContext()));

        return view;
    }

    public void setItems(List<LocalizableObject> objects) {
        mValues.clear();
        mValues.addAll(objects);
        notifyDataSetChanged();
    }

    public void addItem(LocalizableObject object) {
        mValues.add(object);
        notifyDataSetChanged();
    }

    private void removeItem(int position) {
        if (position < mValues.size()) {
            mValues.remove(position);
            notifyDataSetChanged();
        }
    }

    private void upItem(int position) {
        if (position < 0 || position >= mValues.size()) {
            return;
        }

        LocalizableObject object = mValues.get(position);
        if (position > 0) {
            mValues.remove(position);
            mValues.add(position - 1, object);
            notifyDataSetChanged();
        }
    }

    private void downItem(int position) {
        if (position < 0 || position >= mValues.size()) {
            return;
        }

        LocalizableObject object = mValues.get(position);
        if (position < mValues.size() - 1) {
            mValues.remove(position);
            mValues.add(position + 1, object);
            notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag();
        if (viewHolder != null) {
            int position = viewHolder.position;
            switch (v.getId()) {
                case R.id.buttonUp:
                    upItem(position);
                    break;
                case R.id.buttonDown:
                    downItem(position);
                    break;
                case R.id.buttonDelete:
                    removeItem(position);
                    break;
            }
        }
    }
}