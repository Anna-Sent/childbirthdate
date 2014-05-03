package com.anna.sent.soft.childbirthdate.adapters;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.anna.sent.soft.childbirthdate.age.LocalizableObject;

@SuppressLint("ViewConstructor")
public class HackedLocalizableSimpleSpinnerItemArrayAdapter extends
		LocalizableSimpleSpinnerItemArrayAdapter {
	private LocalizableObject mSelectedItem;

	public LocalizableObject getSelectedItem() {
		return mSelectedItem;
	}

	public void setSelectedObject(LocalizableObject value) {
		mSelectedItem = value;
		notifyDataSetChanged();
	}

	public HackedLocalizableSimpleSpinnerItemArrayAdapter(Context context,
			List<LocalizableObject> objects) {
		super(context, objects);
	}

	@Override
	public View getView(int position, View contentView, ViewGroup viewGroup) {
		View v = super.getView(position, contentView, viewGroup);

		if (mSelectedItem != null) {
			ViewHolder viewHolder = (ViewHolder) v.getTag();
			viewHolder.textView.setText(mSelectedItem.toString(getContext()));
		}

		return v;
	}
}