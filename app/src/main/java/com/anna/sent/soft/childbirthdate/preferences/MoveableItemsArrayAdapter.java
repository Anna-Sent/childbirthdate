package com.anna.sent.soft.childbirthdate.preferences;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.age.LocalizableObject;

import java.util.List;

public class MoveableItemsArrayAdapter extends ArrayAdapter<String> implements OnClickListener {
    private final List<LocalizableObject> mValues;

    MoveableItemsArrayAdapter(Context context, List<LocalizableObject> values) {
        super(context, R.layout.dialog_list_item);
        mValues = values;
    }

    public List<LocalizableObject> getValues() {
        return mValues;
    }

    @Override
    public int getCount() {
        return mValues.size();
    }

    @NonNull
    @SuppressWarnings("InflateParams")
    @Override
    public View getView(int position, View contentView, @NonNull ViewGroup viewGroup) {
        View view;
        ViewHolder viewHolder;
        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.dialog_list_item, null);

            viewHolder = new ViewHolder();

            view.setTag(viewHolder);

            viewHolder.textView = view.findViewById(R.id.textViewItem);

            Button buttonUp = view.findViewById(R.id.buttonUp);
            buttonUp.setTag(viewHolder);
            buttonUp.setOnClickListener(this);

            Button buttonDown = view.findViewById(R.id.buttonDown);
            buttonDown.setTag(viewHolder);
            buttonDown.setOnClickListener(this);

            Button buttonDelete = view.findViewById(R.id.buttonDelete);
            buttonDelete.setTag(viewHolder);
            buttonDelete.setOnClickListener(this);
        } else {
            view = contentView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.position = position;
        viewHolder.textView.setText(mValues.get(position).toString(getContext()));

        return view;
    }

    void setItems(List<LocalizableObject> objects) {
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

    private static class ViewHolder {
        private int position;
        private TextView textView;
    }
}
