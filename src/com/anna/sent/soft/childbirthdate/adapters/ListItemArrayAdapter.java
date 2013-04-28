package com.anna.sent.soft.childbirthdate.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.R;

public class ListItemArrayAdapter extends ArrayAdapter<String> implements
		OnClickListener {
	private static final String TAG = "moo";
	private static final boolean DEBUG = false;
	private static final boolean DEBUG_CREATION = false;

	private String wrapMsg(String msg) {
		return getClass().getSimpleName() + ": " + msg;
	}

	private void log(String msg, boolean scenario) {
		if (scenario) {
			Log.d(TAG, wrapMsg(msg));
		}
	}

	public interface OnCheckedListener {
		public void checked(int position, boolean isChecked);
	}

	private OnCheckedListener mListener = null;

	public void setOnCheckedListener(OnCheckedListener listener) {
		mListener = listener;
	}

	private String[] mStrings1;
	private String[] mStrings2;
	private boolean[] mChecked;
	private int mCount;

	private static class ViewHolder {
		private int position;
		private CheckBox checkBox;
		private TextView text1;
		private TextView text2;
	}

	public ListItemArrayAdapter(Context context, String[] strings1) {
		super(context, R.layout.list_item, R.id.text1, strings1);
		mStrings1 = strings1;
		mCount = mStrings1.length;
		mChecked = new boolean[mCount];
		mStrings2 = new String[mCount];
		for (int i = 0; i < mCount; ++i) {
			mChecked[i] = false;
			mStrings2[i] = "";
		}
	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup viewGroup) {
		log("get view " + position, DEBUG_CREATION);
		View view;
		ViewHolder viewHolder = null;
		if (contentView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.list_item, null);
			if (view == null) {
				log("view is not created", DEBUG_CREATION);
			} else {
				log("created view " + view.toString(), DEBUG_CREATION);
				viewHolder = new ViewHolder();
				viewHolder.checkBox = (CheckBox) view
						.findViewById(R.id.checkBox);
				viewHolder.text1 = (TextView) view.findViewById(R.id.text1);
				viewHolder.text2 = (TextView) view.findViewById(R.id.text2);
				viewHolder.position = position;
				view.setTag(viewHolder);
			}
		} else {
			view = contentView;
			log("existing view " + view.toString(), DEBUG_CREATION);
			viewHolder = (ViewHolder) view.getTag();
		}

		if (viewHolder != null) {
			log("update view holder", DEBUG_CREATION);
			viewHolder.checkBox.setChecked(mChecked[position]);
			viewHolder.checkBox.setOnClickListener(this);
			viewHolder.checkBox.setTag(viewHolder);
			viewHolder.text1.setText(mStrings1[position]);
			viewHolder.text2.setText(mStrings2[position]);
			viewHolder.text2
					.setVisibility(viewHolder.checkBox.isChecked() ? View.VISIBLE
							: View.GONE);
		}

		return view;
	}

	@Override
	public void onClick(View v) {
		ViewHolder viewHolder = (ViewHolder) v.getTag();
		if (viewHolder != null) {
			int position = viewHolder.position;
			mChecked[position] = viewHolder.checkBox.isChecked();
			viewHolder.text2
					.setVisibility(viewHolder.checkBox.isChecked() ? View.VISIBLE
							: View.GONE);
			if (mListener != null) {
				mListener.checked(position, mChecked[position]);
			}
		}
	}

	public void updateValues(boolean[] checked, String[] strings2) {
		if (checked.length == mCount && strings2.length == mCount) {
			log("update values", DEBUG);
			for (int i = 0; i < mCount; ++i) {
				mChecked[i] = checked[i];
				mStrings2[i] = strings2[i];
			}

			notifyDataSetChanged();
		}
	}

	public void updateValues(String[] strings2) {
		if (strings2.length == mCount) {
			log("update values", DEBUG);
			for (int i = 0; i < mCount; ++i) {
				mStrings2[i] = strings2[i];
			}

			notifyDataSetChanged();
		}
	}
}
