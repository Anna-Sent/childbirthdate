package com.anna.sent.soft.childbirthdate.adapters;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.age.LocalizableObject;

public class LocalizableSimpleSpinnerItemArrayAdapter extends
		ArrayAdapter<LocalizableObject> {
	private static final String TAG = "moo";
	private static final boolean DEBUG = false;

	private String wrapMsg(String msg) {
		return getClass().getSimpleName() + ": " + msg;
	}

	@SuppressWarnings("unused")
	private void log(String msg) {
		if (DEBUG) {
			Log.d(TAG, wrapMsg(msg));
		}
	}

	private List<LocalizableObject> mObjects;

	private static class ViewHolder {
		private TextView textView;
	}

	public LocalizableSimpleSpinnerItemArrayAdapter(Context context,
			List<LocalizableObject> objects) {
		super(context, android.R.layout.simple_spinner_item, objects);
		mObjects = objects;
	}

	@Override
	public int getCount() {
		return mObjects.size();
	}

	@Override
	public View getView(int position, View contentView, ViewGroup viewGroup) {
		return getView(position, contentView, false);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getView(position, convertView, true);
	}

	private View getView(int position, View contentView, boolean isDropDownView) {
		View view;
		ViewHolder viewHolder = null;
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
			view.setTag(viewHolder);
		} else {
			view = contentView;
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.textView.setText(mObjects.get(position).toString(
				getContext()));

		return view;
	}
}